package org.styskin.ca;

import java.io.FileWriter;
import java.io.Writer;

import junit.framework.TestCase;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.styskin.ca.functions.CacheCriteria;
import org.styskin.ca.functions.Criteria;
import org.styskin.ca.functions.MultiOptimizer;
import org.styskin.ca.model.CriteriaXMLParser;
import org.styskin.ca.model.ValueLogger;
import org.styskin.ca.model.CriteriaXMLParser.Optimize;

public class FlatTest extends TestCase {
	
	private static final Logger logger = Logger.getLogger(FlatTest.class);	
	
	private Criteria flat;
	
	protected void setUp() throws Exception {
		BasicConfigurator.configure();
		flat = CriteriaXMLParser.loadXML("cfg/flat/kv.xml");
	}
	
	public void testFlats() throws Exception {
		logger.info("Optimization started");		
		Optimize op = Optimize.getInput("cfg/flat/input.txt", flat);
/*		Optimizer optimizer = new Optimizer(flat);
		optimizer.optimize(op.getBase(), op.getF());*/
		
		CacheCriteria c = new CacheCriteria(flat, op.getBase(), op.getF());
		c.checkOut2("out1.xml");
		
		
		MultiOptimizer optimizer = new MultiOptimizer(flat);
		flat = optimizer.optimize(op.getBase(), op.getF());
		CriteriaXMLParser.saveXML(flat, "flat.xml");
		
		c = new CacheCriteria(flat, op.getBase(), op.getF());
		c.checkOut2("out2.xml");		
		
		Writer out = new FileWriter("out.txt");
		ValueLogger.output(out);
		out.close();
		logger.info("Optimization finished");
	}

}
