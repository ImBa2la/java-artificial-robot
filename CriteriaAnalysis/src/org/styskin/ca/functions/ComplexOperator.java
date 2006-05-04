package org.styskin.ca.functions;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import org.styskin.ca.model.Pair;

strictfp public abstract class ComplexOperator extends Operator {

	static protected NumberFormat FORMAT = DecimalFormat.getInstance();

	protected double lambda;

	protected List<Pair<Double, Criteria>> children;

	void normalize() {
		double s = 0;
		for(Pair<Double, Criteria> x : children) {
			s += x.getFirst();
		}
		for(Pair<Double, Criteria> x : children) {
			x.setFirst(x.getFirst()/s);
		}
	}

	public void refresh() {
		normalize();
	}

	public void addCriteria(Criteria criteria, double weight) {
		children.add(new Pair<Double, Criteria>(weight, criteria));
	}

	public ComplexOperator(double L, List<Pair<Double, Criteria>> children)
		throws Exception {
		double w = 0;
		for(Pair<Double, Criteria> pair : children) {
			double v = pair.getFirst();
			if(v < 0) {
				throw new Exception("Weight is negative");
			}
			w += v;
		}
		if(Math.abs(w - 1) > EPS) {
			throw new Exception("Sum of weigths isn't equal to 1");
		}
		lambda = L;
		this.children = children;
	}

	public ComplexOperator(List<Pair<Double, Criteria>> children)
		throws Exception {
		this(0.5, children);
	}

	public ComplexOperator(double L) throws Exception {
		lambda = L;
		children = new ArrayList<Pair<Double, Criteria>>();
	}

	public ComplexOperator() throws Exception {
		this(0.5);
	}

	public int getSize() {
		return children.size();
	}

	protected String operatorType() {
		return "";
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("(").append(operatorType()).append(", L=").append(FORMAT.format(lambda)).append(' ');

		for(Pair<Double, Criteria> pair : children) {
			sb.append('{').append(FORMAT.format(pair.getFirst())).append(" - ").append(pair.getSecond().toString()).append('}');
		}
		sb.append(')');
		return sb.toString();
	}

}
