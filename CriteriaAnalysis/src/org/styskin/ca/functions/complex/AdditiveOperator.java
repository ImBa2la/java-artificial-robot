package org.styskin.ca.functions.complex;

import java.util.List;


public class AdditiveOperator extends ComplexOperator {
	
	public AdditiveOperator() throws Exception {
		super();
	}	

	public AdditiveOperator(List<Double> children) throws Exception {
		super(children);
	}


	@Override
	public String operatorType() {
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

	@Override
	public double getKsi(double x) {
		return x;
	}

	@Override
	public double getPhi(double x) {
		return x;
	}
}
