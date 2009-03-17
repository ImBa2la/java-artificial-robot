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
import org.styskin.ca.model.CriteriaXMLParser.Optimize;

public class SingleOptimizer implements Constants, Optimizer {

	private static final int MAX_ITERATIONS = 30;

	private static final Logger logger = Logger.getLogger(SingleOptimizer.class);

	private CacheCriteria cache;

	private Criteria root;

	private List<ComplexOperator> operators;

	private boolean stopFlag = false;
	private List<Double> trace;

	private boolean searchPoint(double[] V, double[] h, ComplexCriteria c) throws Exception {
		boolean moved = false;
		int SW = c.getSize();
		int S = c.getSize() + c.getOperator().getParameters().size();

		ComplexOperator op = c.getOperator();
		cache.turnOffCache(c);
		double f = cache.check();
		for (int i = 0; i < SW; i++) {
			op.setWeight(i, V[i] + h[i]);
			op.refresh();
			if (cache.check() < f) {
				V[i] += h[i];
				moved = true;
			} else {
				op.setWeight(i, V[i] - h[i]);
				op.refresh();
				if (cache.check() < f) {
					V[i] -= h[i];
					moved = true;
				}
			}
		}
		for (int i = SW; i < S; i++) {
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
		for (int i = 0; i < SW; i++) {
			op.getWeights().set(i, V[i]);
		}
		for (int i = SW; i < S; i++) {
			op.getParameters().set(i - SW, V[i]);
		}
		op.refresh();
		cache.refreshCache();
		return moved;
	}

	// TODO: define constant
	private static final double VEPS = 1E-1;
	private static final double EDGE = 1E6;

	private double getValue(double[] V, ComplexCriteria c, Criteria root) throws Exception {
		ComplexOperator op = c.getOperator();
		int SW = c.getSize();
		int S = SW + c.getOperator().getParameters().size();

		for (int i = 0; i < SW; i++) {
			if (V[i] < VEPS || V[i] > 1 - VEPS) {
				return EDGE;
			}
		}
		for (int i = SW; i < S; i++) {
			if (V[i] < op.getParameters().getLowerBound(i - SW)
					|| V[i] > op.getParameters().getUpperBound(i - SW)) {
				return 1E6;
			}
		}
		for (int i = 0; i < SW; i++) {
			op.setWeight(i, V[i]);
		}
		for (int i = SW; i < S; i++) {
			op.getParameters().set(i - SW, V[i]);
		}
		op.refresh();
		return cache.check();
	}

	// TODO : Modify algorithm
	private final static int STEP_COUNT = 3;
	private final static double START_STEP = 1E-2;
	private final static int LEVEL = 100;

	public void criteria(ComplexCriteria c) throws Exception {
		ComplexOperator op = c.getOperator();

		int SW = c.getSize();
		int S = SW + c.getOperator().getParameters().size();

		double[][] V = new double[2][S];
		double[] h = new double[S];
		int step = 0;
		int k = 0;

		for (int i = 0; i < SW; i++) {
			V[1][i] = V[0][i] = op.getWeights().get(i);
		}
		for (int i = SW; i < S; i++) {
			V[1][i] = V[0][i] = op.getParameters().get(i - SW);
		}
		for (int i = 0; i < h.length; i++) {
			// TODO: modify START_STEP(level)			
			h[i] = START_STEP;
		}
		k = 0;
		step = 1;
		double f, fn;
		while (true) {
			while (!searchPoint(V[step], h, c)) {
				for (int j = 0; j < h.length; j++) {
					h[j] /= 2;
				}
				if (++k > STEP_COUNT) {
					return;
				}
			}

			f = getValue(V[step], c, root);
			do {
				if (++k > STEP_COUNT) {
					return;
				}
				step = (step + 1) % 2;
				for (int j = 0; j < V.length; j++) {
					V[step][j] = V[step][j] + 2 * (V[(step + 1) % 2][j] - V[step][j]);
				}
				fn = getValue(V[step], c, root);
			} while (fn < f);

			for (int j = 0; j < V[step].length; j++) {
				V[step][j] = V[(step + 1) % 2][j];
			}
		}
	}

	// Deep Optimization
	public void rec(Criteria c) throws Exception {
		if (c instanceof ComplexCriteria) {
			criteria((ComplexCriteria) c);
			for (Criteria child : c.getChildren()) {
				rec(child);
			}
		}
	}
	
	private void operatorSelection(ComplexCriteria cc) throws Exception {
		 ComplexOperator src = cc.getOperator();
		 ComplexOperator minOperator = src, op = null;
		 double min = cache.check(), tempCheck;
		 for(ComplexOperator operator : operators) {
		 	if(operator instanceof ComplexHOperator) {
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

	// Long optimization
	public void iteration() throws Exception {
		Queue<Pair<Criteria, Integer>> queue = new LinkedList<Pair<Criteria, Integer>>();
		queue.offer(new Pair<Criteria, Integer>(root, 1));
		Pair<Criteria, Integer> c;
		ComplexCriteria cc;
		while ((c = queue.poll()) != null && !stopFlag) {
			if (c.getFirst() instanceof ComplexCriteria) {
				cc = (ComplexCriteria) c.getFirst();
				if (c.getSecond() < LEVEL)
					criteria(cc);
				else
					operatorSelection(cc);
				for (Criteria child : cc.getChildren()) {
					queue.offer(new Pair<Criteria, Integer>(child, c.getSecond() + 1));
				}
			}
			Thread.yield();
		}
	}

	private void optimize(Criteria root) throws Exception {
		operators = new ArrayList<ComplexOperator>();
		try {
			for (Class<? extends ComplexOperator> clazz : ComplexOperator.complexOperators) {
				operators.add(clazz.newInstance());
			}
		} catch (Exception ex) {
			logger.error("Cannot pre-create operator");
		}
		// XXX criteria of finish optimization
		double dt = 1E10, t = 0, tt = 0;
		double presision = Math.min(cache.check() / 20000, 0.01);
		t = cache.check();
		trace.add(t);
		for (int i = 0; !stopFlag && dt > presision && i < MAX_ITERATIONS; i++) {
			iteration();
			tt = t;
			t = cache.check();
			dt = Math.abs(t - tt);
			trace.add(t);
			logger.debug("Step = " + i + ", value = " + t + ", correlation = " + cache.checkCorrelation());
			Thread.yield();
		}
	}

	public SingleOptimizer(Criteria root) {
		this.root = root;
	}

	/* (non-Javadoc)
	 * @see org.styskin.ca.functions.Optimizer#optimize(double[], double[][])
	 */
	public Criteria optimize(Optimize op) throws Exception {
		cache = new CacheCriteria(root, op);
		trace = new ArrayList<Double>();
		optimize(root);
		return root;
	}

	public double getValue() throws Exception {
		return cache.check();
	}

	// FIXME: aproximate with exponent ???
	public double getApproximateValue() {
		// trace, 10%		
		return Double.NEGATIVE_INFINITY;
	}

	public List<Double> getTrace() {
		return trace;
	}

	CacheCriteria getCacheCriteria() {
		return cache;
	}

	public void stop() {
		stopFlag = true;
	}

}
