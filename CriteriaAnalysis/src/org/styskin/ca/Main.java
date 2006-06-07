package org.styskin.ca;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.styskin.ca.functions.CacheCriteria;
import org.styskin.ca.functions.Criteria;
import org.styskin.ca.functions.Optimizer;
import org.styskin.ca.model.CriteriaXMLLoader;

public class Main {

	static {
		BasicConfigurator.configure();
	}

	static Logger logger = Logger.getLogger(Main.class);

	private double[][] getMatrix(int size, int length) {
		double[] CASES = {0.2, 0.5, 0.8};
		int VAR_NUMBER = size;
		long CASE_NUMBER = Math.round(Math.pow(CASES.length, VAR_NUMBER));

		long step = CASE_NUMBER / length - 10;
		double[][] F = new double[length][VAR_NUMBER];

		int iF = 0;
		for(long i=0; i < CASE_NUMBER; i+= step + Math.round(20*Math.random())) {
			if (iF >= length) {
				break;
			}
			long mod = (CASE_NUMBER/CASES.length);
			for(int j=0; j < VAR_NUMBER; j++) {
				F[iF][j] = CASES[(int)((i / mod) %3)];
				mod /= 3;
			}
			iF++;
		}
		return F;
	}


	public void testCriteria() throws Exception {
		Criteria criteria = CriteriaXMLLoader.loadXML("cfg/criteria.xml");
		Criteria criteria2 = CriteriaXMLLoader.loadXML("cfg/criteria2.xml");

		double[][] F = getMatrix(criteria.getTotalSize(), 300);
		CacheCriteria cr = new CacheCriteria(criteria2, criteria, F);

		logger.debug(cr.check());
		logger.debug(criteria2);

		Optimizer optimizer = new Optimizer();
		optimizer.optimize(criteria2, criteria, F);

		cr.clearCache();
		logger.debug("" + cr.check() + criteria2);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		(new Main()).testCriteria();
	}

}
