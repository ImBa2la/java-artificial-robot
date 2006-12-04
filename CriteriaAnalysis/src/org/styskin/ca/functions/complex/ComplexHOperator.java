package org.styskin.ca.functions.complex;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.styskin.ca.model.Slice;

public abstract class ComplexHOperator extends ComplexOperator {
	
	double lPhi = 0.5, lKsi = 0.5;	
	
	public ComplexHOperator() throws Exception{
		super();
	}
	
	public ComplexHOperator(List<Double> weights) throws Exception{
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
		return super.toString() + " : lPhi = " + FORMAT.format(lPhi) + ", lKsi = " + FORMAT.format(lKsi);
	}
	
	public void load(Map<String, Double> parameters) {
		lPhi = parameters.get("lPhi");
		lKsi = parameters.get("lKsi");		
	}

	public Map<String, Double> save() {
		Map<String, Double> parameters = new HashMap<String, Double>();
		parameters.put("lPhi", lPhi);
		parameters.put("lKsi", lKsi);
		return parameters;
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
			return 0;
		}

		public double getUpperBound(int index) {
			return 1;
		}

		public void set(int index, double value) {
			switch (index) {
			case 0:				
				lPhi = value;
				break;
			case 1:				
				lKsi = value;
				break;
			}
		}

		public int size() {
			return 2;
		}		
	}
	
	private TwoParametersSlice twoParametersSlice = new TwoParametersSlice(); 
	
	@Override
	public Slice getParameters() {
		return twoParametersSlice;
	}
}
