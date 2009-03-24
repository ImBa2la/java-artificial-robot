package org.styskin.greed;

import static java.lang.Math.log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OptimizeDcg extends Optimizer {
	
	@Override
	double check(double[] r, double[] b, int[] q) {
		List<UrlQueryScore> list = new ArrayList<UrlQueryScore>();
		for(int i=0; i < r.length; i++)
			list.add(new UrlQueryScore(q[i], r[i], b[i]));
		Collections.sort(list, new UrlQueryScoreComparator());
		double dcg = 0;
		int N = -1;
		int cq = -1, j = 0;
		for(int i=0; i < r.length; i++) {
			UrlQueryScore t = list.get(i);
			if(t.q != cq) {
				j = 1;
				cq = t.q;
				++ N;
			}
			dcg += t.real/(log(j)/log(2) + 1);
			j++;
		}		
		return dcg/N;		
	}

}
