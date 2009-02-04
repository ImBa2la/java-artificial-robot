/*
 *$Id$
 */
package org.styskin.ca.functions.complex;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.styskin.ca.functions.Operator;
import org.styskin.ca.functions.SaveLoadParameters;
import org.styskin.ca.model.Slice;

public abstract class ComplexOperator extends Operator implements SaveLoadParameters, Cloneable {

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
	
	
	private static final Map<String, Double> equalParameters = new HashMap<String, Double>();
	
	static {
		equalParameters.put("lambda", 0.5d);		
		equalParameters.put("lKsi", 0.5d);		
		equalParameters.put("lPhi", 0.5d);		
	}
	
	public ComplexOperator cloneEquals() throws CloneNotSupportedException {
		ComplexOperator op = (ComplexOperator) super.clone();
		op.loadParameters(equalParameters);		
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
		
	@Override
	public String toString() {
		return operatorType();				
	}
	
	public abstract Slice getParameters();
}
