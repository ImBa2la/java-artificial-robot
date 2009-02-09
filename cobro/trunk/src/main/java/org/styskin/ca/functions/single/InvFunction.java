package org.styskin.ca.functions.single;

public class InvFunction extends SingleOperator {
	
	private double lSatiation = 0.05, rSatiation = 0.95;

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

	public double getValue(double[] X) throws Exception {
		double x = X[0];
		if(x < getLFMin()) {
			return rSatiation;
		} else if (x > getLFMax()) {
			return lSatiation; 
		} else {
			double a = (rSatiation - lSatiation) / (1/getLFMin() - 1/getLFMax());
			double b = lSatiation - a/getLFMax();
			return a / (x + EPS) + b;
		}
	}

}
