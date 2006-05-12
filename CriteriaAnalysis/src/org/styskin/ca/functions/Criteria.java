package org.styskin.ca.functions;

public abstract class Criteria {

	public double getValues(double... X) throws Exception {
		return getValue(X, 0, X.length - 1);
	}

	abstract protected double getValue(double[] X, int start, int end) throws Exception;

	abstract protected int getSize();

	abstract public int getTotalSize();
}
