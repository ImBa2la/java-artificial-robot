/*
 *$Id: Optimizer.java 62 2007-05-09 16:19:07Z gutalin $
 */
package org.styskin.ca.functions.single;

import static java.lang.Math.abs;
import static java.lang.Math.pow;

public class BetaDistribution extends SingleOperator {
	private double lSatiation = 0, rSatiation = 1;

	private double P = 1;
	private double Q = 0;
	private double D;
	private double Km;

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
	public double getP() {
		return P;
	}
	public double getQ() {
		return Q;
	}
	public void calculateD() {
		Km = 0;
		if (abs(getFMin() - getFMax()) < EPS) {
			D = 0;
			Km = getFMin();
		} else if ( abs(P) < EPS &&  abs(Q) < EPS) {
			D = 1;
		} else {
			Km = (getFMax() * P + getFMin() * Q) / (P + Q);
			D = 1/(pow(Km-getFMin(),P)*pow(getFMax()-Km,Q));
		}
	}
	public void setP(double p) {
		if(p > -EPS && p < 100 + EPS) {
			P = p;
			calculateD();
		}
	}
	public void setQ(double q) {
		if(q > -EPS && q < 100 + EPS) {
			Q = q;
			calculateD();
		}
	}
	@Override
	public void setFMax(double max) {
		super.setFMax(max);
		calculateD();
	}
	@Override
	public void setFMin(double min) {
		super.setFMin(min);
		calculateD();
	}

	@Override
	public double getValue(double[] X) throws Exception {
		double x = X[0];
		if (getFMin() <= x && x <= getFMax()) {
			if (x < Km) {
				return lSatiation + (1-rSatiation)*D*pow(x - getFMin(), P) * pow(getFMax() - x, Q);
			} else {
				return rSatiation + (1-rSatiation)*D*pow(x - getFMin(), P) * pow(getFMax() - x, Q);
			}
		} else if (x > getFMax()) {
			return rSatiation;
		} else {
			return lSatiation;
		}
	}

}
