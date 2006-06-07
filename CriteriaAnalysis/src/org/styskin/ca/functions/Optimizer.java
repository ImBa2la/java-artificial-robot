/*
 *$Id$
 */
package org.styskin.ca.functions;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.apache.log4j.Logger;
import org.styskin.ca.functions.complex.ComplexOperator;
import org.styskin.ca.model.Constants;
import org.styskin.ca.model.Pair;

public class Optimizer implements Constants {

	static Logger logger = Logger.getLogger(Optimizer.class);

	private CacheCriteria cache;

	private Criteria root;

	private List<ComplexOperator> operators;

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

	private final static double VEPS = 1E-1;

	private double getValue(double[] V, ComplexCriteria c, Criteria root) {
		ComplexOperator op = c.operator;
		for(int i=0; i < V.length; i++) {
			if(V[i] < VEPS || V[i] > 1-VEPS) {
				return 1E6;
			}
		}

		op.lambda = V[0];
		for(int i = 0; i < op.weights.size(); i++) {
			op.weights.set(i, V[i + 1]);
		}
		op.refresh();
		return cache.check();
	}


	// TODO Constants!!!
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
		Queue<Pair<Criteria, Integer>> queue = new LinkedList<Pair<Criteria, Integer>>();
		queue.offer(new Pair<Criteria, Integer>(root, 1));
		Pair<Criteria, Integer> c;
		ComplexCriteria cc;
		while((c = queue.poll()) != null) {
			if(c.getFirst() instanceof ComplexCriteria) {
				cc = (ComplexCriteria) c.getFirst();
//				logger("%d, ", c.getSecond());

				if (c.getSecond() < 3) {
					criteria(cc);
				} else {
					// TODO clone
					ComplexOperator src = cc.operator;
					ComplexOperator minOperator = src, op = null;
					double min = cache.check(), tempCheck;

					for(ComplexOperator operator : operators) {
						try {
							op = operator.clone();
							op.lambda = src.lambda;
							op.weights = new ArrayList<Double>();
							op.weights.addAll(src.weights);
							op.refresh();
						} catch(CloneNotSupportedException ex) {
							ex.printStackTrace();
						}
						cc.operator = op;
						criteria(cc);
						tempCheck = cache.check();
						if (tempCheck < min) {
							min = tempCheck;
							minOperator = op;
						}
					}

					cc.operator = minOperator;
					cache.turnOffCache(cc);
					cache.refreshCache();
				}
				for(Criteria child : cc.children) {
					queue.offer(new Pair<Criteria, Integer>(child, c.getSecond() + 1));
				}
			}
		}
	}

	private void optimize(Criteria root) {
		operators = new ArrayList<ComplexOperator>();
		try {
			for(Class clazz : ComplexOperator.complexOperators) {
				operators.add((ComplexOperator) clazz.newInstance());
			}
		} catch(Exception ex) {
			ex.printStackTrace();
		}

		for(int i=0; i < 50; i++) {
			iteration();
			logger.debug(cache.check());
//			System.out.printf("\nIteration #%d\nCheck = %4.4f\n%s\n", i, cache.check(), root);
		}

	}

	public void optimize(Criteria root, double[] base, double[][] F) {
		this.root = root;
		cache = new CacheCriteria(root, base,  F);
		optimize(root);
	}

	public void optimize(Criteria root, Criteria base, double[][] F) {
		this.root = root;
		cache = new CacheCriteria(root, base,  F);
		optimize(root);
	}
}
