package org.styskin.ca.functions;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.styskin.ca.math.SimpleMathUtils;
import org.styskin.ca.model.CriteriaXMLParser;
import org.styskin.ca.model.Pair;
import org.styskin.ca.model.CriteriaXMLParser.Optimize;

import ru.yandex.utils.Triple;

public class MultiOptimizer implements Optimizer {
	
	private static final Logger logger = Logger.getLogger(MultiOptimizer.class);
	
	private Criteria root;
	
	private int CHECKER = 0;
	
	class Checker implements Runnable {
		
		private Criteria root;
		private Thread thread;
		private Optimize op;
		
		private int index;
		
		private SingleOptimizer optimizer;
		
		public Checker(Criteria root, Optimize op) {
//				this.root = root.clone();
			this.root = root;
			this.op = op;
			
			index = CHECKER ++;
			
			thread = new Thread(this);
			thread.start();			
		}

		public void run() {
			optimizer = new SingleOptimizer(root);
			try {
				root = optimizer.optimize(op);
			} catch(Exception ex) {
				logger.error("Cannot optimize", ex);
			}
		}

		public void stop() {
			optimizer.stop();			
		}

		public double strongValue() throws Exception {
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

		public int getIndex() {
			return index;
		}
		
	}
	
	public static class Merger {
		
		
		static class CriteriaEx {
			private IdentityHashMap<Criteria, Pair<Integer, ComplexCriteria>> parentMap = new IdentityHashMap<Criteria, Pair<Integer,ComplexCriteria>>();
			private List<Criteria> list = new ArrayList<Criteria>();
			private List<Double> probability = new ArrayList<Double>();
			private int maxLevel;

			public CriteriaEx(Criteria cr) {
				this(cr, 1);
			}

			public CriteriaEx(Criteria cr, int maxLevel) {
				this.maxLevel = maxLevel;
				addCriteria(cr);
			}
			

			private void addCriteria(Criteria cr) {
				addCriteria(cr, 0, 1);
			}

			private void addCriteria(Criteria cr, int level, double p) {
				if(level > 0 && cr instanceof ComplexCriteria) {
					list.add(cr);
					probability.add(p);
				}
				if(cr instanceof ComplexCriteria && level < maxLevel) {
					ComplexCriteria cc = (ComplexCriteria) cr;
					for(int i=0; i < cc.getSize(); ++i) {
						parentMap.put(cc.getChildren().get(i), new Pair<Integer, ComplexCriteria>(i, cc));
						addCriteria(cc.getChildren().get(i), level + 1, p/2);
					}
				}
			}
			
			public Triple<ComplexCriteria, Integer, Criteria> getRandom(double r) {
				double sum = 0;
				double[] pr = new double[probability.size()];
				for(int i=0; i < probability.size(); ++i) {
					pr[i] = probability.get(i);
					sum += probability.get(i);
				}
				pr[0] = pr[0]/sum;
				for(int i=1; i < pr.length; ++i)
					pr[i] = pr[i]/sum + pr[i-1];
				int ind = SimpleMathUtils.findIndex(pr, r) + 1;
				Criteria c = list.get(ind);
				Pair<Integer, ComplexCriteria> pair = parentMap.get(c);
				return new Triple<ComplexCriteria, Integer, Criteria>(pair.getSecond(), pair.getFirst(), c);
			}
			
		}
		
		public Pair<Criteria,Criteria> merge(Criteria c1, Criteria c2) throws Exception {
			Criteria new1 = c1.clone();
			Criteria new2 = c2.clone();				
			CriteriaEx e1 = new CriteriaEx(new1);
			CriteriaEx e2 = new CriteriaEx(new2);
			double r = Math.random();
			Triple<ComplexCriteria, Integer, Criteria> t1 = e1.getRandom(r);
			Triple<ComplexCriteria, Integer, Criteria> t2 = e2.getRandom(r);
			assert !t1.getSecond().equals(t2.getSecond());
			t1.getFirst().getChildren().set(t1.getSecond(), t2.getThird());
			t2.getFirst().getChildren().set(t2.getSecond(), t1.getThird());
			return Pair.makePair(new1, new2);
		}
	}
	
	private final Merger merger = new Merger(); 		

	public MultiOptimizer(Criteria criteria) {
		root = criteria;		
	}
	
	private static final int THREAD_COUNT = 10;
	private static final long SLEEP_TIMEOUT = 3000l; // 10 sec
	
	public Criteria optimize(Optimize op) throws Exception {
		LinkedList<Checker> pool = new LinkedList<Checker>();
		Criteria best = null;
		double value = 1E10;
		try {
			Criteria rootEq = root.cloneEquals();
			pool.add(new Checker(root.clone(), op));
			pool.add(new Checker(rootEq, op));

			Map<Integer, Checker> q = new TreeMap<Integer, Checker>();
			int Q = 0;
			Thread.sleep(SLEEP_TIMEOUT);
			while(!pool.isEmpty()) {
				Iterator<Checker> it = pool.iterator();
				q.clear();
				while(it.hasNext()) {
					Checker c = it.next();
					q.put((int)(Math.random()*100000), c);				
					
					if(!c.isAlive()) {
						if(c.strongValue() < value) {
							value = c.strongValue();
							// FIXME: clone is hack
							best = c.getCriteria().clone();
						}
						it.remove();
					} else if(c.aproximateValue() > value) {
						c.stop();
						it.remove();
					} else if(c.strongValue() < value) {
						value = c.strongValue();
						best = c.getCriteria().clone();
					}
				}
				logger.info("Best value: " + value);
				if(Q ++ < THREAD_COUNT) {
					Iterator<Map.Entry<Integer, Checker>> ite = q.entrySet().iterator();
					Criteria c1 = ite.next().getValue().getCriteria();
					if(!ite.hasNext()) {
						logger.warn("Strange, very strange");
						continue;
					}
//					Criteria c2 = best.getCriteria();
					Criteria c2 = ite.next().getValue().getCriteria();
					Pair<Criteria, Criteria> pair = merger.merge(c1, c2);					
					pool.add(new Checker(pair.getFirst(), op));
					pool.add(new Checker(pair.getSecond(), op));
					logger.info("Two criterias were added");
				}
				Thread.sleep(SLEEP_TIMEOUT);
			}
		} catch (Exception ex) {
			logger.error("What the fuck?", ex);
		}		
//		logger.info("Strong value: " + best.strongValue());
//		logger.info("Strong trace: " + best.getTrace());
//		logger.info("Strong trace: " + best.getIndex());
		return best;
	}

	public Criteria optimize(Criteria base, double[][] F) throws Exception {
		double[] b = new double[F.length];
		for(int i=0; i< F.length; i++) {
			b[i] = root.getValues(F[i]);
		}
		Optimize op = new Optimize(F, b);
		return optimize(op);
	}
	
}
