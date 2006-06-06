package org.styskin.ca.functions.single;

import org.styskin.ca.functions.Operator;

public class SingleOperator extends Operator {

	@Override
	public double getValue(double[] X) throws Exception {
		return X[0];
	}

}
