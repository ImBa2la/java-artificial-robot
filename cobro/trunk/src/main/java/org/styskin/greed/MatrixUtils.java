package org.styskin.greed;


public class MatrixUtils {
	
	public static float[][] tr(float[][] M) {
		float[][] r = new float[M[0].length][M.length];
		for(int i=0; i < M.length; i++)
			for(int j=0; j < M[i].length; j++)
				r[j][i] = M[i][j];
		return r;
	}
	
	public static void add(float[] a, float[] b) {
		for(int i=0; i < a.length; i++)
			a[i] += b[i];		
	}
	
	public static void mult(float[] a, float c) {
		for(int i=0; i < a.length; i++)
			a[i] *= c;		
	}	
	
	public static float[] copy(float[] a) {
		float[] r = new float[a.length];
		System.arraycopy(a, 0, r, 0, a.length);
		return r;
	}
	
	public static float correlation(float[] x, float[] y) {
		float d = 0;
		float a = 0, a1 = 0;
		float b = 0, b1 = 0;
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
		return (float) (Math.abs(d-a1*b1)/Math.sqrt((a-a1*a1)*(b-b1*b1)));
	}

}
