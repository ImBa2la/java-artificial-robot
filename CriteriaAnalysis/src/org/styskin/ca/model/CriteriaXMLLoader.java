package org.styskin.ca.model;

import java.io.FileReader;
import java.util.Stack;

import org.styskin.ca.functions.ComplexCriteria;
import org.styskin.ca.functions.Criteria;
import org.styskin.ca.functions.SingleCriteria;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

public class CriteriaXMLLoader {

	static class CriteriaXMLHandler extends DefaultHandler {

		private Criteria criteria = null;

		private Stack<Criteria> stack = new Stack<Criteria>();

		boolean tag = false;
	    int level = 0;

	    public void startDocument() {}


	    public void endDocument() {
	    	criteria.setMatrix();
	    }


	    public void startElement(String uri, String name, String qName, Attributes atts) {
	    	Criteria newCriteria = null;
	    	if("single".equals(atts.getValue("type"))) {
	    		newCriteria = new SingleCriteria();
	    	} else {
	    		OperatorType operatorType = OperatorUtils.getOperatorTypeByName(atts.getValue("class"));
	    		double lambda = 0.5;
	    		if(atts.getValue("lambda") != null) {
	    			lambda = Double.valueOf(atts.getValue("lambda"));
	    		}
	    		try {
					newCriteria = new ComplexCriteria(operatorType, lambda);
				} catch (Exception e) {
					e.printStackTrace();
				}
	    	}
	    	if(level == 0) {
	    		criteria = newCriteria;
		    	stack.push(newCriteria);
	    	} else {
	    		ComplexCriteria node = (ComplexCriteria) stack.get(stack.size() - 1);
	    		double weight = Double.valueOf(atts.getValue("weight"));
	    		node.addChild(newCriteria, weight);
		    	stack.push(newCriteria);
	    	}
	        tag = true;
	        level++;
	    }


	    public void endElement(String uri, String name, String qName) {
	    	tag = false;
	    	level --;
	    	Criteria criteria = stack.pop();
    		criteria.refresh();
	    }


	    public void characters(char ch[], int start, int length) {
	    }


		public Criteria getCriteria() {
			return criteria;
		}
	}


	public static Criteria loadXML(String fileName) throws Exception {
        XMLReader xmlReader = XMLReaderFactory.createXMLReader();
        CriteriaXMLHandler handler = new CriteriaXMLHandler();

        xmlReader.setContentHandler(handler);
        xmlReader.setErrorHandler(handler);

        FileReader r = new FileReader(fileName);
        xmlReader.parse(new InputSource(r));

        return handler.getCriteria();
	}

}
