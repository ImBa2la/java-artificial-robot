package org.styskin.ca.functions;

import java.util.List;

public class PowerDoubleOperator extends PowerOperator {

	public PowerDoubleOperator(double L) throws Exception {
		super(L);
	}

	public PowerDoubleOperator() throws Exception {
		super();
	}

	public PowerDoubleOperator(double L, List<Double> children) throws Exception {
		super(L, children);
	}

	public PowerDoubleOperator(List<Double> children) throws Exception {
		super(children);
	}

	@Override
	public double getValue(double[] X) throws Exception {
		double result = 0;
		int i = 0;
		for(double weight : weights) {
			result += weight * Math.pow(X[i++], FPower);
		}
		return Math.pow(result, 1/FPower);
	}

	@Override
	protected String operatorType() {
		return "**D";
	}
}
