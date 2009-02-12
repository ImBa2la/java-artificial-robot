/*
 *$Id: Optimizer.java 62 2007-05-09 16:19:07Z gutalin $
 */
package org.styskin.ca.functions.single;

public class Linear extends SingleOperatorSatiation {

	@Override
	public double getValue(double x) {
		if (fMin <= x && x <= fMax)
			return lSatiation + (rSatiation-lSatiation)*(x-getLFMin())/(getLFMax() - getLFMin());
		else
			return super.getValue(x);
	}
}
