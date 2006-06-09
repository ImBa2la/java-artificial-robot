package org.styskin.ca.model;

import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;
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

public class CriteriaXMLParser {

	private static final NumberFormat NUMBER_FORMAT = DecimalFormat.getInstance(Locale.US);

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
	    		double lambda = 0.5;
	    		if(atts.getValue("lambda") != null) {
	    			lambda = Double.valueOf(atts.getValue("lambda"));
	    		}
	    		try {
					newCriteria = new ComplexCriteria(ComplexFunction.createOperator(atts.getValue("class"), lambda));
				} catch (Exception e) {
					e.printStackTrace();
				}
	    	}
	    	if(level == 0) {
	    		criteria = newCriteria;
		    	stack.push(newCriteria);
	    	} else {
	    		ComplexCriteria node = (ComplexCriteria) stack.peek();
	    		double weight = Double.valueOf(atts.getValue("weight"));
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
	    		((ComplexCriteria)criteria).operator.refresh();
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
			out.printf("%s<criteria name=\"%s\" type=\"complex\" class=\"%s\" lambda=\"%s\">\n",
					sb, criteria.getName(), ComplexFunction.getOperatorName(node.operator.getClass()) , NUMBER_FORMAT.format(node.operator.lambda));
			int i = 0;
			sb.append('\t');
			for(Criteria cr : node.getChildren()) {
				saveNode(cr, node.operator.weights.get(i++), sb, out);
			}
			out.printf("%s</criteria>\n", sb);
			sb.delete(sb.length()-1, sb.length());
		} else if (criteria instanceof SingleCriteria) {
			SingleCriteria node = (SingleCriteria) criteria;
			// TODO extra attributes parsing
			out.printf("%s<criteria name=\"%s\" type=\"single\" class=\"%s\"/>\n",
					sb, criteria.getName(), SingleFunction.getOperatorName(node.getOperator().getClass()));
		}
	}

	public static void saveXML(Criteria criteria, File file) throws Exception {
		PrintWriter out = new PrintWriter(file);
		saveNode(criteria, 1, new StringBuffer() ,out);
		out.close();
	}

	public static void saveXML(Criteria criteria, String fileName) throws Exception {
		saveXML(criteria, new File(fileName));
	}

}