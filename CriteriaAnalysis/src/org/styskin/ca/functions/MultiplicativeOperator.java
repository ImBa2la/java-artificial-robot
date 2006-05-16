package org.styskin.ca.functions;

import static java.lang.Math.abs;
import static java.lang.Math.max;
import static java.lang.Math.min;

import java.util.List;

public class MultiplicativeOperator extends ComplexOperator {
	
	/**
	 * @throws Exception
	 */
	public MultiplicativeOperator() throws Exception {
		super();
	}
	
	public MultiplicativeOperator(double L) throws Exception {		
		super(L);
	}	
	
	public MultiplicativeOperator(double L, List<Double> weights) throws Exception {		
		super(L, weights);
	}	
	
	public MultiplicativeOperator(List<Double> weights) throws Exception {		
		super(weights);
	}	

	private double FConst;

	private static final double DELTA = 1E-4;


	@Override
	protected String operatorType() {
		return "*";
	}

	@Override
	public void refresh() {
		super.refresh();
		double[] V = new double[weights.size()];
		int i = 0;
		for(double weight : weights) {
			V[i++] = weight;
		}
		reCalculateC(V);
	}

	@Override
	public void addCriteria(double weight) {
		weights.add(weight);
	}

	@Override
	public double getValue(double[] X) throws Exception {
		double result = 1;
		double L = 0;
		if( lambda < 0.5 + DELTA) {
			L = min(lambda, 0.5 - DELTA);
			int i = 0;
			for(double w : weights) {
				result *= 1 + FConst * 2 * L * w * X[i++];
			}

			result = (result - 1)/FConst;
		} else {
			L  = max(lambda, 0.5 + DELTA);
			int i = 0;
			for(double w : weights) {
				result *= 1 + FConst*2*(1 - L)* w * (1 - X[i++]);
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
