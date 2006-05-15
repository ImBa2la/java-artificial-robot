package org.styskin.ca.functions;


public class SingleCriteria extends Criteria {

//	private SingleOperator operator = new SingleOperator();

	@Override
	protected double getValue(double[] X, int start, int end) throws Exception {
		if (start != end || start >= X.length) {
			throw new Exception("Incorrect");
		}
		return X[start];
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
