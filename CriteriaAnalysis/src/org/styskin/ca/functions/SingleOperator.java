package org.styskin.ca.functions;

public class SingleOperator extends Operator {
	double x;

	public void setValue(double x) {
		this.x = x;
	}

	public void setValues(double... X)
		throws Exception {
		if(X.length != 1) {
			throw new Exception("X must be length 1");
		}
		x = X[0];
	}

	@Override
	public double getValue() {
		return x;
	}

}
