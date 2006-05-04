package org.styskin.ca.functions;

import java.util.List;

import org.styskin.ca.model.Pair;

public class PowerDoubleOperator extends PowerOperator {
	public PowerDoubleOperator(double L) throws Exception {
		super(L);
	}

	public PowerDoubleOperator() throws Exception {
		super();
	}

	public PowerDoubleOperator(double L, List<Pair<Double, Criteria>> children) throws Exception {
		super(L, children);
	}

	public PowerDoubleOperator(List<Pair<Double, Criteria>> children) throws Exception {
		super(children);
	}

	@Override
	public double getValue() {
		double result = 0;
		for(Pair<Double, Criteria> pair : children) {
			result += pair.getFirst() * Math.pow(pair.getSecond().getValue(), FPower);
		}
		return Math.pow(result, 1/FPower);
	}

	@Override
	protected String operatorType() {
		return "**D";
	}
}
