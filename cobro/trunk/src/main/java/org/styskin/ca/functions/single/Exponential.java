/*
 *$Id: Optimizer.java 62 2007-05-09 16:19:07Z gutalin $
 */
package org.styskin.ca.functions.single;

import static java.lang.Math.pow;

public class Exponential extends SingleOperatorSatiation {

	private double base = 1;

	public void setLBase(double base) {
		if(base > -EPS && base < 1 + EPS) {
			this.base = base;
		}
	}

	@Override
	public double getValue(double x) {
		if (fMin <= x && x <= fMax) {
			return lSatiation + (rSatiation-lSatiation)*(1-pow(base, (x - getLFMin())/(getLFMax()-getLFMin())))/(1 - base);
		} else {
			return super.getValue(x);
		}
	}
}
