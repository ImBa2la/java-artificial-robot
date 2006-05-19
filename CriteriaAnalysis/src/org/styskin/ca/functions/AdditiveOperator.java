package org.styskin.ca.functions;

import java.util.List;

public class AdditiveOperator extends ComplexOperator {
	
	public AdditiveOperator(double L, List<Double> children)
		throws Exception {
		super(L, children);
	}

	public AdditiveOperator(List<Double> children) throws Exception {
		super(children);
	}

	public AdditiveOperator(double L) throws Exception {
		super(L);
	}

	public AdditiveOperator() throws Exception {
		super();
	}

	@Override
	protected String operatorType() {
		return "+";
	}

	@Override
	public double getValue(double[] X) throws Exception {
		assert(X.length != weights.size());		
		
		double result = 0;
		for(int i = 0; i < weights.size(); i++) {
			result += weights.get(i) * X[i];
		}
		return result;
	}
}
