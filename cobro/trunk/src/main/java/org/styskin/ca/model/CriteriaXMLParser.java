/*
 *$Id$
 */
package org.styskin.ca.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;
import org.styskin.ca.functions.ComplexCriteria;
import org.styskin.ca.functions.Criteria;
import org.styskin.ca.functions.IntegralCriteria;
import org.styskin.ca.functions.LinearFunction;
import org.styskin.ca.functions.SaveLoadFunction;
import org.styskin.ca.functions.SingleCriteria;
import org.styskin.ca.functions.complex.BinaryOperator;
import org.styskin.ca.functions.single.SingleOperator;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

public class CriteriaXMLParser implements Constants {
	
	private static final Logger logger = Logger.getLogger(CriteriaXMLParser.class);
	
	public static double getDouble(String s) throws Exception {
		return FORMAT.parse(s).doubleValue();
	}	
	
	private static int getCriteriaSize(Criteria cr, Map<String, List<Integer>> map) throws Exception {
		return getCriteriaSize(cr, map, 0);
	}
	
	private static int getCriteriaSize(Criteria cr, Map<String, List<Integer>> map, int begin) throws Exception {
		if(cr instanceof ComplexCriteria) {
			for(Criteria c : cr.getChildren()) {
				begin = getCriteriaSize(c, map, begin);
			}								
		} else {
			addToMap(map, cr.getName(), begin);
			++ begin;
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
	
	private static List<String> getBinaryMap(Criteria cr) throws Exception {
		List<String> list = new ArrayList<String>();
		getBinaryCriteria(cr, list);
		return list;			
	}
	
	private static void buildName(Criteria cr, StringBuilder sb, List<SingleOperator> ops) {
		if(cr instanceof ComplexCriteria) {
			for(Criteria c : cr.getChildren()) {
				buildName(c, sb, ops);										
			}
		} else {
			sb.append('\t').append(cr.getName());
			ops.add((SingleOperator) cr.getOperator());
		}
	}
	
	private static void addToMap(Map<String, List<Integer>> map, String key, int value) {
		List<Integer> list = map.get(key);
		if(list == null) {
			list = new ArrayList<Integer>();
			map.put(key, list);
		}
		list.add(value);
	}
	
	public static class OptimizeInputFormat {
		
		private Criteria criteria;		
		private Map<String, List<Integer>> positionMap = new HashMap<String, List<Integer>>();
		private List<String> binaryCriteria;
		private List<List<Integer>> invMap = new ArrayList<List<Integer>>();
		private int criteriaSize;		
		
		public OptimizeInputFormat(Criteria cr) throws Exception {
			criteria = cr;
			initCriteria();
		}
		
		private void initCriteria() throws Exception {
			criteriaSize = getCriteriaSize(criteria, positionMap);
			binaryCriteria = getBinaryMap(criteria);
			addToMap(positionMap, criteria.getName(), -1);
		}
		
		
		public void init(String head) {
			StringTokenizer st = new StringTokenizer(head);
			while(st.hasMoreTokens()) {
				String key = st.nextToken(); 
				key = prepareToken(key);
				if(positionMap.containsKey(key)) {
					invMap.add(positionMap.get(key));						
				} else {
					List<Integer> list = new ArrayList<Integer>();
					list.add(-2);
					invMap.add(list);
				}
			}
		}

		private static String prepareToken(String key) {
			if(key.startsWith("\"")) {
				key = key.substring(1, key.length()-1);
			}
			return key;
		}
		
		public Pair<Double, double[]> parseLine(String line) throws Exception {
			//XXX: be accurate with \t
			StringTokenizer st = new StringTokenizer(line, "\t");
			double res = -1;			
			double[] d = new double[criteriaSize];
			int i = 0;
			while(st.hasMoreTokens()) {
				String v = st.nextToken();
				v = prepareToken(v);
				if(positionMap.containsKey(v)) {
					// Binary property
					for(Integer index : positionMap.get(v))
						d[index] = 1;
				} else {
					for(Integer index : invMap.get(i)) {		
						if(index == -1) {
							res = getDouble(v);
						} else if(index >= 0) {
							d[index] = getDouble(v);
						}
					}
				}
				++ i;
			}
			return new Pair<Double, double[]>(res, d);
		}
		
	}
	
	public static class Optimize {
		double[][] F;
		double[] base;
		String[] lines;
		
		private Optimize() {}

		public Optimize(double[][] f, double[] base) {
			F = f;
			this.base = base;
		}

		public static void saveInput(String file, Criteria cr, double[][] F, double[] B) throws Exception {
			PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(file)));
			out.print(cr.getName());
			StringBuilder sb = new StringBuilder();
			List<SingleOperator> ops = new ArrayList<SingleOperator>();
			buildName(cr, sb, ops);
			out.println(sb.toString());
			for(int i=0; i < F.length; i++) {
				out.print(FORMAT.format(B[i]));
				for(int j=0; j < F[i].length; j++) {
					out.print('\t');
					out.print(FORMAT.format( ops.get(j).getValue(F[i][j])));
				}
				out.println();
			}
			out.close();
		}
		
		public static Optimize getInput(String file, Criteria cr) throws Exception {
			return getInput(file, cr, 1).getFirst();			
		}

		public static Optimize getInput(File file, Criteria cr) throws Exception {
			return getInput(file, cr, 1).getFirst();			
		}
			
		public static Pair<Optimize, Optimize> getInput(String file, Criteria cr, double ratio) throws Exception {
			return getInput(new File(file), cr, ratio);
		}
		
		@SuppressWarnings("unchecked")
		public static Pair<Optimize, Optimize> getInput(File file, Criteria cr, double ratio) throws Exception {
			int TWO = 2;			
			Optimize[] o = new Optimize[TWO];
			
			OptimizeInputFormat optimizeInputFormat = new OptimizeInputFormat(cr);
			
			List<Double>[] inputP = new List[TWO];				
			List<String>[] inputS = new List[TWO];				
			List<double[]>[] input = new List[TWO];
			for(int i=0; i < TWO; i++) {
				o[i] = new Optimize();
				input[i] = new ArrayList<double[]>();
				inputP[i] = new ArrayList<Double>();
				inputS[i] = new ArrayList<String>();
			}
			BufferedReader in = new BufferedReader(new FileReader(file));
			
			optimizeInputFormat.init(in.readLine());
			String s = null;
			while((s = in.readLine()) != null) {
				int ind = 0;
				if(Math.random() > ratio)
					ind = 1;
				
				Pair<Double, double[]> pair = optimizeInputFormat.parseLine(s);
				
				input[ind].add(pair.getSecond());
				inputP[ind].add(pair.getFirst());
				inputS[ind].add(s);
			}
			for(int i=0; i < TWO; i++) {
				int size = input[i].size();
				if(size > 0) {
					o[i].F = new double[size][input[i].get(0).length];
					o[i].base = new double[size];
					o[i].lines = new String[size];
					for(int j=0; j < input[i].size(); j++) {
						o[i].F[j] = input[i].get(j);
						o[i].base[j] = inputP[i].get(j);
						o[i].lines[j] = inputS[i].get(j);
					}
				}
			}
			return Pair.makePair(o[0], o[1]);
		}
		
/*		public static Optimize getInput(DataSource dataSource, final String tableName, Criteria cr) throws Exception {
			JdbcTemplate template = new JdbcTemplate(dataSource);			
			final Optimize o = new Optimize();
			final Map<String, Integer> map = getCriteriaMap(cr);
			final List<String> binaryCriteria = getBinaryMap(cr);
			map.put("price", -1);
//			int inputSize = template.queryForInt("select count(1) from " + tableName);
			int inputSize = template.queryForInt("select count(1) from (select * from " + tableName + " ) a");
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
		}*/
		
		public static double[][] getMatrix(int size, int length) {
			double[] CASES = {0.2, 0.6, 0.8};
			int VAR_NUMBER = size;
			long CASE_NUMBER = Math.round(Math.pow(CASES.length, VAR_NUMBER));
			long step = CASE_NUMBER / length - 10;
			double[][] F = new double[length][VAR_NUMBER];
			int iF = 0;
			for(long i=0; i < CASE_NUMBER; i+= step /* + Math.round(20*Math.random())*/) {
				if (iF >= length) {
					break;
				}
				long mod = (CASE_NUMBER/CASES.length);
				for(int j=0; j < VAR_NUMBER; j++) {
					F[iF][j] = CASES[(int)((i / mod) % CASES.length)];
					mod /= CASES.length;
				}
				iF++;
			}
			return F;
		}		

		public double[] getBase() {
			return base;
		}

		public double[][] getF() {
			return F;
		}

		public String[] getLines() {
			return lines;
		}
		
	}

	static class CriteriaXMLHandler extends DefaultHandler {

		private IntegralCriteria criteria = null;

		private Stack<Criteria> stack = new Stack<Criteria>();

		boolean tag = false;
	    int level = 0;
	    
	    SaveLoadFunction function = null;
	    
	    public CriteriaXMLHandler() {
	    	super();	    	
	    }

	    //XXX: remember about this
	    public CriteriaXMLHandler(SaveLoadFunction f) {
	    	super();
	    	function = f;
	    }	    

	    public void startDocument() {}


	    public void endDocument() {
//	    	criteria.setMatrix();
	    }


	    public void startElement(String uri, String name, String qName, Attributes atts) {
    		Map<String, Double> lambda = new HashMap<String, Double>();
    		try {
	    		for(int i=0; i < atts.getLength(); i++) {
	    			if (atts.getQName(i).startsWith("l")) {	    				
	    				double value = getDouble(atts.getValue(i));	    				
	    				lambda.put(atts.getQName(i), value);	    				
	    			}
	    		}
    		} catch (Exception e) {
    			logger.error("Cannot parse lambda", e);
			}
	    	Criteria newCriteria = null;
	    	if("single".equals(atts.getValue("type"))) {
	    		try {
	    			if (atts.getValue("class") == null) {
	    				newCriteria = new SingleCriteria(new SingleOperator());
	    			} else {
	    				newCriteria = new SingleCriteria( SingleFunction.getSingleOperator(atts.getValue("class")));
	    			}
	    			newCriteria.getOperator().loadParameters(lambda);
				} catch (Exception e) {
	    			logger.error("Cannot create SingleOperator", e);
				}
	    	} else {
	    		try {
		    		if(level == 0) {
		    			newCriteria = new IntegralCriteria(ComplexFunction.createOperator(atts.getValue("class")), function != null? function : new LinearFunction());
		    		} else {
		    			newCriteria = new ComplexCriteria(ComplexFunction.createOperator(atts.getValue("class")));
		    		}
					newCriteria.getOperator().loadParameters(lambda);
				} catch (Exception e) {
	    			logger.error("Cannot create ComplexOperator", e);
				}
	    	}
	    	if(level == 0) {
	    		criteria = (IntegralCriteria) newCriteria;
		    	stack.push(newCriteria);
	    	} else {
	    		ComplexCriteria node = (ComplexCriteria) stack.peek();
	    		double weight = 0;
	    		try {
	    			weight = getDouble(atts.getValue("weight"));
	    		} catch (Exception e) {
	    			logger.error("Cannot parse weight", e);
				}
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
	    
		public IntegralCriteria getCriteria() {
			return criteria;
		}
	}

	public static IntegralCriteria loadXML(File file) throws Exception {
        XMLReader xmlReader = XMLReaderFactory.createXMLReader();
        CriteriaXMLHandler handler = new CriteriaXMLHandler();
        xmlReader.setContentHandler(handler);
        xmlReader.setErrorHandler(handler);

        FileReader r = new FileReader(file);
        xmlReader.parse(new InputSource(r));

        return handler.getCriteria();
	}


	public static IntegralCriteria loadXML(String fileName) throws Exception {
		return loadXML(new File(fileName));
	}

	private static void saveNode(Criteria criteria, double weight, StringBuffer sb, PrintWriter out) throws Exception {
		if (criteria instanceof ComplexCriteria) {
			ComplexCriteria node = (ComplexCriteria) criteria;
			Map<String, Double> lambda = new HashMap<String, Double>(); 
			node.getOperator().saveParameters(lambda);
			StringBuffer lambdaSB = new StringBuffer();
			for(String name : lambda.keySet()) {
				lambdaSB.append(name).append("=\"").append(FORMAT.format(lambda.get(name))).append("\" ");
			}
			out.printf("%s<criteria name=\"%s\" type=\"complex\" class=\"%s\" weight=\"%s\" %s >\n",
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
