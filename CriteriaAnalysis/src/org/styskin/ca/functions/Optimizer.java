/*
 *$Id$
 */
package org.styskin.ca.functions;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.apache.log4j.Logger;
import org.styskin.ca.functions.complex.ComplexHOperator;
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
		int SW = c.getSize();
		int S = c.getSize() + c.getOperator().getParameters().size();
		

		ComplexOperator op = c.getOperator();
		cache.turnOffCache(c);
		double f = cache.check();
		for(int i = 0; i < SW; i++) {
			op.getWeights().set(i, V[i] + h[i]);
			op.refresh();
			if (cache.check() < f) {
				V[i] += h[i];
				moved = true;
			} else {
				op.getWeights().set(i, V[i] - h[i]);
				op.refresh();
				if (cache.check() < f) {
					V[i] -= h[i];
					moved = true;
				}
			}
		}
		// Lambda optimization
		for(int i=SW; i < S; i++) {
			op.getParameters().set(i - SW, V[i] + h[i]);
			op.refresh();
			if (cache.check() < f) {
				V[i] += h[i];
				moved = true;
			} else {
				op.getParameters().set(i - SW, V[i] - h[i]);
				op.refresh();
				if (cache.check() < f) {
					V[i] -= h[i];
					moved = true;
				}				
			}
		}
		for(int i = 0; i < SW; i++) {
			op.getWeights().set(i, V[i]);
		}
		for(int i = SW; i < S; i++) {
			op.getParameters().set(i - SW, V[i]);						
		}
		op.refresh();
		cache.refreshCache();
		return moved;
	}

	private final static double VEPS = 1E-1;

	private double getValue(double[] V, ComplexCriteria c, Criteria root) {
		ComplexOperator op = c.getOperator();
		int SW = c.getSize(); 
		int S = SW + c.getOperator().getParameters().size();
		
		for(int i=0; i < V.length; i++) {
			if(V[i] < VEPS || V[i] > 1-VEPS) {
				return 1E6;
			}
		}
		for(int i = 0; i < SW; i++) {
			op.getWeights().set(i, V[i]);
		}
		for(int i = SW; i < S; i++) {
			op.getParameters().set(i - SW, V[i]);						
		}
		op.refresh();
		return cache.check();
	}

	// TODO : Modify algorithm

	private final static int STEP = 5;
	private final int LEVEL = 3;

	public void criteria(ComplexCriteria c) {
		ComplexOperator op = c.getOperator();
		
		int SW = c.getSize(); 
		int S = SW + c.getOperator().getParameters().size();

		double[][] V = new double[2][S];
		double[] h = new double[S];
		int step = 0;
		int k = 0;

		for(int i = 0; i < SW; i++) {
			V[1][i] = V[0][i] = op.getWeights().get(i);
		}
		for(int i = SW; i < S; i++) {			
			V[1][i] = V[0][i] = op.getParameters().get(i - SW);
		}
		// TODO: modify
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
			for(Criteria child : c.getChildren()) {
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
				if (c.getSecond() < LEVEL) {
					criteria(cc);
				} else {
					ComplexOperator src = cc.getOperator();
					ComplexOperator minOperator = src, op = null;
					double min = cache.check(), tempCheck;
					for(ComplexOperator operator : operators) {
						if(op instanceof ComplexHOperator) {
							try {
								op = operator.clone();
								List<Double> w = new ArrayList<Double>();
								w.addAll(src.getWeights());
								op.setWeights(w);
								for(int i=0; i < src.getParameters().size(); i++) {
									op.getParameters().set(i, src.getParameters().get(i));
								}
								op.refresh();
							} catch(CloneNotSupportedException ex) {
								logger.error("Operator clone exception");
							}
						}
						cc.setOperator(op);
						criteria(cc);
						tempCheck = cache.check();
						if (tempCheck < min) {
							min = tempCheck;
							minOperator = op;
						}
					}
					cc.setOperator(minOperator);
					cache.turnOffCache(cc);
					cache.refreshCache();
				}
				for(Criteria child : cc.getChildren()) {
					queue.offer(new Pair<Criteria, Integer>(child, c.getSecond() + 1));
				}
			}
		}
	}

	private void optimize(Criteria root) {
		operators = new ArrayList<ComplexOperator>();
		try {
			for(Class<? extends ComplexOperator> clazz : ComplexOperator.complexOperators) {
				operators.add(clazz.newInstance());
			}
		} catch(Exception ex) {
			logger.error("Cannot pre-create operator");
		}
		// TODO criteria of finish optimization
		for(int i=0; i < 100; i++) {
			iteration();
//			logger.info(cache.check());
//			System.out.printf("\nIteration #%d\nCheck = %4.4f\n%s\n", i, cache.check(), root);
			System.out.printf("%4.4f\n", cache.check());
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
		cache.checkOut();
	}

	public static double[][] getMatrix(int size, int length) {
		double[] CASES = {0.2, 0.6, 0.8};
		int VAR_NUMBER = size;
		long CASE_NUMBER = Math.round(Math.pow(CASES.length, VAR_NUMBER));

		long step = 1; //CASE_NUMBER / length - 10;
		double[][] F = new double[length][VAR_NUMBER];

		int iF = 0;
		for(long i=0; i < CASE_NUMBER; i+= step /* + Math.round(20*Math.random())*/) {
			if (iF >= length) {
				break;
			}
			long mod = (CASE_NUMBER/CASES.length);
			for(int j=0; j < VAR_NUMBER; j++) {
				F[iF][j] = CASES[(int)((i / mod) %3)];
				mod /= 3;
			}
			iF++;
		}
		return F;
	}
}
