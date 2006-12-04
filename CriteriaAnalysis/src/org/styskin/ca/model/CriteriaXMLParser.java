package org.styskin.ca.model;

import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import org.styskin.ca.functions.ComplexCriteria;
import org.styskin.ca.functions.Criteria;
import org.styskin.ca.functions.SingleCriteria;
import org.styskin.ca.functions.single.SingleOperator;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

public class CriteriaXMLParser implements Constants {

	static class CriteriaXMLHandler extends DefaultHandler {

		private Criteria criteria = null;

		private Stack<Criteria> stack = new Stack<Criteria>();

		boolean tag = false;
	    int level = 0;

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
					newCriteria = new ComplexCriteria(ComplexFunction.createOperator(atts.getValue("class")));
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
        CriteriaXMLHandler handler = new CriteriaXMLHandler();

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
					sb, criteria.getName(), ComplexFunction.getOperatorName(node.getOperator().getClass()) , FORMAT.format(weight), lambdaSB.toString());
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
