/*
 *$Id: Optimizer.java 62 2007-05-09 16:19:07Z gutalin $
 */
package org.styskin.ca.functions.complex;

import it.unimi.dsi.fastutil.doubles.DoubleList;

import java.util.Map;

import org.styskin.ca.model.Slice;

public abstract class ComplexH1Operator extends ComplexOperator {

	double lambda = 0.5;	
	
	public ComplexH1Operator() throws Exception {
		super();
	}
	
	public ComplexH1Operator(DoubleList weight) throws Exception {
		super(weight);
	}
	
	public void initialize() {}
	
	public abstract double getKsi(double x);
	public abstract double getPhi(double x);

	
	@Override
	public void refresh() {
		normalize();
		initialize();
	}
	
	public void loadParameters(Map<String, Double> parameters) {
		lambda = parameters.containsKey("lambda") ? parameters.get("lambda") : 0.5 ;
		initialize();
	}

	public void saveParameters(Map<String, Double> parameters) {
		parameters.put("lambda", lambda);
	}

	class OneParametersSlice implements Slice {

		public double get(int index) {
			return index == 0 ? lambda : 0;
		}

		public double getLowerBound(int index) {
			return 0.2;
		}

		public double getUpperBound(int index) {
			return 0.8;
		}

		public void set(int index, double value) {
			lambda = value;
			if(lambda < getLowerBound(index)) {
				lambda = getLowerBound(index);
			}
			if(lambda > getUpperBound(index)) {
				lambda = getUpperBound(index);
			}
		}

		public int size() {
			return 1;
		}
	}	
	
	private OneParametersSlice oneParametersSlice = null;

	public OneParametersSlice getOneParametersSlice() {
		if(oneParametersSlice == null) {
			oneParametersSlice = new OneParametersSlice();
		}
		return oneParametersSlice;
	}

	@Override
	public Slice getParameters() {
		return getOneParametersSlice();
	}
	
	@Override
	public String operatorType() {
		return "H1-type";
	}
	
	@Override
	public ComplexOperator clone() throws CloneNotSupportedException {
		ComplexH1Operator op = (ComplexH1Operator) super.clone();
		op.oneParametersSlice = null;
		return op;
	}


}
