package org.styskin.ca.functions;

import java.util.List;

import org.styskin.ca.model.Pair;

public class PowerIOperator extends PowerOperator {

	public PowerIOperator(double L) throws Exception {
		super(L);
	}

	public PowerIOperator() throws Exception {
		super();
	}

	public PowerIOperator(double L, List<Pair<Double, Criteria>> children) throws Exception {
		super(L, children);
	}

	public PowerIOperator(List<Pair<Double, Criteria>> children) throws Exception {
		super(children);
	}

	@Override
	public double getValue() {
		double result = 0;
		for(Pair<Double, Criteria> pair : children) {
			result += pair.getFirst() * Math.pow(pair.getSecond().getValue(), FPower);
		}
		return result;
	}

	@Override
	protected String operatorType() {
		return "**I";
	}
}
