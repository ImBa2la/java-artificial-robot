/*
 *$Id: Optimizer.java 62 2007-05-09 16:19:07Z gutalin $
 */
package org.styskin.ca.functions.single;

import static java.lang.Math.pow;

public class Exponential extends SingleOperator {

	private double lSatiation = 0, rSatiation = 1, base = 1;

	public double getLLSatiation() {
		return lSatiation;
	}
	public double getLRSatiation() {
		return rSatiation;
	}
	public double getLBase() {
		return base;
	}
	public void setLLSatiation(double satiation) {
		if(satiation > -EPS && satiation < 1 + EPS) {
			lSatiation = satiation;
		}
	}
	public void setLRSatiation(double satiation) {
		if(satiation > -EPS && satiation < 1 + EPS) {
			rSatiation = satiation;
		}
	}
	public void setLBase(double base) {
		if(base > -EPS && base < 1 + EPS) {
			this.base = base;
		}
	}

	@Override
	public double getValue(double[] X) throws Exception {
		double x = X[0];
		if (getLFMin() <= x && x <= getLFMax()) {
			return lSatiation + (rSatiation-lSatiation)*(1-pow(base, (x - getLFMin())/(getLFMax()-getLFMin())))/(1 - base);
		} else if (x > getLFMax()) {
			return rSatiation;
		} else {
			return lSatiation;
		}
	}
}
