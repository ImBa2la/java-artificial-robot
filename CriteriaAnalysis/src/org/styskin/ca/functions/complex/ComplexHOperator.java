package org.styskin.ca.functions.complex;

import java.util.List;

public abstract class ComplexHOperator extends ComplexOperator {
	
	double lPhi, lKsi;	
	
	public ComplexHOperator() throws Exception{
		super();
	}
	
	public ComplexHOperator(List<Double> weights) throws Exception{
		super(weights);
	}
	
	public void initialize(double lPhi, double lKsi) {
		this.lKsi = lKsi;
		this.lPhi = lPhi;
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
		initialize(lPhi, lKsi);
	}
}
