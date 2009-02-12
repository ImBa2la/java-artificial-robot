/*
 *$Id: Optimizer.java 62 2007-05-09 16:19:07Z gutalin $
 */
package org.styskin.ca.functions.single;

import static java.lang.Math.abs;
import static java.lang.Math.pow;

public class BetaDistribution extends SingleOperatorSatiation {
	private double P = 1;
	private double Q = 0;
	private double D;
	private double Km;

	public double getLP() {
		return P;
	}
	public double getLQ() {
		return Q;
	}
	
	public void calculateD() {
		Km = 0;
		if (abs(getLFMin() - getLFMax()) < EPS) {
			D = 0;
			Km = getLFMin();
		} else if ( abs(P) < EPS &&  abs(Q) < EPS) {
			D = 1;
		} else {
			Km = (getLFMax() * P + getLFMin() * Q) / (P + Q);
			D = 1/(pow(Km-getLFMin(),P)*pow(getLFMax()-Km,Q));
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
	public double getValue(double x) {
		if (fMin <= x && x <= fMax) {
			if (x < Km) {
				return lSatiation + (1-rSatiation)*D*pow(x - getLFMin(), P) * pow(getLFMax() - x, Q);
			} else {
				return rSatiation + (1-rSatiation)*D*pow(x - getLFMin(), P) * pow(getLFMax() - x, Q);
			}
		} else {
			return super.getValue(x);
		}
	}

}
