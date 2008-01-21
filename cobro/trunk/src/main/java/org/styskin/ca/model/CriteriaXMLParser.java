/*
 *$Id$
 */
package org.styskin.ca.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.StringTokenizer;

import javax.sql.DataSource;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.styskin.ca.functions.ComplexCriteria;
import org.styskin.ca.functions.Criteria;
import org.styskin.ca.functions.IntegralCriteria;
import org.styskin.ca.functions.SingleCriteria;
import org.styskin.ca.functions.complex.BinaryOperator;
import org.styskin.ca.functions.single.SingleOperator;
import org.styskin.ca.math.Function;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

public class CriteriaXMLParser implements Constants {
	
	
	public static class Optimize {
		double[][] F;
		double[] base;
		
		private static NumberFormat FORMAT = NumberFormat.getNumberInstance();
		
		private static int getCriteriaSize(Criteria cr, Map<String, Integer> map, int begin) throws Exception {
			if(cr instanceof ComplexCriteria) {
				for(Criteria c : cr.getChildren()) {
					begin = getCriteriaSize(c, map, begin);
				}								
			} else {
				if(!map.containsKey(cr.getName())) {
					map.put(cr.getName(), begin);
				} else {
					throw new Exception("duplicate");
				}
				begin++;
			}
			return begin;			
		}
		
		private static void getBinaryCriteria(Criteria cr, List<String> list) throws Exception {
			if(cr instanceof ComplexCriteria) {
				if(((ComplexCriteria)cr).getOperator() instanceof BinaryOperator) {
					list.add(cr.getName());					
				} else {				
					for(Criteria c : cr.getChildren()) {
						getBinaryCriteria(c, list);
					}
				}
			}
		}
		
		
		private static Map<String, Integer> getCriteriaMap(Criteria cr) throws Exception {
			Map<String, Integer> map = new HashMap<String, Integer>();
			getCriteriaSize(cr, map, 0);
			return map;			
		}
		
		private static List<String> getBinaryMap(Criteria cr) throws Exception {
			List<String> list = new ArrayList<String>();
			getBinaryCriteria(cr, list);
			return list;			
		}		
		
		public static Optimize getInput(String file, Criteria cr) throws Exception {
			Optimize o = new Optimize();
			Map<String, Integer> map = getCriteriaMap(cr);
			map.put("price", -1);
			List<Integer> in_map = new ArrayList<Integer>();
			
			List<List<Double>> input = new ArrayList<List<Double>>();				
			BufferedReader in = new BufferedReader(new FileReader(file));
			StringTokenizer st;
			
			st = new StringTokenizer(in.readLine());
			while(st.hasMoreTokens()) {
				String key = st.nextToken(); 
				if(map.containsKey(key)) {
					in_map.add(map.get(key));						
				}
			}				
			String s = null;
			while((s = in.readLine()) != null) {
				st = new StringTokenizer(s);
				List<Double> d = new ArrayList<Double>();
				input.add(d);
				while(st.hasMoreTokens()) {
					d.add(FORMAT.parse(st.nextToken()).doubleValue());
				}
				if(d.size() != in_map.size()) {
					throw new Exception("Incorrect parameters number");						
				}
			}
			o.F = new double[input.size()][map.size()-1];
			o.base = new double[input.size()];				
			for(int j=0; j < input.size(); j++) {
				List<Double> d = input.get(j);
				for(int i=0; i < d.size(); i++) {
					if(in_map.get(i) < 0) {
						o.base[j] = d.get(i);
					} else {
						o.F[j][in_map.get(i)] = d.get(i);
					}
				}
			}				
			return o;
		}
		
		public static Optimize getInput(DataSource dataSource, final String tableName, Criteria cr) throws Exception {
			JdbcTemplate template = new JdbcTemplate(dataSource);			
			final Optimize o = new Optimize();
			final Map<String, Integer> map = getCriteriaMap(cr);
			final List<String> binaryCriteria = getBinaryMap(cr);
			map.put("price", -1);
			int inputSize = template.queryForInt("select count(1) from " + tableName);
			o.F = new double[inputSize][map.size()-1];
			o.base = new double[inputSize];
			template.execute(new ConnectionCallback() {
				public Object doInConnection(Connection connection) throws SQLException, DataAccessException {
					ResultSet rs = connection.createStatement().executeQuery("select * from " + tableName);
					int index = 0;
					while(rs.next()) {
						Arrays.fill(o.F[index], 0);						
						for(String binary : binaryCriteria) {
							o.F[index][map.get(rs.getString(binary))] = 1;				
						}
						for(String name : map.keySet()) {
							if(map.get(name) < 0) {
								o.base[index] = rs.getDouble(name);
							} else {
								try {
									o.F[index][map.get(name)] = rs.getDouble(name);
								} catch (Exception e) {}
							}
						}
						index ++;
					}					
					return null;
				}
			});	
			return o;
		}
		

		public double[] getBase() {
			return base;
		}

		public double[][] getF() {
			return F;
		}
	}

	static class CriteriaXMLHandler extends DefaultHandler {

		private Criteria criteria = null;

		private Stack<Criteria> stack = new Stack<Criteria>();

		boolean tag = false;
	    int level = 0;
	    
	    Function function = null;
	    
	    public CriteriaXMLHandler() {
	    	super();	    	
	    }

	    public CriteriaXMLHandler(Function f) {
	    	super();
	    	function = f;
	    }	    

	    public void startDocument() {}


	    public void endDocument() {
//	    	criteria.setMatrix();
	    }


	    public void startElement(String uri, String name, String qName, Attributes atts) {
	    	Criteria newCriteria = null;
	    	if("single".equals(atts.getValue("type"))) {
	    		try {
	    			if (atts.getValue("class") == null) {
	    				newCriteria = new SingleCriteria(new SingleOperator());
	    			} else {
	    				newCriteria = new SingleCriteria( SingleFunction.getSingleOperator(atts.getValue("class"), atts));
	    			}
				} catch (Exception e) {
					e.printStackTrace();
				}
	    	} else {
	    		try {
		    		Map<String, Double> lambda = new HashMap<String, Double>();
		    		for(int i=0; i < atts.getLength(); i++) {
		    			if (atts.getQName(i).startsWith("l")) {
		    				double value = FORMAT.parse(atts.getValue(i)).doubleValue();	    				
		    				lambda.put(atts.getQName(i), value);	    				
		    			}
		    		}
		    		if(level == 0 && function != null) {
		    			newCriteria = new IntegralCriteria(ComplexFunction.createOperator(atts.getValue("class")), function);
		    		} else {
		    			newCriteria = new ComplexCriteria(ComplexFunction.createOperator(atts.getValue("class")));
		    		}
					((ComplexCriteria) newCriteria).getOperator().load(lambda);
				} catch (Exception e) {
					e.printStackTrace();
				}
	    	}
	    	if(level == 0) {
	    		criteria = newCriteria;
		    	stack.push(newCriteria);
	    	} else {
	    		ComplexCriteria node = (ComplexCriteria) stack.peek();
	    		double weight = Double.parseDouble(atts.getValue("weight"));
	    		node.addChild(newCriteria, weight);
		    	stack.push(newCriteria);
	    	}
    		if(atts.getValue("name") != null) {
    			String crName = atts.getValue("name");
    			newCriteria.setName(crName);
    		}

	        tag = true;
	        level++;
	    }


	    public void endElement(String uri, String name, String qName) {
	    	tag = false;
	    	level --;
	    	Criteria criteria = stack.pop();
	    	if (criteria instanceof ComplexCriteria) {
	    		((ComplexCriteria)criteria).getOperator().refresh();
	    	}
	    }

	    public void characters(char ch[], int start, int length) {
	    }
	    
		public Criteria getCriteria() {
			return criteria;
		}
	}

	public static Criteria loadXML(File file) throws Exception {
        XMLReader xmlReader = XMLReaderFactory.createXMLReader();
        // TODO: Function in XML embeded         
        CriteriaXMLHandler handler = new CriteriaXMLHandler(new Function() {
			public double getValue(double x) {
				return 30000+5000000*x;
			}
        });
        xmlReader.setContentHandler(handler);
        xmlReader.setErrorHandler(handler);

        FileReader r = new FileReader(file);
        xmlReader.parse(new InputSource(r));

        return handler.getCriteria();
	}


	public static Criteria loadXML(String fileName) throws Exception {
		return loadXML(new File(fileName));
	}

	private static void saveNode(Criteria criteria, double weight, StringBuffer sb, PrintWriter out) throws Exception {
		if (criteria instanceof ComplexCriteria) {
			ComplexCriteria node = (ComplexCriteria) criteria;
			Map<String, Double> lambda = node.getOperator().save();
			StringBuffer lambdaSB = new StringBuffer();
			for(String name : lambda.keySet()) {
				lambdaSB.append(name).append("=\"").append(FORMAT.format(lambda.get(name))).append("\" ");
			}
			out.printf("%s<criteria name=\"%s\" type=\"complex\" class=\"%s\" weight=\"%s\" %s >\n",
//					sb, criteria.getName(), ComplexFunction.getOperatorName(node.getOperator().getClass()) , FORMAT.format(weight), lambdaSB.toString());
					sb, criteria.getName(), node.getOperator().operatorType(), FORMAT.format(weight), lambdaSB.toString());
			int i = 0;
			sb.append('\t');
			for(Criteria cr : node.getChildren()) {
				saveNode(cr, node.getOperator().getWeights().get(i++), sb, out);
			}
			sb.delete(sb.length()-1, sb.length());
			out.printf("%s</criteria>\n", sb);
		} else if (criteria instanceof SingleCriteria) {
			SingleCriteria node = (SingleCriteria) criteria;
			SingleOperator op = node.getOperator();
			StringBuffer params = new StringBuffer();
			Method[] methods = op.getClass().getMethods();
			for(Method method : methods) {
				String name = method.getName();
				if (name.startsWith("get") && method.getParameterTypes().length == 0 && method.getReturnType() == double.class) {
					params.append(Character.toLowerCase(name.charAt(3))).append(name.substring(4)).append("=\"").append(method.invoke(op)).append("\" ");
				}
			}
			out.printf("%s<criteria name=\"%s\" type=\"single\" class=\"%s\" weight=\"%s\" %s/>\n",
					sb, criteria.getName(), SingleFunction.getOperatorName(node.getOperator().getClass()),FORMAT.format(weight), params);
		}
	}

	public static void saveXML(Criteria criteria, File file) throws Exception {
		PrintWriter out = new PrintWriter(file);
		out.println("<?xml version=\"1.0\" encoding=\"windows-1251\"?>\n");
		saveNode(criteria, 1, new StringBuffer() ,out);
		out.close();
	}

	public static void saveXML(Criteria criteria, String fileName) throws Exception {
		saveXML(criteria, new File(fileName));
	}

}
