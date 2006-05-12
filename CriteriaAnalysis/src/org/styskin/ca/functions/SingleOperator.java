package org.styskin.ca.functions;

public class SingleOperator extends Operator {

	@Override
	public double getValue(double[] X) throws Exception {
		return X[0];
	}

}
