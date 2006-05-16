package org.styskin.ca.functions;

import java.util.List;

public abstract class PowerOperator extends ComplexOperator {

	protected double FPower = 0;
	
	@Override
	public void refresh() {
		if( lambda < 1 && lambda > 0) {
			FPower = Math.log(lambda)/Math.log(1 - lambda);
		}		
	}

	public PowerOperator(double L) throws Exception {
		super(L);
	}

	public PowerOperator() throws Exception {
		super();
	}

	public PowerOperator(double L, List<Double> children) throws Exception {
		super(L, children);
	}

	public PowerOperator(List<Double> children) throws Exception {
		super(children);
	}

	@Override
	protected String operatorType() {
		return "**";
	}
}
