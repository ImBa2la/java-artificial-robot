/*
 *$Id: Optimizer.java 62 2007-05-09 16:19:07Z gutalin $
 */
package org.styskin.ca.functions.complex;

import java.util.List;
import static java.lang.Math.pow;
import static java.lang.Math.log;

public class PowerHOperator extends ComplexHOperator {	
	
	private double P, T;

	public PowerHOperator() throws Exception{
		super();
	}
	
	public PowerHOperator(List<Double> weights) throws Exception{
		super(weights);
	}	
	
	@Override
	public double getKsi(double x) {
		return pow(x, P);
	}

	@Override
	public double getPhi(double x) {
		return pow(x, T);
	}
	
	@Override
	public String operatorType() {
		return "pow_h";
	}

	@Override
	public double getValue(double[] X) throws Exception {
		double y = 0;
		double t = 1/T;
		for(int i=0; i < weights.size(); i++) {
			y += weights.get(i)*pow(X[i], t);
		}		
		return pow(y, P);
	}

	@Override
	public void initialize() {
		T  = log(lPhi)/log(1 - lPhi);
		P  = log(lKsi)/log(1 - lKsi);
	}
	
}
