package org.styskin.ca;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.styskin.ca.functions.CacheCriteria;
import org.styskin.ca.functions.Criteria;
import org.styskin.ca.functions.MultiOptimizer;
import org.styskin.ca.functions.Optimizer;
import org.styskin.ca.model.CriteriaXMLParser;
import org.styskin.ca.model.ValueLogger;
import org.styskin.ca.model.CriteriaXMLParser.Optimize;

import ru.yandex.utils.spring.SpringContextTestCase;

public class AutoTest extends SpringContextTestCase {
	
	private static final Logger logger = Logger.getLogger(AutoTest.class);
	
	private String namespace = "results/auto/";	
	private Criteria auto;
	private DataSource autoDataSource;
	
	public void testAuto() throws Exception {
		prepareDirs();		
		logger.info("Optimization started");		
		Optimize op = Optimize.getInput(autoDataSource, "car_v", auto);
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
		out.close();
		logger.info("Optimization finished");
	}

	private void prepareDirs() {
		File f = new File(namespace);
		f.mkdirs();
	}

	public void setAuto(Criteria auto) {
		this.auto = auto;
	}
	
	public void setAutoDataSource(DataSource autoDataSource) {
		this.autoDataSource = autoDataSource;
	}

	@Override
	protected String[] getConfigLocations() {
		return new String[] {"auto.xml"};
	}	
	

}