package org.styskin.ca;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;

import org.apache.log4j.Logger;
import org.styskin.ca.functions.CacheCriteria;
import org.styskin.ca.functions.Criteria;
import org.styskin.ca.functions.MultiOptimizer;
import org.styskin.ca.model.CriteriaXMLParser;
import org.styskin.ca.model.CriteriaXMLParser.Optimize;

import ru.yandex.utils.spring.SpringContextTestCase;

public class CarsTest extends SpringContextTestCase {
	
	private static final Logger logger = Logger.getLogger(CarsTest.class);
	
	private String namespace = "results/cars/";	
	private Criteria cars;
	
	public void testCars() throws Exception {
		(new File(namespace)).mkdirs();		
		logger.info("Optimization started");		
		Optimize op = Optimize.getInput("bin/cars/input.txt", cars);
		
		CacheCriteria c = new CacheCriteria(cars, op);
		c.checkOut2(namespace + "out1.txt");		
		MultiOptimizer optimizer = new MultiOptimizer(cars);
//		Optimizer optimizer = new SingleOptimizer(cars);
		cars = optimizer.optimize(op);
		CriteriaXMLParser.saveXML(cars, namespace + "auto.out.xml");
		c = new CacheCriteria(cars, op);
		c.checkOut2(namespace + "out1.txt");		
		Writer out = new FileWriter(namespace + "auto.out.txt");
		out.close();
		logger.info("Optimization finished");
	}

	public void setCars(Criteria cars) {
		this.cars = cars;
	}

	@Override
	protected String[] getConfigLocations() {
		return new String[] {"cars.xml"};
	}	
	

}
