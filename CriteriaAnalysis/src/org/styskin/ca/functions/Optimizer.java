/*
 *$Id$
 */
package org.styskin.ca.functions;

import org.styskin.ca.model.Constants;

public class Optimizer implements Constants {

	private boolean searchPoint(double[] V, double[] h, ComplexCriteria c, Criteria root) {
		boolean moved = false;

		ComplexOperator op = c.operator;

		double f = root.check();
		op.lambda += h[0];
		op.refresh();
		if (root.check() < f) {
			V[0] += h[0];
			moved = true;
		} else {
			op.lambda -= 2*h[0];
			op.refresh();
			if (root.check() < f) {
				V[0] -= h[0];
				moved = true;
			}
		}

		for(int i = 1; i < V.length; i++) {
			op.weights.set(i-1, V[i] + h[i]);
			op.refresh();
			if (root.check() < f) {
				V[i] += h[i];
				moved = true;
			} else {
				op.weights.set(i-1, V[i] - h[i]);
				op.refresh();
				if (root.check() < f) {
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
		return moved;
	}

	private double getValue(double[] V, ComplexCriteria c, Criteria root) {
		ComplexOperator op = c.operator;
		op.lambda = V[0];
		for(int i = 0; i < op.weights.size(); i++) {
			op.weights.set(i, V[i + 1]);
		}
		op.refresh();
		return root.check();
	}

	public void criteria(ComplexCriteria c, Criteria root) {
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
			while ( !searchPoint(V[step], h, c, root) ) {
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

	public void iteration(Criteria c, Criteria root) {
		if(c instanceof ComplexCriteria) {
			criteria((ComplexCriteria) c, root);
			for(Criteria child : ((ComplexCriteria) c).children) {
				iteration(child,root);
			}
		}
	}

	public void optimize(Criteria root) {
		for(int i=0; i < 100; i++) {
			iteration(root, root);
		}
	}
}
