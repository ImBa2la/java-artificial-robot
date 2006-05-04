package org.styskin.ca;

import org.styskin.ca.functions.Criteria;
import org.styskin.ca.functions.Optimizer;
import org.styskin.ca.model.CriteriaXMLLoader;

public class Main {


	public void testCriteria() throws Exception {
		Criteria criteria = CriteriaXMLLoader.loadXML("cfg/criteria.xml");
		Criteria criteria2 = CriteriaXMLLoader.loadXML("cfg/criteria2.xml");
		criteria2.setBase(criteria);
		System.out.println(criteria2.check());

		Optimizer optimizer = new Optimizer();
		optimizer.optimize(criteria2);

		System.out.printf("%4.4f - %s\n", criteria2.check(), criteria2);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		// TEST COMMENT
		(new Main()).testCriteria();
	}

}
