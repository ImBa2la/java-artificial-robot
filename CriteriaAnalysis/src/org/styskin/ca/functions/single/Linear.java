package org.styskin.ca.functions.single;

public class Linear extends SingleOperator {

	private double lSatiation = 0, rSatiation = 1;

	public double getLSatiation() {
		return lSatiation;
	}
	public double getRSatiation() {
		return rSatiation;
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

	@Override
	public double getValue(double[] X) throws Exception {
		double x = X[0];
		if (getFMin() <= x && x <= getFMax()) {
			return lSatiation + (rSatiation-lSatiation)*(x-getFMin())/(getFMax() - getFMin());
		} else if (x > getFMax()) {
			return rSatiation;
		} else {
			return lSatiation;
		}
	}
}
