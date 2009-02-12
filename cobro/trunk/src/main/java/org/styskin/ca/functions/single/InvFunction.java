package org.styskin.ca.functions.single;

public class InvFunction extends SingleOperatorSatiation {
	
	{
		setLLSatiation(0.95);
		setLRSatiation(0.05);
	}

	@Override
	public double getValue(double x) {
		if (fMin <= x && x <= fMax) {
			double a = (rSatiation - lSatiation) / (1/getLFMin() - 1/getLFMax());
			double b = lSatiation - a/getLFMax();
			return a / (x + EPS) + b;
		} else {
			return super.getValue(x);
		}
	}

}
