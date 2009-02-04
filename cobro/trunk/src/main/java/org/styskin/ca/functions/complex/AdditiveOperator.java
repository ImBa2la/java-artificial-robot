/*
 *$Id$
 */
package org.styskin.ca.functions.complex;

import java.util.List;
import java.util.Map;

import org.styskin.ca.model.Slice;


public class AdditiveOperator extends ComplexOperator {
	
	public AdditiveOperator() throws Exception {
		super();
	}	

	public AdditiveOperator(List<Double> children) throws Exception {
		super(children);
	}


	@Override
	public String operatorType() {
		return "add";
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

	public void loadParameters(Map<String, Double> parameters) {}

	public void saveParameters(Map<String, Double> parameters) {}
	
	class EmptySlice implements Slice {

		public double get(int index) {
			return 0;
		}

		public double getLowerBound(int index) {
			return 0;
		}

		public double getUpperBound(int index) {
			return 0;
		}

		public void set(int index, double value) {
		}

		public int size() {
			return 0;
		}		
	}
	
	private EmptySlice emptySlice = new EmptySlice();

	@Override
	public Slice getParameters() {
		return emptySlice;
	}
}
