package org.styskin.ca;

import org.styskin.ca.functions.CacheCriteria;
import org.styskin.ca.functions.Criteria;
import org.styskin.ca.functions.Optimizer;
import org.styskin.ca.model.CriteriaXMLLoader;

public class Main {


	private double[][] getMatrix(int size) {
		double[] CASES = {0.2, 0.5, 0.8};
		int VAR_NUMBER = size;
		int CASE_NUMBER = (int) Math.round(Math.pow(CASES.length, VAR_NUMBER));

		double[][] F = new double[CASE_NUMBER][VAR_NUMBER];
		for(int i=0; i < CASE_NUMBER; i++) {
			int mod = CASE_NUMBER/3;
			for(int j=0; j < VAR_NUMBER; j++) {
				F[i][j] = CASES[(i / mod) %3];
				mod /= 3;
			}
		}
		return F;
	}


	public void testCriteria() throws Exception {
		Criteria criteria = CriteriaXMLLoader.loadXML("cfg/criteria.xml");
		Criteria criteria2 = CriteriaXMLLoader.loadXML("cfg/criteria2.xml");

		double[][] F = getMatrix(criteria.getTotalSize());
		CacheCriteria cr = new CacheCriteria(criteria2, criteria, F);
/*		cr.refreshCache();
		cr.turnOffCache(((ComplexCriteria)criteria2).children.get(0));
		((ComplexCriteria)((ComplexCriteria)criteria2).children.get(0)).operator.lambda += 0.1;
		((ComplexCriteria)((ComplexCriteria)criteria2).children.get(0)).operator.refresh();
		System.out.println(cr.check());

		double d = 0 , t1 , t2;
		for(int i=0; i < F.length; i++) {
			t1 = criteria.getValues(F[i]);
			t2 = criteria2.getValues(F[i]);
			d += (t1 - t2)*(t1 - t2);
		}
		d = Math.sqrt(d);
		System.out.println(d);*/


		Optimizer optimizer = new Optimizer();
		optimizer.optimize(criteria2, criteria, F);

		cr.clearCache();
		System.out.printf("%4.4f - %s\n", cr.check(), criteria2);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		(new Main()).testCriteria();
	}

}
