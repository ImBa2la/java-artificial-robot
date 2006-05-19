package org.styskin.ca.functions;

import java.util.List;

public class PowerIOperator extends PowerOperator {

	public PowerIOperator(double L) throws Exception {
		super(L);
	}

	public PowerIOperator() throws Exception {
		super();
	}

	public PowerIOperator(double L, List<Double> children) throws Exception {
		super(L, children);
	}

	public PowerIOperator(List<Double> children) throws Exception {
		super(children);
	}

	@Override
	public double getValue(double[] X) throws Exception {
		assert(X.length != weights.size());		
		
		double result = 0;
		int i = 0;
		for(double w : weights) {
			result += w * Math.pow(X[i++], FPower);
		}
		return result;
	}

	@Override
	protected String operatorType() {
		return "**I";
	}
}
