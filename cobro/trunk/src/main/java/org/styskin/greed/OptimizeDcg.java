package org.styskin.greed;

import static java.lang.Math.log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.styskin.greed.Formula.T;

public class OptimizeDcg extends Optimizer {
	
	static class T {
		int q;
		double score;
		double real;
		
		public T(int q, double score, double real) {
			this.q = q;
			this.score = score;
			this.real = real;
		}
	}


	@Override
	double check(double[] r, double[] b, int[] q) {
		List<T> list = new ArrayList<T>();
		for(int i=0; i < r.length; i++)
			list.add(new T(q[i], r[i], b[i]));
		Collections.sort(list, new Comparator<T>() {
			public int compare(T o1, T o2) {
				if(o1.q  == o2.q) {
					return -Double.compare(o1.score, o2.score);										
				} else {				
					return o1.q < o2.q ? -1 : 1;
				}
			}
		});
		double dcg = 0;
		int N = -1;
		int cq = -1, j = 0;
		for(int i=0; i < r.length; i++) {
			T t = list.get(i);
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
