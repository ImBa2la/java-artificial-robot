/*
 *$Id: Optimizer.java 62 2007-05-09 16:19:07Z gutalin $
 */
package org.styskin.ca.functions.complex;

import static java.lang.Math.log;
import static java.lang.Math.pow;
import it.unimi.dsi.fastutil.doubles.DoubleList;

public class PowerIOperator extends ComplexH1Operator {
	
	private double P;	

	public PowerIOperator() throws Exception {
		super();
	}

	public PowerIOperator(DoubleList weights) throws Exception{
		super(weights);
	}	
	
	
	@Override
	public String operatorType() {
		return "pow_I";
	}
	
	@Override
	public double getKsi(double x) {
		return pow(x, P);
	}
	@Override
	public double getPhi(double x) {
		return x;
	}
	
	public double getValue(double[] X) throws Exception {
		double y = 0;
		for(int i=0; i < weights.size(); i++) {
			y += weights.getDouble(i)*X[i];
		}
		// About 50% of time
		return pow(y, P);
	}

	@Override
	public void initialize() {
		P  = log(lambda)/log(1 - lambda);
	}
}
