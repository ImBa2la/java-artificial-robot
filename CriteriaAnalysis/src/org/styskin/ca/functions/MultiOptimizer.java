package org.styskin.ca.functions;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

public class MultiOptimizer {
	
	private static final Logger logger = Logger.getLogger(MultiOptimizer.class);
	
	private Criteria root;
	
	class Checker implements Runnable {
		
		private Criteria root;
		private Thread thread;
		private double[] base;
		private double[][] F;

		private Optimizer optimizer;
		
		public Checker(Criteria root, double[] base, double[][] F) {
			try {
				this.root = root.clone();
			} catch(CloneNotSupportedException ex) {
				logger.error(ex);
			}
			this.base = base;
			this.F = F;	
			thread = new Thread(this);
			thread.start();			
		}

		public void run() {
			optimizer = new Optimizer(root);
			optimizer.optimize(base, F);
		}

		public void stop() {
			optimizer.stop();			
		}

		public double strongValue() {			
			return optimizer.getValue();
		}

		public double aproximateValue() {
			return optimizer.getApproximateValue();
		}

		public Criteria getCriteria() {
			return root;
		}
		public List<Double> getTrace() {
			return optimizer.getTrace();
		}
		
		public Thread getThread() {
			return thread;
		}
	}


	public MultiOptimizer(Criteria criteria) {
		root = criteria;		
	}
	
	public Criteria optimize(double[] base, double[][] F) {
		LinkedList<Checker> pool = new LinkedList<Checker>();
		Checker best = null;
		double value = 1E10;
		try {
			// FIXME: обходим и чуть-чуть меняем точки => создаем множество исходных точек
			
			pool.add(new Checker(root, base, F));			
			while(pool.size() > 0) {
				Iterator<Checker> it = pool.iterator();
				while(it.hasNext()) {
					Checker c = it.next();
					if(!c.getThread().isAlive()) {
						if(c.strongValue() < value) {
							value = c.strongValue();
							best = c;
						}
						it.remove();
					}
				}
				Thread.sleep(1000);
			}
		} catch(InterruptedException ex) {
			logger.error(ex);
		}		
		logger.info(value);
		logger.info(best.getTrace());
		return best.getCriteria();
	}

	public Criteria optimize(Criteria base, double[][] F) {
		double[] b = new double[F.length];
		try {
			for(int i=0; i< F.length; i++) {
				b[i] = root.getValues(F[i]);
			}
		} catch(Exception e) {
			logger.error(e);			
		}
		return optimize(b, F);
	}
	
}
