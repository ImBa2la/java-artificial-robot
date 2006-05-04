package org.styskin.ca.functions;

import java.util.List;

import org.styskin.ca.model.Pair;

strictfp public class AdditiveOperator extends ComplexOperator {

	public AdditiveOperator(double L, List<Pair<Double, Criteria>> children)
		throws Exception {
		super(L, children);
	}

	public AdditiveOperator(List<Pair<Double, Criteria>> children) throws Exception {
		super(children);
	}

	public AdditiveOperator(double L) throws Exception {
		super(L);
	}

	public AdditiveOperator() throws Exception {
		super();
	}

	@Override
	protected String operatorType() {
		return "+";
	}

	@Override
	public double getValue() {
		double result = 0;
		for(Pair<Double, Criteria> pair : children) {
			double v = pair.getFirst();
			Criteria child = pair.getSecond();

			result += v * child.getValue();
		}
		return result;
	}
}
