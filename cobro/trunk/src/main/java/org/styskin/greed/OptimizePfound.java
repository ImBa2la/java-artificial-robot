package org.styskin.greed;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OptimizePfound extends Optimizer {

	@Override
	double check(double[] a, double[] b, int[] q) {
		List<UrlQueryScore> list = new ArrayList<UrlQueryScore>();
		for(int i=0; i < a.length; i++)
			list.add(new UrlQueryScore(q[i], a[i], b[i]));
		Collections.sort(list, new UrlQueryScoreComparator());
		double pfound = 0;
		int N = -1;
		int cq = -1;
		double pCont = 1.0;
		for(int i=0; i < a.length; i++) {
			UrlQueryScore t = list.get(i);
			if(t.q != cq) {
				cq = t.q;
				++ N;
				pCont = 1.0;
			}
			pfound += t.real*pCont;
			pCont *= 0.85*(1.0 - t.real);
		}		
		return pfound/N;
	}

}
