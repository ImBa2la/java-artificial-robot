package org.styskin.ca;

import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Arrays;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.styskin.ca.functions.Criteria;
import org.styskin.ca.functions.Optimizer;
import org.styskin.ca.model.CriteriaXMLParser;

public class Main {

	static {
		BasicConfigurator.configure();
	}

	static Logger logger = Logger.getLogger(Main.class);

	public void testCriteria() throws Exception {
		Criteria criteria0 = CriteriaXMLParser.loadXML("cfg/test0.xml");
		Criteria criteria1 = CriteriaXMLParser.loadXML("cfg/test1.xml");
		Criteria criteria2 = CriteriaXMLParser.loadXML("cfg/test2.xml");

		double[][] F = Optimizer.getMatrix(criteria0.getTotalSize(), 300);
/*		logger.debug(cr1.check());
		logger.debug(criteria2);

		Optimizer optimizer = new Optimizer();
		optimizer.optimize(criteria2, criteria, F);

		cr.clearCache();
		logger.debug("" + cr.check() + criteria2);*/
		Optimizer optimizer = new Optimizer();
/*		System.setOut(new PrintStream(new FileOutputStream("out1.txt")));
		optimizer.optimize(criteria1, criteria0, F);
		System.out.close();*/
		System.setOut(new PrintStream(new FileOutputStream("out4.txt")));
		optimizer.optimize(criteria2, criteria0, F);
		System.out.close();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
/*		Criteria criteria = CriteriaXMLParser.loadXML("cfg/criteria3.xml");
		double[][] F = {{1,1},{2,2},{3,3}};

		CacheCriteria cr = new CacheCriteria(criteria, criteria, F);
		for(double x : cr.getValue()) {
			logger.info(x);
		}*/
		(new Main()).testCriteria();
	}

}
