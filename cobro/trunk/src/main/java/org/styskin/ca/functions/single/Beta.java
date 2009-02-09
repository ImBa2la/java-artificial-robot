/*
 *$Id: Optimizer.java 62 2007-05-09 16:19:07Z gutalin $
 */
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
	public double getLP() {
		return P;
	}
	public double getLQ() {
		return Q;
	}
	
	public void calculateD() {
//		Km = 0;
		if (abs(getLFMin() - getLFMax()) < EPS) {
			D = 0;
//			Km = getFMin();
		} else if ( abs(P) < EPS &&  abs(Q) < EPS) {
			D = 1/(getLFMax() - getLFMin());
		} else {
//			Km = (getFMax() * P + getFMin() * Q) / (P + Q);
			D = 0;
			double step = (getLFMax() - getLFMin())/C;
			double hX = getLFMin() + step, hF, lF = 0;
			while (hX < getLFMax()) {
				hF = pow(hX-getLFMin(), P) * pow(getLFMax()-hX, Q);
				D += (lF + hF)/2 * step;
				lF = hF;
				hX += step;
			}
			D = 1/D;
		}
	}
	public void setLP(double p) {
		if(p > -EPS && p < 100 + EPS) {
			P = p;
			calculateD();
		}
	}
	public void setLQ(double q) {
		if(q > -EPS && q < 100 + EPS) {
			Q = q;
			calculateD();
		}
	}
	@Override
	public void setLFMax(double max) {
		super.setLFMax(max);
		calculateD();
	}
	@Override
	public void setLFMin(double min) {
		super.setLFMin(min);
		calculateD();
	}

	@Override
	public double getValue(double[] X) throws Exception {
		double x = X[0];
		double y = 0;
		int C = 1000;
		if (getLFMin() <= x && x <= getLFMax()) {
			double hX, hF, lF = 0;
			double step = (getLFMax() - getLFMin())/C;
			hX = getLFMin() + step;
			while (hX <= x) {
				hF = pow(hX -getLFMin(), P) * pow(getLFMax()-hX,Q);
				y += (lF + hF)/2*step;
				lF = hF;
				hX += step;
			}
			return lSatiation + (rSatiation - lSatiation)*y*D;
		} else if (x > getLFMax()) {
			return rSatiation;
		} else {
			return lSatiation;
		}
	}

}
