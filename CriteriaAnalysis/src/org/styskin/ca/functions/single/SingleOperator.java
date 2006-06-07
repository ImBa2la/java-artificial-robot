package org.styskin.ca.functions.single;

import org.styskin.ca.functions.Operator;

public class SingleOperator extends Operator {

	private double fMin, fMax;

	@Override
	public double getValue(double[] X) throws Exception {
		return X[0];
	}

	public double getFMax() {
		return fMax;
	}
	public double getFMin() {
		return fMin;
	}
	public void setFMax(double max) {
		fMax = max;
	}
	public void setFMin(double min) {
		fMin = min;
	}
}
