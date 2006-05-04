package org.styskin.ca.functions;

import static java.lang.Math.abs;
import static java.lang.Math.max;
import static java.lang.Math.min;

import java.util.List;

import org.styskin.ca.model.Pair;

public class MultiplicativeOperator extends ComplexOperator {

	private double FConst;

	private static final double DELTA = 1E-4;

	public MultiplicativeOperator(double L, List<Pair<Double, Criteria>> children) throws Exception {
		super(L, children);
	}

	public MultiplicativeOperator(List<Pair<Double, Criteria>> children) throws Exception {
		super(children);
	}

	public MultiplicativeOperator(double L) throws Exception {
		super(L);
	}

	public MultiplicativeOperator() throws Exception {
		super();
	}

	@Override
	protected String operatorType() {
		return "+";
	}

	@Override
	public void refresh() {
		super.refresh();
		double[] V = new double[children.size()];
		int i = 0;
		for(Pair<Double, Criteria> pair : children) {
			V[i++] = pair.getFirst();
		}
		reCalculateC(V);
	}

	@Override
	public void addCriteria(Criteria criteria, double weight) {
		children.add(new Pair<Double, Criteria>(weight, criteria));
		double sum = 0;
		double[] V = new double[children.size()];
		int i = 0;
		for(Pair<Double, Criteria> pair : children) {
			V[i++] = pair.getFirst();
			sum += V[i - 1];
		}
/*		if(Math.abs(sum - 1) < EPS) {
			reCalculateC(V);
		}*/
	}

	@Override
	public double getValue() {
		double result = 1;
		double L = 0;
		if( lambda < 0.5 + DELTA) {
			L = min(lambda, 0.5 - DELTA);
			for(Pair<Double, Criteria> pair : children) {
				result *= 1 + FConst * 2 * L * pair.getFirst() * pair.getSecond().getValue();
			}

			result = (result - 1)/FConst;
		} else {
			L  = max(lambda, 0.5 + DELTA);
			for(Pair<Double, Criteria> pair : children) {
				result *= 1 + FConst*2*(1 - L)* pair.getFirst()*(1 - pair.getSecond().getValue());
			}
			result = 1 - (result - 1)/FConst;
		}
		return result;
	}

	private void reCalculateC(double[] V) {
	    if(lambda < 0.5 + EPS) {
	    	FConst = calculateC(min(lambda, 0.5 - EPS), V);
	    } else {
	    	FConst = calculateC(1-max(lambda, 0.5 + EPS), V);
	    }
	}

	private double calculate(double L, double C, double[] V) {
		double result = 1;
		for(double v : V) {
			result *= 1 + 2*C*L*v;
		}
		return result - C - 1;
	}

	private double calculateG(double L, double C, double[] V) {
		double result = 1;
		for(double v : V) {
			result *= 1 + 2*C*L*v;
		}
		return (result -1)/C;
	}

	private double calculateC( double L, double[] V) {
		double g = 0, result = 0;
		double Delta = 10;
		double leftV = 0, rightV = 0;
		double right = Delta;

		do {
			rightV = calculate(L, right, V);
			if( (rightV > 0 && leftV < EPS) || (rightV < 0 && leftV > 0)) {
				g = calculateG(L, right, V);
				result = right;
				Delta /= 2;
				right -= Delta;
			} else {
				leftV = rightV;
				right += Delta;
			}
		} while(!(abs(g - 1) < EPS) || Delta < EPS);

		return result;
	}
}
