/*
 *$Id: Optimizer.java 62 2007-05-09 16:19:07Z gutalin $
 */
package org.styskin.ca.functions.complex;

import it.unimi.dsi.fastutil.doubles.DoubleList;

import java.util.Map;

import org.styskin.ca.model.Slice;

public abstract class ComplexHOperator extends ComplexOperator {

	double lPhi = 0.5, lKsi = 0.5;

	public ComplexHOperator() throws Exception {
		super();
	}

	public ComplexHOperator(DoubleList weights) throws Exception {
		super(weights);
	}

	public void initialize() {
	}

	public double getLKsi() {
		return lKsi;
	}

	public double getLPhi() {
		return lPhi;
	}

	@Override
	public String operatorType() {
		return "H-type";
	}

	@Override
	public void refresh() {
		normalize();
		initialize();
	}

	@Override
	public String toString() {
		return super.toString() + " : lPhi = " + FORMAT.format(lPhi)
				+ ", lKsi = " + FORMAT.format(lKsi);
	}

	public void loadParameters(Map<String, Double> parameters) {
		lPhi = parameters.containsKey("lPhi") ? parameters.get("lPhi") : 0.5 ;
		lKsi = parameters.containsKey("lKsi") ? parameters.get("lKsi") : 0.5;
		initialize();
	}

	public void saveParameters(Map<String, Double> parameters) {
		parameters.put("lPhi", lPhi);
		parameters.put("lKsi", lKsi);
	}

	class TwoParametersSlice implements Slice {

		public double get(int index) {
			switch (index) {
			case 0:
				return lPhi;
			case 1:
				return lKsi;
			}
			return 0;
		}

		public double getLowerBound(int index) {
			return 0.1;
		}

		public double getUpperBound(int index) {
			return 0.9;
		}

		public void set(int index, double value) {
			if(value < getLowerBound(index)) {
				value = getLowerBound(index);
			}
			if(value > getUpperBound(index)) {
				value = getUpperBound(index);
			}
			switch (index) {
			case 0:
				lPhi = value;
				break;
			case 1:
				lKsi = value;
				break;
			}
			initialize();
		}

		public int size() {
			return 2;
		}
	}

	private TwoParametersSlice twoParametersSlice = null;

	public TwoParametersSlice getTwoParametersSlice() {
		if(twoParametersSlice == null) {
			twoParametersSlice = new TwoParametersSlice();
		}
		return twoParametersSlice;
	}

	@Override
	public Slice getParameters() {
		return getTwoParametersSlice();
	}

	@Override
	public ComplexOperator clone() throws CloneNotSupportedException {
		ComplexHOperator op = (ComplexHOperator) super.clone();
		op.twoParametersSlice = null;
		return op;
	}
}
