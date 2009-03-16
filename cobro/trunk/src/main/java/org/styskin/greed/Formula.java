package org.styskin.greed;

import java.util.ArrayList;
import java.util.List;
import static org.styskin.greed.MatrixUtils.*;


public class Formula {
	
	List<Monom> monoms = new ArrayList<Monom>();
	List<Float> weight = new ArrayList<Float>();
	
	static class Monom {
		List<Integer> list = new ArrayList<Integer>();
		
		public float result(float[] row) {
			float r = 1;
			for(int i=0; i < list.size(); i++)
				r *= row[list.get(i)];
			return r;
		}
		
		Monom copy() {
			Monom m = new Monom();
			m.list.addAll(this.list);
			return m;
		}
		
		List<Monom> addFactor(int index) {
			List<Monom> list = new ArrayList<Monom>();
			{
				Monom m = copy();
				m.list.add(index);
				list.add(m);
			}
			for(int i=0; i < list.size(); i++) {
				Monom m = copy();
				m.list.set(i, index);
				list.add(m);
			}		
			return list;
		}
	}
	
	public float[] result(float[][] M) {
		float[] res = new float[M.length];
		for(int i=0; i < M.length; i++) {
			for(int j=0; j < monoms.size(); j++)
				res[i] += weight.get(j) * monoms.get(j).result(M[i]);
		}
		return res;
	}
	
	public boolean addMonoms(List<Monom> list, float[] B, float[][] A) {
		float[] Y = result(A);
		mult(Y, -1);
		add(Y, B);
		int N = B.length;
		float[][] X = new float[list.size()][N];
		for(int i=0; i < list.size(); i++)
			for(int j=0; j < N; j++)
				X[i][j] = list.get(i).result(A[j]);
		// Regression for X -> Y
		
		
		
		return true; // true - if added
	}

}
