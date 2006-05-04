package org.styskin.ca.functions;

import java.util.List;

import org.styskin.ca.model.Pair;

public class PowerIIOperator extends PowerOperator {

	public PowerIIOperator(double L) throws Exception {
		super(L);
	}

	public PowerIIOperator() throws Exception {
		super();
	}

	public PowerIIOperator(double L, List<Pair<Double, Criteria>> children) throws Exception {
		super(L, children);
	}

	public PowerIIOperator(List<Pair<Double, Criteria>> children) throws Exception {
		super(children);
	}

	@Override
	public double getValue() {
		double result = 0;
		for(Pair<Double, Criteria> pair : children) {
			result += pair.getFirst() * pair.getSecond().getValue();
		}
		return Math.pow(result, FPower);
	}

	@Override
	protected String operatorType() {
		return "**II";
	}
}
