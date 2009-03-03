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

public class BankTest extends SpringContextTestCase {
	
	private static final Logger logger = Logger.getLogger(AutoTest.class);
	
	private String namespace = "results/bank/industrials/";	
	private Criteria bank;
	
	public void testBank() throws Exception {
		prepareDirs();		
		logger.info("Optimization started");		
		Optimize op = Optimize.getInput("src/main/cfg/bank/industrials.in", bank);
		CacheCriteria c = new CacheCriteria(bank, op);
		CacheCriteria.outputValues(namespace + "out0.txt", op.getBase());		
		c.checkOut2(namespace + "out1.txt");
		Optimizer optimizer = new MultiOptimizer(bank);
//		Optimizer optimizer = new SingleOptimizer(auto);
		bank = optimizer.optimize(op);
		CriteriaXMLParser.saveXML(bank, namespace + "bank.out.xml");
		c = new CacheCriteria(bank, op);
		c.checkOut2(namespace + "out2.txt");		
		logger.info("Optimization finished");
	}

	private void prepareDirs() {
		File f = new File(namespace);
		f.mkdirs();
	}

	

	public void setBank(Criteria bank) {
		this.bank = bank;
	}

	@Override
	protected String[] getConfigLocations() {
		return new String[] {"bank.xml"};
	}	
	

}
