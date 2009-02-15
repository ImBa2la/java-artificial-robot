package org.styskin.ca;

import java.io.File;

import org.apache.log4j.Logger;
import org.styskin.ca.functions.CacheCriteria;
import org.styskin.ca.functions.Criteria;
import org.styskin.ca.functions.MultiOptimizer;
import org.styskin.ca.functions.Optimizer;
import org.styskin.ca.model.CriteriaXMLParser;
import org.styskin.ca.model.CriteriaXMLParser.Optimize;

import ru.yandex.utils.spring.SpringContextTestCase;

public class TestingTest extends SpringContextTestCase {
	
	private static final Logger logger = Logger.getLogger(AutoTest.class);
	
	private String namespace = "results/test/";	
	private Criteria test;
	private Criteria testBase;
	
	public void testTesting() throws Exception {
		prepareDirs();		
		logger.info("Optimization started");
		double[][] F = Optimize.getMatrix(testBase.getTotalSize(), 500);
		double[] B = new double[F.length];
		double[] BB = new double[F.length];
		for(int i=0; i < B.length; i ++) {
			BB[i] = testBase.getValues(F[i]);
			B[i] = testBase.getValues(F[i]);// + (Math.random()/2.5 - 0.2);
			B[i] = B[i] > 1 ? 1 : B[i];
			B[i] = B[i] < 0 ? 0 : B[i];
		}
		Optimize.saveInput(namespace + "test.in", testBase, F, B);		
		
		Optimize op = Optimize.getInput(namespace + "test.in", test);
	
		CacheCriteria c = new CacheCriteria(test, op.getBase(), op.getF());
		CacheCriteria.outputValues(namespace + "out0.txt", BB);		
		c.checkOut2(namespace + "out1.txt");		
		Optimizer optimizer = new MultiOptimizer(test);
		test = optimizer.optimize(op.getBase(), op.getF());
		CriteriaXMLParser.saveXML(test, namespace + "test.out.xml");
		c = new CacheCriteria(test, op.getBase(), op.getF());
		c.checkOut2(namespace + "out2.txt");		
		logger.info("Optimization finished");
	}

	private void prepareDirs() {
		File f = new File(namespace);
		f.mkdirs();
	}
	
	public void setTest(Criteria test) {
		this.test = test;
	}

	public void setTestBase(Criteria testBase) {
		this.testBase = testBase;
	}

	@Override
	protected String[] getConfigLocations() {
		return new String[] {"test.xml"};
	}	
	

}
