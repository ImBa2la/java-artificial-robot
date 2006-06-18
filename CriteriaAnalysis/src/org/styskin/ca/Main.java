package org.styskin.ca;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Scanner;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.styskin.ca.functions.CacheCriteria;
import org.styskin.ca.functions.Criteria;
import org.styskin.ca.functions.Optimizer;
import org.styskin.ca.model.CriteriaXMLParser;

public class Main {

	static {
		BasicConfigurator.configure();
	}

	static Logger logger = Logger.getLogger(Main.class);

	public void testCriteria() throws Exception {
		Criteria criteria0 = CriteriaXMLParser.loadXML("cfg/criteria.xml");
		Criteria criteria1 = CriteriaXMLParser.loadXML("cfg/criteria2.xml");
		Criteria criteria2 = CriteriaXMLParser.loadXML("cfg/test2.xml");

		double[][] F = Optimizer.getMatrix(criteria0.getTotalSize(), 300);

		PrintWriter outF = new PrintWriter("F.txt");
		for(int i = 0; i < F.length; i++) {
			for(int j = 0; j < F[i].length; j++) {
				outF.printf("%f ", F[i][j]);
			}
			outF.println();
		}
		outF.close();


/*		double[][] F = new double[300][criteria0.getTotalSize()];
		Scanner inF = new Scanner(new File("F.txt"));
		for(int i = 0; i < F.length; i++) {
			for(int j = 0; j < F[i].length; j++) {
				F[i][j] = inF.nextDouble();
			}
		}
		inF.close();*/


/*		logger.debug(cr1.check());
		logger.debug(criteria2);

		Optimizer optimizer = new Optimizer();
		optimizer.optimize(criteria2, criteria, F);

		cr.clearCache();
		logger.debug("" + cr.check() + criteria2);*/
		Optimizer optimizer = new Optimizer();
		System.setOut(new PrintStream(new FileOutputStream("outObxod3_2.txt")));
		optimizer.optimize(criteria1, criteria0, F);
		System.out.close();
/*		System.setOut(new PrintStream(new FileOutputStream("out4.txt")));
		optimizer.optimize(criteria2, criteria0, F);
		System.out.close();*/
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
		(new Main()).test();
	}


	public void test() throws Exception {
		Criteria criteria0 = CriteriaXMLParser.loadXML("cfg/criteria.xml");
		Criteria criteria1 = CriteriaXMLParser.loadXML("tree1.xml");
		Criteria criteria2 = CriteriaXMLParser.loadXML("tree2.xml");

		double[][] F = new double[300][criteria0.getTotalSize()];
		Scanner inF = new Scanner(new File("F.txt"));
		for(int i = 0; i < F.length; i++) {
			for(int j = 0; j < F[i].length; j++) {
				F[i][j] = inF.nextDouble();
			}
		}
		inF.close();
		CacheCriteria cache1 = new CacheCriteria(criteria1, criteria0, F);
		CacheCriteria cache2 = new CacheCriteria(criteria2, criteria0, F);

		System.out.printf("1 = %4.4f;\n2 = %4.4f", cache1.check(), cache2.check());
	}

}
