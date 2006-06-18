package org.styskin.ca.functions.single;

import static java.lang.Math.abs;
import static java.lang.Math.pow;

public class Beta extends SingleOperator {
	private static final int C = 1000;

	private double lSatiation = 0, rSatiation = 1;

	private double P = 1;
	private double Q = 0;
	private double D;
//	private double Km;

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
//		Km = 0;
		if (abs(getFMin() - getFMax()) < EPS) {
			D = 0;
//			Km = getFMin();
		} else if ( abs(P) < EPS &&  abs(Q) < EPS) {
			D = 1/(getFMax() - getFMin());
		} else {
//			Km = (getFMax() * P + getFMin() * Q) / (P + Q);
			D = 0;
			double step = (getFMax() - getFMin())/C;
			double hX = getFMin() + step, hF, lF = 0;
			while (hX < getFMax()) {
				hF = pow(hX-getFMin(), P) * pow(getFMax()-hX, Q);
				D += (lF + hF)/2 * step;
				lF = hF;
				hX += step;
			}
			D = 1/D;
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
		double y = 0;
		int C = 1000;
		if (getFMin() <= x && x <= getFMax()) {
			double hX, hF, lF = 0;
			double step = (getFMax() - getFMin())/C;
			hX = getFMin() + step;
			while (hX <= x) {
				hF = pow(hX -getFMin(), P) * pow(getFMax()-hX,Q);
				y += (lF + hF)/2*step;
				lF = hF;
				hX += step;
			}
			return lSatiation + (rSatiation - lSatiation)*y*D;
		} else if (x > getFMax()) {
			return rSatiation;
		} else {
			return lSatiation;
		}
	}

}
