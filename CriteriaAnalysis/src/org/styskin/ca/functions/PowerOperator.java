package org.styskin.ca.functions;

import java.util.List;

public abstract class PowerOperator extends ComplexOperator {

	protected double FPower = 0;

	public PowerOperator(double L) throws Exception {
		super(L);
		if( L < 1 && L > 0) {
			FPower = Math.log(L)/Math.log(1 - L);
		}
	}

	public PowerOperator() throws Exception {
		super();
	}

	public PowerOperator(double L, List<Double> children) throws Exception {
		super(L, children);
		if( L < 1 && L > 0) {
			FPower = Math.log(L)/Math.log(1 - L);
		}
	}

	public PowerOperator(List<Double> children) throws Exception {
		super(children);
	}

	@Override
	protected String operatorType() {
		return "**";
	}
}
