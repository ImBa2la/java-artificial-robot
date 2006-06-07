package org.styskin.ca.functions.single;

public class Gaussian extends SingleOperator {

	private double lSatiation = 0, rSatiation = 1;

	public double getLSatiation() {
		return lSatiation;
	}
	public double getRSatiation() {
		return rSatiation;
	}
	protected void setLSatiation(double satiation) {
		if(satiation > -EPS && satiation < 1 + EPS) {
			lSatiation = satiation;
		}
	}
	protected void setRSatiation(double satiation) {
		if(satiation > -EPS && satiation < 1 + EPS) {
			rSatiation = satiation;
		}
	}


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
	public double getValue(double[] X) {
		double x = X[0];
		if (getFMin() <= x && x <= getFMax()) {
			return lSatiation + (rSatiation-lSatiation)*(gaussian((x-getFMin())/(getFMax() - getFMin())*3.92 - 1.96, 1E-3) - 0.025)/0.95;
		} else if (x > getFMax()) {
			return rSatiation;
		} else {
			return lSatiation;
		}
	}

}
