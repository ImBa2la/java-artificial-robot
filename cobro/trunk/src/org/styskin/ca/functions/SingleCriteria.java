/*
 *$Id$
 */

package org.styskin.ca.functions;

import java.util.ArrayList;
import java.util.List;

import org.styskin.ca.functions.single.SingleOperator;


public class SingleCriteria extends Criteria {

	private SingleOperator operator;
	private static List<Criteria> emptyList = new ArrayList<Criteria>();

	public SingleCriteria(SingleOperator operator) {
		this.operator = operator;
	}

	@Override
	public List<Criteria> getChildren() {
		return emptyList;
	}

	@Override
	protected double getValue(double[] X, int start, int end) throws Exception {
		if (start != end || start >= X.length) {
			throw new Exception("Incorrect");
		}
		double[] Y = {X[start]};
		return operator.getValue(Y);
	}

	@Override
	protected int getSize() {
		return 1;
	}

	@Override
	public int getTotalSize() {
		return 1;
	}

	public SingleOperator getOperator() {
		return operator;
	}

	private void setOperator(SingleOperator operator) {
		this.operator = operator;
	}

	public Criteria clone() throws CloneNotSupportedException {
		SingleCriteria criteria = (SingleCriteria) super.clone();
		criteria.setOperator((SingleOperator) operator.clone());
		return criteria;
	}

/*	@Override
	public String toString() {
		return "x";
	}*/
}
