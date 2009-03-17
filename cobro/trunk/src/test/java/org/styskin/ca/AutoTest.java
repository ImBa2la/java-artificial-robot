package org.styskin.ca;

import java.io.File;

import org.apache.log4j.Logger;
import org.styskin.ca.functions.CacheCriteria;
import org.styskin.ca.functions.Criteria;
import org.styskin.ca.functions.MultiOptimizer;
import org.styskin.ca.functions.Optimizer;
import org.styskin.ca.functions.SingleOptimizer;
import org.styskin.ca.model.CriteriaXMLParser;
import org.styskin.ca.model.Pair;
import org.styskin.ca.model.CriteriaXMLParser.Optimize;

import ru.yandex.utils.spring.SpringContextTestCase;

public class AutoTest extends SpringContextTestCase {
	
	private static final Logger logger = Logger.getLogger(AutoTest.class);
	
	private String namespace = "results/auto2/";	
	private Criteria auto;
	
	public void testAuto() throws Exception {
		prepareDirs();		
		logger.info("Optimization started");		
//		Optimize op = Optimize.getInput(autoDataSource, "car_vc" + " limit 660, 165", auto);
		Pair<Optimize, Optimize> opm = Optimize.getInput("cfg/auto/cars.txt", auto, 1);
		Optimize op = opm.getFirst();
		CacheCriteria c = new CacheCriteria(auto, op);
//		Optimizer optimizer = new MultiOptimizer(auto);
		Optimizer optimizer = new SingleOptimizer(auto);
		auto = optimizer.optimize(op);
		CriteriaXMLParser.saveXML(auto, namespace + "auto.out.xml");
		c = new CacheCriteria(auto, op);
//		CacheCriteria cross = new CacheCriteria(auto, opm.getSecond());
		logger.info(c.check());
		logger.info(c.checkCorrelation());
//		logger.info(cross.check());
		logger.info("Optimization finished");
	}

	private void prepareDirs() {
		File f = new File(namespace);
		f.mkdirs();
	}

	public void setAuto(Criteria auto) {
		this.auto = auto;
	}
	
	@Override
	protected String[] getConfigLocations() {
		return new String[] {"auto.xml"};
	}	
	

}
