package org.styskin.greed;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.styskin.util.PairDI;

import Jama.Matrix;
import static org.styskin.greed.MatrixUtils.*;
import static java.lang.Math.*;


public class Formula {
	
	List<Monom> monoms = new ArrayList<Monom>();
	List<Double> weight = new ArrayList<Double>();
	
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
	}
	
	public double[] result(double[][] M) {
		double[] res = new double[M.length];
		for(int i=0; i < M.length; i++) {
			for(int j=0; j < monoms.size(); j++)
				res[i] += weight.get(j) * monoms.get(j).result(M[i]);
		}
		return res;
	}
	
	public boolean addMonoms(List<Monom> newMonoms, double[] Y, double[][] A) {
		List<PairDI> r = new ArrayList<PairDI>();
		for(int i=0; i < newMonoms.size(); i++) {
			Monom m = newMonoms.get(i);
			double[] t = new double[A.length];
			for(int j=0; j < A.length; j++)
				t[j] = m.result(A[j]);
			r.add(new PairDI(-correlation(t, Y), i));
		}
		Collections.sort(r);
		List<Monom> list = new ArrayList<Monom>();
		for(int i=0; i < min(4, r.size()); i++) {
			list.add(newMonoms.get(r.get(i).second));
		}		
		//======
		int N = Y.length;
		double[][] X = new double[list.size()][N];
		for(int i=0; i < list.size(); i++)
			for(int j=0; j < N; j++)
				X[i][j] = list.get(i).result(A[j]);
		// Regression for X -> Y
		Matrix a = new Matrix(X); 
		Matrix b = new Matrix(new double[][] {Y});
		Matrix x = a.transpose().solve(b.transpose());
		for(int i=0; i < list.size(); i++) {
			this.monoms.add(list.get(i));
			this.weight.add(x.get(i, 0));
		}
		return true; // true - if added
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
		for(int i=0; i < 4; i++) {
			newMonoms.add(new Monom(list.get(i).second));
			for(Monom m : this.monoms) {
				m.addFactor(list.get(i).second, newMonoms);
			}
		}
		addMonoms(newMonoms, Y, A);
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

}
