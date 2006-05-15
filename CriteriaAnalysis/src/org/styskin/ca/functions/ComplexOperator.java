package org.styskin.ca.functions;

import java.util.ArrayList;
import java.util.List;

public abstract class ComplexOperator extends Operator {

	// TODO public -> protected
	public double lambda;

	protected List<Double> weights;

	void normalize() {
		double sum = 0;
		for(double x : weights) {
			sum += x;
		}
		for(int i = 0; i < weights.size(); i++) {
			weights.set(i, weights.get(i)/sum);
		}
	}

	public void refresh() {
		normalize();
	}

	public void addCriteria(double weight) {
		weights.add(weight);
	}

	public ComplexOperator(double L, List<Double> weights)
		throws Exception {
		double w = 0;
		lambda = L;
		this.weights = weights;
		normalize();
	}

	public ComplexOperator(List<Double> weights)
		throws Exception {
		this(0.5, weights);
	}

	public ComplexOperator(double L) throws Exception {
		lambda = L;
		weights = new ArrayList<Double>();
	}

	public ComplexOperator() throws Exception {
		this(0.5);
	}

	public int getSize() {
		return weights.size();
	}

	protected String operatorType() {
		return "";
	}

}