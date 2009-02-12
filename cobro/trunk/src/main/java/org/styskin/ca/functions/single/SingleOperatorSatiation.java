package org.styskin.ca.functions.single;

public class SingleOperatorSatiation extends SingleOperator {
	
	protected double lSatiation = 0, rSatiation = 1;

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
	public double getValue(double x) {
		if (x > getLFMax()) {
			return rSatiation;
		} else {
			return lSatiation;
		}
	}



}
