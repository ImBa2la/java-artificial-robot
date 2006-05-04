package org.styskin.ca.functions;

import java.util.List;

import org.styskin.ca.model.Pair;

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

	public PowerOperator(double L, List<Pair<Double, Criteria>> children) throws Exception {
		super(L, children);
		if( L < 1 && L > 0) {
			FPower = Math.log(L)/Math.log(1 - L);
		}
	}

	public PowerOperator(List<Pair<Double, Criteria>> children) throws Exception {
		super(children);
	}

	@Override
	protected String operatorType() {
		return "**";
	}
}
