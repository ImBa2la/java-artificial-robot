package org.styskin.ca.functions;

import java.text.NumberFormat;

public class SingleCriteria extends Criteria {

	private SingleOperator operator = new SingleOperator();

	public void setValue(double x) {
		operator.setValue(x);
	}

	@Override
	protected double getValue() {
		return operator.getValue();
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
	protected void setValues(double[] X, int start, int end) throws Exception {
		if(start != end) {
			throw new Exception("Incorrect");
		}
		this.setValue(X[start]);
	}

	@Override
	public String toString() {
		return NumberFormat.getInstance().format(operator.x);
	}
}
