package org.styskin.ca.functions;

import java.util.List;

import org.styskin.ca.model.Constants;

public abstract class Criteria implements Cloneable, Constants {

	private String name;

	public double getValues(double... X) throws Exception {
		return getValue(X, 0, X.length - 1);
	}

	abstract protected double getValue(double[] X, int start, int end) throws Exception;

	abstract protected int getSize();

	abstract public int getTotalSize();

	abstract public List<Criteria> getChildren();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return name;
	}

	public Criteria clone() throws CloneNotSupportedException {
		Criteria criteria = (Criteria) super.clone();
		criteria.setName(name);
		return criteria;
	}
}
