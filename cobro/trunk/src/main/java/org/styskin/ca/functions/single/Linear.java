/*
 *$Id: Optimizer.java 62 2007-05-09 16:19:07Z gutalin $
 */
package org.styskin.ca.functions.single;

public class Linear extends SingleOperator {

	private double lSatiation = 0, rSatiation = 1;

	public double getLLSatiation() {
		return lSatiation;
	}
	public double getLRSatiation() {
		return rSatiation;
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

	@Override
	public double getValue(double[] X) throws Exception {
		double x = X[0];
		if (getLFMin() <= x && x <= getLFMax()) {
			return lSatiation + (rSatiation-lSatiation)*(x-getLFMin())/(getLFMax() - getLFMin());
		} else if (x > getLFMax()) {
			return rSatiation;
		} else {
			return lSatiation;
		}
	}
}
