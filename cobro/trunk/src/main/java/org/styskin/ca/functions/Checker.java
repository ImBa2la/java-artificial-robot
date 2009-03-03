package org.styskin.ca.functions;

public interface Checker {
	double check(double[] x, double[] y);
}

class CheckLeastSquare implements Checker {
	public double check(double[] x, double[] y) {
		double d = 0;
		for(int i=0; i< y.length; i++) {
			double t = x[i] - y[i];
			d += t*t;
		}
		return Math.sqrt(d/y.length);
	}
}

class CheckAbs implements Checker {
	public double check(double[] x, double[] y) {
		double d = 0;
		for(int i=0; i< y.length; i++) {
			double t = Math.abs(x[i] - y[i]);
			d += t;
		}
		return d/y.length;
	}
}

class CheckCorrelation implements Checker {
	public double check(double[] x, double[] y) {
		double d = 0, a = 0, b = 0;
		for(int i=0; i< y.length; i++) {
			d += x[i] * y[i];
			a += x[i]*x[i];
			b += y[i]*y[i];
		}
		return -Math.abs(d)/Math.sqrt(a*b);
	}
}

class CheckSampleCorrelation implements Checker {
	public double check(double[] x, double[] y) {
		double d = 0, a = 0, b = 0;
		double a1 = 0, b1 = 0;
		for(int i=0; i< y.length; i++) {
			d += x[i]*y[i];
			a += x[i];
			b += y[i];
			a += x[i]*x[i];
			b += y[i]*y[i];
		}
		d *= y.length;
		a *= y.length;
		b *= y.length;
		return -Math.abs(d-a1*b1)/Math.sqrt((a-a1*a1)*(b-b1*b1));		
	}
}

class CheckMaxError implements Checker {
	public double check(double[] x, double[] y) {
		double d = 0;
		for(int i=0; i< y.length; i++) {
			double t = x[i] - y[i];
			if (Math.abs(t) > d) {
				d = Math.abs(t);
			}
		}
		return d;
	}
}
