package org.styskin.greed;

import static java.lang.Math.*;

public class MatrixUtils {
	
	public static double[][] tr(double[][] M) {
		double[][] r = new double[M[0].length][M.length];
		for(int i=0; i < M.length; i++)
			for(int j=0; j < M[i].length; j++)
				r[j][i] = M[i][j];
		return r;
	}
	
	public static void add(double[] a, double[] b) {
		for(int i=0; i < a.length; i++)
			a[i] += b[i];		
	}
	
	public static void mult(double[] a, double c) {
		for(int i=0; i < a.length; i++)
			a[i] *= c;		
	}
	
	public static double[] mult(double[] a, double c, double[] res) {
		for(int i=0; i < a.length; i++)
			res[i] = a[i] * c;
		return res;
	}	
	
	public static double[] scalar(double[] a, double[] b, double[] res) {
		for(int i=0; i < a.length; i++)
			res[i] = a[i] * b[i];
		return res;
	}
	
	public static double[] copy(double[] a) {
		double[] r = new double[a.length];
		System.arraycopy(a, 0, r, 0, a.length);
		return r;
	}
	
	public static double leastSqares(double[] x, double[] y) {
		int N = x.length;
		double s = 0;
		for(int i=0; i < N; i++)
			s += (x[i]-y[i])*(x[i]-y[i]);
		s /= N;
		return sqrt(s);
	}
	
	public static double correlation(double[] x, double[] y) {
		double d = 0;
		double a = 1e-10, a1 = 0;
		double b = 1e-10, b1 = 0;
		for(int i=0; i< y.length; i++) {
			a += x[i]*x[i];
			a1 += x[i];
			b += y[i]*y[i];
			b1 += y[i];
			d += x[i]*y[i];
		}
		b *= y.length;		
		d *= y.length;
		a *= y.length;
		return Math.abs(d-a1*b1)/Math.sqrt((a-a1*a1)*(b-b1*b1));
	}

}
