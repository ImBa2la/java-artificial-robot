/*
 *$Id: Optimizer.java 62 2007-05-09 16:19:07Z gutalin $
 */
package org.styskin.ca.functions.single;

public class Gaussian extends SingleOperatorSatiation {

	public double gaussian(double x, double e) {
		double t = x;
		double sum = x, x2 = x*x, s = 0;
		int m = 2;
		do {
			s = sum;
			t *= -x2/m;
			sum += t/(m+1);
			m += 2;
		} while (Math.abs(s-sum) > e/10);
		t = 0.5 + sum/Math.sqrt(2*Math.PI);
		return Math.round(t/e) * e;
	}

	@Override
	public double getValue(double x) {
		if (fMin <= x && x <= fMax) {
			return lSatiation + (rSatiation-lSatiation)*(gaussian((x-getLFMin())/(getLFMax() - getLFMin())*3.92 - 1.96, 1E-3) - 0.025)/0.95;
		} else {
			return super.getValue(x);
		}
	}

}
