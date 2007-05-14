package org.styskin.ca.functions;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.styskin.ca.model.Pair;

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
		
		public boolean isAlive() {
			return thread.isAlive();
		}	
		
	}
	
	public static class Merger {
		
		// TODO: hack with merging
		public Pair<Criteria,Criteria> merge(Criteria c1, Criteria c2) {
			int size = c1.getChildren().size();
			int index = (int)Math.random()*size;
			Criteria new1;
			Criteria new2;				
			try {
				new1 = c1.clone();
				new2 = c2.clone();				
				Criteria child1 = new1.getChildren().get(index);
				Criteria child2 = new2.getChildren().get(index);				
				new1.getChildren().set(index, child2);
				new2.getChildren().set(index, child1);				
				return Pair.makePair(new1, new2);
			} catch (CloneNotSupportedException e) {
				logger.error(e);
			}
			return null;
		}
	}
	
	private static final Merger merger = new Merger(); 		


	public MultiOptimizer(Criteria criteria) {
		root = criteria;		
	}
	
	private static final int THREAD_COUNT = 2;
	private static final long SLEEP_TIMEOUT = 10000l;
	
	public Criteria optimize(double[] base, double[][] F) {
		LinkedList<Checker> pool = new LinkedList<Checker>();
		Checker best = null;
		double value = 1E10;
		try {
			Criteria rootEq = root.cloneEquals();
			pool.add(new Checker(root, base, F));
			pool.add(new Checker(rootEq, base, F));

			Map<Integer, Checker> q = new TreeMap<Integer, Checker>();
			int Q = 0;			
			Thread.sleep(5000);			
			while(pool.size() > 0) {
				Iterator<Checker> it = pool.iterator();
				q.clear();
				while(it.hasNext()) {
					Checker c = it.next();
					q.put((int)Math.random()*100000, c);				
					
					if(!c.isAlive()) {
						if(c.strongValue() < value) {
							value = c.strongValue();
							best = c;
						}
						it.remove();
					} else if(c.aproximateValue() > value) {
						c.stop();
						it.remove();
					} else if(c.strongValue() < value) {
						value = c.strongValue();
						best = c;
					}
				}
				if(Q ++ < THREAD_COUNT) {
					Iterator<Map.Entry<Integer, Checker>> ite = q.entrySet().iterator();
					Criteria c1 = ite.next().getValue().getCriteria();
					Criteria c2 = best.getCriteria();//ite.next().getValue().getCriteria();
					Pair<Criteria, Criteria> pair = merger.merge(c1, c2);					
					pool.add(new Checker(pair.getFirst(), base, F));
					pool.add(new Checker(pair.getSecond(), base, F));
				}
				Thread.sleep(SLEEP_TIMEOUT);
			}
		} catch (InstantiationException ex) {
			logger.error(ex);
		} catch (IllegalAccessException ex) {
			logger.error(ex);
		} catch (CloneNotSupportedException ex) {
			logger.error(ex);
		} catch (InterruptedException ex) {
			logger.error(ex);
		}		
		logger.info(best.strongValue());
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
