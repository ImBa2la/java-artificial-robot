package org.styskin.greed;

import static java.lang.Math.log;
import static java.lang.Math.max;
import static java.lang.Math.min;
import static org.styskin.greed.MatrixUtils.add;
import static org.styskin.greed.MatrixUtils.correlation;
import static org.styskin.greed.MatrixUtils.leastSqares;
import static org.styskin.greed.MatrixUtils.mult;
import static org.styskin.greed.MatrixUtils.tr;
import it.unimi.dsi.fastutil.doubles.DoubleArrayList;
import it.unimi.dsi.fastutil.doubles.DoubleList;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntCollections;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.ints.IntLists;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.styskin.util.LoadInput;
import org.styskin.util.PairDI;

import Jama.Matrix;


public class Formula {
	
	private static final double EPS = 1E-5;
	
	List<Monom> monoms = new ArrayList<Monom>();
	DoubleList weight = new DoubleArrayList();
	
	IntSet bannedMonoms = new IntOpenHashSet();
	
	public Formula() {
		monoms.add(new Monom(0));
		weight.add(1d);
	}
	
	static class Monom {
		IntList list = new IntArrayList();
		int uniqSize = 0;

		Monom() {}
		
		public Monom(int... inds) {
			for(int ind : inds)
				list.add(ind);
		}

		public double result(double[] row) {
			double r = 1;
			for(int i=0; i < list.size(); i++)
				r *= row[list.getInt(i)];
			return r;
		}
		
		Monom copy() {
			Monom m = new Monom();
			m.list.addAll(this.list);
			m.uniqSize = this.uniqSize;
			return m;
		}
		
		void add(int index) {
			list.add(index);
			Collections.sort(list);
		}
		
		void addFactor(int index, List<Monom> out) {
			if(uniqSize <= 4){
				Monom m = copy();
				m.uniqSize ++;
				for(int i=0; i < m.list.size(); i++) {
					if(m.list.getInt(i) == index) {
						m.uniqSize --;
						break;
					}
				}
				m.add(index);
				out.add(m);

			}
			if(list.size() > 1) {
				for(int i=0; i < list.size(); i++) {
					Monom m = copy();
					m.list.set(i, index);
					out.add(m);
				}
			}		
		}
		
		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			for(int i=0; i < list.size(); i++) {
				sb.append("*").append("[").append(list.get(i)).append("]");
			}
			return sb.toString();
		}
		
		public String toString(LoadInput in) {
			StringBuilder sb = new StringBuilder();
			for(int i=0; i < list.size(); i++) {
				sb.append("*").append("[").append(in.getName(list.get(i))).append("]");
			}
			return sb.toString();
		}

		public int code() {
			int c = list.getInt(0);
			for(int i=1, j = 0; i < list.size(); i++) {
				if(list.getInt(i) != list.getInt(i-1)) {
					j ++;
					c ^= list.getInt(i) << (i*8); 
				}				
			}
			return c;
		}
		
		
	}
	
	public double[] result(double[][] M) {
		double[] res = new double[M.length];
		for(int i=0; i < M.length; i++) {
			for(int j=0; j < monoms.size(); j++)
				res[i] += weight.get(j) * monoms.get(j).result(M[i]);
		}
		return res;
	}
	
	public double check(LoadInput in) {
		return leastSqares(result(in.M), in.B);		
	}
	
	public double checkCorrelation(LoadInput in) {
		return correlation(result(in.M), in.B);		
	}

	
	public double dcg(LoadInput in) {
		List<UrlQueryScore> list = new ArrayList<UrlQueryScore>();
		double[] r = result(in.M);
		for(int i=0; i < r.length; i++)
			list.add(new UrlQueryScore(in.Q[i], r[i], in.B[i]));
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
	
	public double pfound(LoadInput in) {
		List<UrlQueryScore> list = new ArrayList<UrlQueryScore>();
		double[] r = result(in.M);
		for(int i=0; i < r.length; i++)
			list.add(new UrlQueryScore(in.Q[i], r[i], in.B[i]));
		Collections.sort(list, new UrlQueryScoreComparator());
		double pfound = 0;
		int N = -1;
		int cq = -1;
		double pCont = 1.0;
		for(int i=0; i < r.length; i++) {
			UrlQueryScore t = list.get(i);
			if(t.q != cq) {
				cq = t.q;
				++ N;
				pCont = 1.0;
			}
			pfound += t.real*pCont;
			pCont *= 0.85*(1.0 - t.real);
		}		
		return pfound/N;		
	}	
	
	public boolean addMonoms(List<Monom> newMonoms, double[] Y, double[] B, double[][] A) {
		List<PairDI> r = new ArrayList<PairDI>();
		for(int i=0; i < newMonoms.size(); i++) {
			Monom m = newMonoms.get(i);
			if(!bannedMonoms.contains(m.code())) {
				double[] t = new double[A.length];
				for(int j=0; j < A.length; j++)
					t[j] = m.result(A[j]);
				r.add(new PairDI(-correlation(t, Y), i));
			}
		}
		Collections.sort(r);
		//======
		
		int N = Y.length;
		int C = 0;
		for(int i=0; i < r.size() && C < 5; i++) {
			double cr = 0;
			double[] ta = calcVector(newMonoms.get(r.get(i).second), A);
			for(int j=0; j < monoms.size(); j++)
				cr = max(cr, correlation(ta, calcVector(monoms.get(j), A)));
			if(cr < 0.97) {
				monoms.add(newMonoms.get(r.get(i).second));
				++ C;
//					System.out.printf("Correlation: %f\t%f\t%s\n", cr, r.get(i).first, newMonoms.get(r.get(i).second));
			} else {
				bannedMonoms.add(newMonoms.get(r.get(i).second).code());
//					System.out.printf("Huge correlation: %f\t%s\n", cr, newMonoms.get(r.get(i).second));
			}
		}		
		double[][] X = new double[monoms.size()][N];
		for(int i=0; i < monoms.size(); i++)
			for(int j=0; j < N; j++)
				X[i][j] = monoms.get(i).result(A[j]);
		// Regression for X -> Y
		Matrix a = new Matrix(X); 
		Matrix b = new Matrix(new double[][] {B});
		Matrix x = a.transpose().solve(b.transpose());
		weight.clear();
		for(int i=0; i < monoms.size(); i++) {
			weight.add(x.get(i, 0));
		}
		return true; // true - if added
	}
	
	public boolean addMonomsIncremental(List<Monom> newMonoms, double[] Y, double[] B, double[][] A, LoadInput input) {
		List<PairDI> r = new ArrayList<PairDI>();
		for(int i=0; i < newMonoms.size(); i++) {
			Monom m = newMonoms.get(i);
			double[] t = new double[A.length];
			for(int j=0; j < A.length; j++)
				t[j] = m.result(A[j]);
			r.add(new PairDI(-correlation(t, Y), i));
		}
		Collections.sort(r);
		//======
		
		List<Monom> monomsForAdd = new ArrayList<Monom>();  
		int N = Y.length;
		int C = 0;
		for(int i=0; i < r.size() && C < 3; i++) {
			double cr = 0;
			double[] ta = calcVector(newMonoms.get(r.get(i).second), A);
			for(int j=0; j < monoms.size(); j++)
				cr = max(cr, correlation(ta, calcVector(monoms.get(j), A)));
			for(int j=0; j < monomsForAdd.size(); j++)
				cr = max(cr, correlation(ta, calcVector(monomsForAdd.get(j), A)));
			
			if(cr < 0.97) {
				monomsForAdd.add(newMonoms.get(r.get(i).second));
				++ C;
//					System.out.printf("Correlation: %f\t%f\t%s\n", cr, r.get(i).first, newMonoms.get(r.get(i).second));
			} else {
//					System.out.printf("Huge correlation: %f\t%s\n", cr, newMonoms.get(r.get(i).second));
			}
		}		
		double[][] X = new double[monomsForAdd.size()][N];
		for(int i=0; i < monomsForAdd.size(); i++)
			for(int j=0; j < N; j++)
				X[i][j] = monomsForAdd.get(i).result(A[j]);
		// Regression for X -> Y
		Matrix a = new Matrix(X); 
		Matrix b = new Matrix(new double[][] {Y});
		Matrix x = a.transpose().solve(b.transpose());
		for(int i=0; i < monomsForAdd.size(); i++) {
			monoms.add(monomsForAdd.get(i));
			weight.add(x.get(i, 0));
		}

		
		// Optimize
		Optimizer optimizer = new OptimizeDcg();
		optimizer.optimize(this, input);
		
		return true; // true - if added
	}
	
	
	
	private static double[] calcVector(Monom m, double[][] A) {
		double[] ta = new double[A.length];
		for(int l=0; l < A.length; l++)
			ta[l] = m.result(A[l]);
		return ta;
	}
	
	public boolean iteration(LoadInput input) {
		double[] Y = result(input.M);
		mult(Y, -1);
		add(Y, input.B);

		double[][] t = tr(input.M);
		List<PairDI> list = new ArrayList<PairDI>();		
		for(int i=0; i < t.length; i++)
			list.add(new PairDI(-correlation(t[i], Y), i));
		Collections.sort(list);

		List<Monom> newMonoms = new ArrayList<Monom>();
		for(int i=0; i < min(10, list.size()); i++) {
			newMonoms.add(new Monom(list.get(i).second));
			for(Monom m : this.monoms) {
				m.addFactor(list.get(i).second, newMonoms);
			}
		}
		addMonoms(newMonoms, Y, input.B, input.M);
//		addMonomsIncremental(newMonoms, Y, input.B, input.M, input);
//		System.out.printf("Optimisation: %s\t%f\n", this, correlation(result(A), B));		
		return true;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for(int i=0; i < weight.size(); i++) {
			sb.append(weight.get(i)).append(monoms.get(i)).append("\n");
		}
		return sb.toString();
	}
	
	public String toString(LoadInput in) {
		StringBuilder sb = new StringBuilder();
		for(int i=0; i < weight.size(); i++) {
			sb.append(weight.get(i)).append(monoms.get(i).toString(in)).append("\n");
		}
		return sb.toString();
	}

}
