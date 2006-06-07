package org.styskin.ca.functions;

import org.styskin.ca.functions.single.SingleOperator;


public class SingleCriteria extends Criteria {

	private SingleOperator operator;

	public SingleCriteria(SingleOperator operator) {
		this.operator = operator;
	}

	@Override
	protected double getValue(double[] X, int start, int end) throws Exception {
		if (start != end || start >= X.length) {
			throw new Exception("Incorrect");
		}
		double[] Y = {X[start]};
		return operator.getValue(Y);
//		return X[start];
	}

	@Override
	protected int getSize() {
		return 1;
	}

	@Override
	public int getTotalSize() {
		return 1;
	}

	@Override
	public String toString() {
		return "x";
	}
}
