package org.styskin.ca.functions.complex;

import java.util.ArrayList;
import java.util.List;

import org.styskin.ca.functions.Operator;

public abstract class ComplexOperator extends Operator implements Cloneable {

	public static List<Class> complexOperators = new ArrayList<Class>();

	static {
/*		complexOperators.add(MultiplicativeOperator.class);
		complexOperators.add(PowerDoubleOperator.class);
		complexOperators.add(PowerIIOperator.class);
		complexOperators.add(PowerIOperator.class);*/
	}

//	double lf, lk;

	List<Double> weights;

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

	public int getSize() {
		return weights.size();
	}

	public String operatorType() {
		return "Undefined";
	}
	
	public ComplexOperator() throws Exception {
		weights = new ArrayList<Double>();
	}
	
	public ComplexOperator(List<Double> weights) throws Exception {
		this.weights = weights;		
	}

	@Override
	public ComplexOperator clone() throws CloneNotSupportedException {
		ComplexOperator op = (ComplexOperator) super.clone();
		op.weights = new ArrayList<Double>(weights);
		return op;
	}

	public List<Double> getWeights() {
		return weights;
	}
	
	public abstract double getPhi(double x);

	public abstract double getKsi(double x);	
}
