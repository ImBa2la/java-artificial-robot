package org.styskin.ca.functions.complex;

import java.util.ArrayList;
import java.util.List;

import org.styskin.ca.functions.Operator;

public abstract class ComplexOperator extends Operator implements Cloneable {

	public static List<Class> complexOperators = new ArrayList<Class>();

	static {
		complexOperators.add(AdditiveOperator.class);
		complexOperators.add(MultiplicativeOperator.class);
		complexOperators.add(PowerDoubleOperator.class);
		complexOperators.add(PowerIIOperator.class);
		complexOperators.add(PowerIOperator.class);
	}

	public double lambda;

	public List<Double> weights;

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

	public String operatorType() {
		return "";
	}

	@Override
	public ComplexOperator clone() throws CloneNotSupportedException {
		ComplexOperator op = (ComplexOperator) super.clone();
		op.weights = new ArrayList<Double>();
		for(double x : this.weights) {
			op.weights.add(x);
		}
		return op;
	}

}
