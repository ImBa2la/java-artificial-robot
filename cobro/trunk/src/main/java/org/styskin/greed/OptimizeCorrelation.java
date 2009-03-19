package org.styskin.greed;

import static org.styskin.greed.MatrixUtils.correlation;

public class OptimizeCorrelation extends Optimizer {

	@Override
	double check(double[] a, double[] b, int[] q) {
		return correlation(a, b);
	}

}
