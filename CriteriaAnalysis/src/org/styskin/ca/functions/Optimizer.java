/*
 *$Id$
 */
package org.styskin.ca.functions;

import java.util.LinkedList;
import java.util.Queue;

import org.styskin.ca.model.Constants;

public class Optimizer implements Constants {

	private CacheCriteria cache;

	private Criteria root;

	private boolean searchPoint(double[] V, double[] h, ComplexCriteria c) {
		boolean moved = false;

		ComplexOperator op = c.operator;
		cache.turnOffCache(c);

		double f = cache.check();
		op.lambda += h[0];
		op.refresh();
		if (cache.check() < f) {
			V[0] += h[0];
			moved = true;
		} else {
			op.lambda -= 2*h[0];
			op.refresh();
			if (cache.check() < f) {
				V[0] -= h[0];
				moved = true;
			}
		}

		for(int i = 1; i < V.length; i++) {
			op.weights.set(i-1, V[i] + h[i]);
			op.refresh();
			if (cache.check() < f) {
				V[i] += h[i];
				moved = true;
			} else {
				op.weights.set(i-1, V[i] - h[i]);
				op.refresh();
				if (cache.check() < f) {
					V[i] -= h[i];
					moved = true;
				}
			}
		}
		op.lambda = V[0];
		for(int i = 0; i < op.weights.size(); i++) {
			op.weights.set(i, V[i+1]);
		}

		op.refresh();
		cache.refreshCache();
		return moved;
	}

	private double getValue(double[] V, ComplexCriteria c, Criteria root) {
		ComplexOperator op = c.operator;
		op.lambda = V[0];
		for(int i = 0; i < op.weights.size(); i++) {
			op.weights.set(i, V[i + 1]);
		}
		op.refresh();
		return cache.check();
	}

	public void criteria(ComplexCriteria c) {
		int STEP = 5;
		ComplexOperator op = c.operator;

		double[][] V = new double[2][c.getSize()+1];
		double[] h = new double[c.getSize()+1];
		int step = 0;
		int k = 0;

		V[0][0] = c.operator.lambda;
		V[1][0] = c.operator.lambda;

		for(int i = 0; i < op.weights.size(); i++) {
			V[1][i+1] = V[0][i+1] = op.weights.get(i);
		}
		// XXX step
		for(int i = 0; i < h.length; i++) {
			h[i] = 1E-3;
		}

		k = 0;
		step = 1;
		double f, fn;
		while (true) {
			while ( !searchPoint(V[step], h, c) ) {
				for(int j = 0; j < h.length; j++) {
					h[j] /= 2;
				}
				if (++k > STEP) {
					return;
				}
			}

			f = getValue(V[step], c, root);
			do {
				if (++k > STEP) {
					return;
				}
				step = (step + 1) % 2;
				for(int j=0; j < V.length; j++) {
					V[step][j] = V[step][j] + 2*(V[(step+1)%2][j] - V[step][j]);
				}
				fn = getValue(V[step], c, root);

			} while ( fn < f );

			for(int j=0; j < V[step].length; j++) {
				V[step][j] = V[(step + 1) %2][j];
			}
		}
	}

	// Deep Optimization
	public void rec(Criteria c) {
		if(c instanceof ComplexCriteria) {
			criteria((ComplexCriteria) c);
			for(Criteria child : ((ComplexCriteria) c).children) {
				rec(child);
			}
		}
	}

	// Long optimization
	public void iteration() {
		Queue<Criteria> queue = new LinkedList<Criteria>();
		queue.offer(root);
		Criteria c;
		while((c = queue.poll()) != null) {
			if(c instanceof ComplexCriteria) {
				criteria((ComplexCriteria) c);
				for(Criteria child : ((ComplexCriteria) c).children) {
					queue.offer(child);
				}
			}
		}
	}

	public void optimize(Criteria root, Criteria base, double[][] F) {
		this.root = root;
		cache = new CacheCriteria(root, base,  F);

		for(int i=0; i < 400; i++) {
			iteration();
		}
	}
}
