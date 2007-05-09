package org.styskin.ca.test;

import junit.framework.TestCase;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.styskin.ca.functions.Criteria;
import org.styskin.ca.functions.MultiOptimizer;
import org.styskin.ca.model.CriteriaXMLParser;
import org.styskin.ca.model.CriteriaXMLParser.Optimize;

public class Flat extends TestCase {
	
	private static final Logger logger = Logger.getLogger(Flat.class);	
	
	private Criteria flat;
	
	protected void setUp() throws Exception {
		BasicConfigurator.configure();
		flat = CriteriaXMLParser.loadXML("cfg/flat/kv.xml");
	}
	
	public void testFlats() throws Exception {
		logger.info("Optimization started");		
		Optimize op = Optimize.getInput("cfg/input.txt", flat);
/*		Optimizer optimizer = new Optimizer(flat);
		optimizer.optimize(op.getBase(), op.getF());*/
		MultiOptimizer optimizer = new MultiOptimizer(flat);
		flat = optimizer.optimize(op.getBase(), op.getF());
		CriteriaXMLParser.saveXML(flat, "flat.xml");		
		logger.info("Optimization finished");
	}

}
