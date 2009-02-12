/*
 *$Id$
 */
package org.styskin.ca.functions.complex;

import java.util.ArrayList;
import java.util.List;

import org.styskin.ca.functions.Operator;
import org.styskin.ca.math.Function;
import org.styskin.ca.model.Slice;

public abstract class ComplexOperator implements Operator, Cloneable {

	public static List<Class<? extends ComplexOperator>> complexOperators = new ArrayList<Class<? extends ComplexOperator>>();

	static {
		complexOperators.add(PowerHOperator.class);
		complexOperators.add(ExponentalHOperator.class);
		complexOperators.add(PowerIOperator.class);
	}

	List<Double> weights;

	public void normalize() {
		double sum = 0;
		for(double x : weights) {
			sum += x > 0? x : 0;
		}
		for(int i = 0; i < weights.size(); i++) {
			weights.set(i, weights.get(i) > 0? weights.get(i)/sum: 0);
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
		normalize();
	}

	@Override
	public ComplexOperator clone() throws CloneNotSupportedException {
		ComplexOperator op = (ComplexOperator) super.clone();
		op.weights = new ArrayList<Double>(weights);
		return op;
	}
		
	public ComplexOperator cloneEquals() throws Exception {
		ComplexOperator op = getClass().newInstance();
		op.weights = new ArrayList<Double>(weights.size());
		for(int i=0; i < weights.size(); i++) {
			op.weights.add(1d);
		}
		op.normalize();
		return op;
	}	

	public List<Double> getWeights() {
		return weights;
	}
	
	public void setWeight(int index, double w) {
		weights.set(index, w > EPS ? w: EPS);
	}
	
	public void setWeights(List<Double> weights) {
		this.weights = weights;
		normalize();
	}
	
	public abstract double getPhi(double x);

	public abstract double getKsi(double x);
	
	private class PhiFunction implements Function {
		public double getValue(double x) {
			return getPhi(x);
		}
	}
	
	private PhiFunction phiFunction = new PhiFunction();
	
	private class KsiFunction implements Function {
		public double getValue(double x) {
			return getKsi(x);
		}
	}
	
	private KsiFunction ksiFunction =  new KsiFunction();
	
	public PhiFunction getPhiFunction() {
		return phiFunction;
	}

	public KsiFunction getKsiFunction() {
		return ksiFunction;
	}

	@Override
	public String toString() {
		return operatorType();				
	}
	
	public abstract Slice getParameters();
}
