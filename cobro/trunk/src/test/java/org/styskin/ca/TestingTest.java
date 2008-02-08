package org.styskin.ca;

import java.io.File;

import org.apache.log4j.Logger;
import org.styskin.ca.functions.Criteria;
import org.styskin.ca.model.CriteriaXMLParser.Optimize;

import ru.yandex.utils.spring.SpringContextTestCase;

public class TestingTest extends SpringContextTestCase {
	
	private static final Logger logger = Logger.getLogger(AutoTest.class);
	
	private String namespace = "results/test/";	
	private Criteria test;
	private Criteria testBase;
	
	public void testAuto() throws Exception {
		prepareDirs();		
		logger.info("Optimization started");
		double[][] F = Optimize.getMatrix(test.getTotalSize(), 500);
		double[] B = new double[F.length];
		for(int i=0; i < B.length; i ++) {
			B[i] = testBase.getValues(F[i]); // + Math.random()/
			B[i] = B[i] > 1 ? 1 : B[i];
			B[i] = B[i] < 0 ? 0 : B[i];
		}		
		
		
		
		Optimize op = Optimize.getInput("", test);
		
/*		Optimize op = Optimize.getInput(autoDataSource, "car_v", auto);
		CacheCriteria c = new CacheCriteria(auto, op.getBase(), op.getF());
		CacheCriteria.outputValues(namespace + "out0.txt", op.getBase());		
		c.checkOut2(namespace + "out1.txt");		
		Optimizer optimizer = new MultiOptimizer(auto);
//		Optimizer optimizer = new SingleOptimizer(auto);
		auto = optimizer.optimize(op.getBase(), op.getF());
		CriteriaXMLParser.saveXML(auto, namespace + "auto.out.xml");
		c = new CacheCriteria(auto, op.getBase(), op.getF());
		c.checkOut2(namespace + "out2.txt");		
		Writer out = new FileWriter(namespace + "auto.out.txt");
		ValueLogger.output(out);
		out.close();*/
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
