package org.styskin.ca;

import org.styskin.ca.functions.CacheCriteria;
import org.styskin.ca.functions.Criteria;
import org.styskin.ca.functions.Optimizer;
import org.styskin.ca.model.CriteriaXMLLoader;

public class Main {


	public void testCriteria() throws Exception {
		Criteria criteria = CriteriaXMLLoader.loadXML("cfg/criteria.xml");
		Criteria criteria2 = CriteriaXMLLoader.loadXML("cfg/criteria2.xml");
		
		CacheCriteria cr = new CacheCriteria(criteria2);
		cr.setBase(criteria);
		cr.clearCache();
		System.out.println(cr.check());

		Optimizer optimizer = new Optimizer();
		optimizer.optimize(criteria2, criteria);

		cr.clearCache();		
		System.out.printf("%4.4f - %s\n", cr.check(), criteria2);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		// TEST COMMENT
		(new Main()).testCriteria();
	}

}
