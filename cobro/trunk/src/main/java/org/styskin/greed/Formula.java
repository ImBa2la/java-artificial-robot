package org.styskin.greed;

import it.unimi.dsi.fastutil.doubles.DoubleArrayList;
import it.unimi.dsi.fastutil.doubles.DoubleList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.styskin.util.LoadInput;
import org.styskin.util.PairDI;

import ru.yandex.utils.Triple;

import Jama.Matrix;
import static org.styskin.greed.MatrixUtils.*;
import static java.lang.Math.*;


public class Formula {
	
	List<Monom> monoms = new ArrayList<Monom>();
	DoubleList weight = new DoubleArrayList();
	
	public Formula() {
		monoms.add(new Monom(0));
		weight.add(1d);
	}
	
	static class Monom {
		List<Integer> list = new ArrayList<Integer>();

		Monom() {}
		
		public Monom(int... inds) {
			for(int ind : inds)
				this.list.add(ind);
		}

		public double result(double[] row) {
			double r = 1;
			for(int i=0; i < list.size(); i++)
				r *= row[list.get(i)];
			return r;
		}
		
		Monom copy() {
			Monom m = new Monom();
			m.list.addAll(this.list);
			return m;
		}
		
		void addFactor(int index, List<Monom> out) {
			{
				Monom m = copy();
				m.list.add(index);
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
	
	public double dcg(LoadInput in) {
		List<T> list = new ArrayList<T>();
		double[] r = result(in.M);
		for(int i=0; i < r.length; i++)
			list.add(new T(in.Q[i], r[i], in.B[i]));
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
	
	public boolean addMonoms(List<Monom> newMonoms, double[] Y, double[] B, double[][] A) {
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
		
		int N = Y.length;
		if(true) {
			int C = 0;
			for(int i=0; i < r.size() && C < 3; i++) {
				double cr = 0;
				double[] ta = calcVector(newMonoms.get(r.get(i).second), A);
				for(int j=0; j < monoms.size(); j++)
					cr = max(cr, correlation(ta, calcVector(monoms.get(j), A)));
				if(cr < 0.97) {
					monoms.add(newMonoms.get(r.get(i).second));
					++ C;
//					System.out.printf("Correlation: %f\t%f\t%s\n", cr, r.get(i).first, newMonoms.get(r.get(i).second));
				} else {
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
		}
		return true; // true - if added
	}

	private static double[] calcVector(Monom m, double[][] A) {
		double[] ta = new double[A.length];
		for(int l=0; l < A.length; l++)
			ta[l] = m.result(A[l]);
		return ta;
	}
	
	public boolean iteration(double[] B, double[][] A) {
		double[] Y = result(A);
		mult(Y, -1);
		add(Y, B);

		double[][] t = tr(A);
		List<PairDI> list = new ArrayList<PairDI>();		
		for(int i=0; i < t.length; i++)
			list.add(new PairDI(-correlation(t[i], Y), i));
		Collections.sort(list);

		List<Monom> newMonoms = new ArrayList<Monom>();
		for(int i=0; i < min(4, list.size()); i++) {
			newMonoms.add(new Monom(list.get(i).second));
			for(Monom m : this.monoms) {
				m.addFactor(list.get(i).second, newMonoms);
			}
		}
		addMonoms(newMonoms, Y, B, A);
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
