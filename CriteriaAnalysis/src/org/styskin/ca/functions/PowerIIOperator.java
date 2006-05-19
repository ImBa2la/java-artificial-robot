package org.styskin.ca.functions;

import java.util.List;

public class PowerIIOperator extends PowerOperator {
	
	public PowerIIOperator(double L) throws Exception {
		super(L);
	}

	public PowerIIOperator() throws Exception {
		super();
	}

	public PowerIIOperator(double L, List<Double> children) throws Exception {
		super(L, children);
	}

	public PowerIIOperator(List<Double> children) throws Exception {
		super(children);
	}

	@Override
	public double getValue(double[] X) throws Exception {
		assert(X.length != weights.size());		
		
		double result = 0;
		int i = 0;
		for(double w : weights) {
			result += w * X[i++];
		}
		return Math.pow(result, FPower);
	}

	@Override
	protected String operatorType() {
		return "**II";
	}
}
