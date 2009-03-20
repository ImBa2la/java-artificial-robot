package org.styskin.greed;

import static org.styskin.greed.MatrixUtils.correlation;
import static org.styskin.greed.MatrixUtils.scalar;

import java.util.Arrays;

import org.styskin.util.LoadInput;

public abstract class Optimizer {
	
	private static final double EPS = 1E-9;
	
	abstract double check(double[] a, double[] b, int[] q);
	
	double[] result(double[] w, double[][] x, double[] res) {
		for(int i=0; i < x.length; i++) {
			res[i] = 0;
			for(int j=0; j < w.length; j++) {
				res[i] += w[j] * x[i][j];				
			}
		}
		return res;
	}
	
	boolean searchPoint(int M, double[] w, double[][] x, double[] y, int[] q, double[] h) {
		//TODO: fast calculation
		boolean was = false;
		double[] c = new double[x.length];
		double cr = check(result(w, x, c), y, q);
		for(int i=0; i < M; i++) {
			for(int m = -1; m <=1; m += 2) {
				w[i] += m*h[i];
				double ncr = check(result(w, x, c), y, q);
				if(ncr > cr + EPS) {
					h[i] = m*h[i];
					break;
				}
				w[i] -= m*h[i];
			}
		}	
		return was;
	}
	
	public void optimizeFunction(double[] w, double[][] x, double[] y, int[] q) {
		int N = x.length;
		int M = w.length;
		double[] h = new double[N];
		
		int deep = 0;
		outer: for(int i=0; i < 100; i++) {
			double start = 1;
			Arrays.fill(h, start);
			while(!searchPoint(M, w, x, y, q, h)) {
				start /= 2;
				Arrays.fill(h, start);
				if(++ deep > 15)
					break outer;
			}
			double[] c = new double[N];
			double cr = check(result(w, x, c), y, q);
			while(true) {
				for(int j=0; j < N; j++)
					w[j] += h[j];
				double ncr = check(result(w, x, c), y, q);
				if(ncr > cr + EPS) {
					for(int j=0; j < N; j++)
						h[j] *= 2;					
				} else {
					for(int j=0; j < N; j++)
						w[j] -= h[j];
					break;					
				}
			}
		}
	}	
	
	public void optimize(Formula fml, LoadInput in) {
		int N = in.M.length;
		int M = fml.monoms.size();
		double[][] x = new double[N][M];
		double[] w = new double[M];
		for(int i=0; i < N; i++)
			for(int j=0; j < fml.monoms.size(); j++)
				x[i][j] = fml.monoms.get(j).result(in.M[i]);
		for(int j=0; j < M; j++)
			w[j] = fml.weight.getDouble(j);
		optimizeFunction(w, x, in.B, in.Q);
		for(int i=0; i < M; i++)
			fml.weight.set(i, w[i]);
	}
}
