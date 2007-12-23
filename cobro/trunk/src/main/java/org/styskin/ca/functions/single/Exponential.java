/*
 *$Id: Optimizer.java 62 2007-05-09 16:19:07Z gutalin $
 */
package org.styskin.ca.functions.single;

import static java.lang.Math.pow;

public class Exponential extends SingleOperator {

	private double lSatiation = 0, rSatiation = 1, base = 1;

	public double getLSatiation() {
		return lSatiation;
	}
	public double getRSatiation() {
		return rSatiation;
	}
	public double getBase() {
		return base;
	}
	public void setLSatiation(double satiation) {
		if(satiation > -EPS && satiation < 1 + EPS) {
			lSatiation = satiation;
		}
	}
	public void setRSatiation(double satiation) {
		if(satiation > -EPS && satiation < 1 + EPS) {
			rSatiation = satiation;
		}
	}
	public void setBase(double base) {
		if(base > -EPS && base < 1 + EPS) {
			this.base = base;
		}
	}

	@Override
	public double getValue(double[] X) throws Exception {
		double x = X[0];
		if (getFMin() <= x && x <= getFMax()) {
			return lSatiation + (rSatiation-lSatiation)*(1-pow(base, (x - getFMin())/(getFMax()-getFMin())))/(1 - base);
		} else if (x > getFMax()) {
			return rSatiation;
		} else {
			return lSatiation;
		}
	}
}
