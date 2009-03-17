package org.styskin.ca.functions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public interface Checker {
	double check(double[] x);
}

class CheckLeastSquare implements Checker {
	
	private double[] y;
	
	public CheckLeastSquare(double[] y) {
		this.y = y;
	}

	public double check(double[] x) {
		double d = 0;
		for(int i=0; i< y.length; i++) {
			double t = x[i] - y[i];
			d += t*t;
		}
		return Math.sqrt(d/y.length);
	}
}

class CheckAbs implements Checker {
	private double[] y;
	
	public CheckAbs(double[] y) {
		this.y = y;
	}

	public double check(double[] x) {
		double d = 0;
		for(int i=0; i< y.length; i++) {
			double t = Math.abs(x[i] - y[i]);
			d += t;
		}
		return d/y.length;
	}
}

class CheckSampleCorrelation implements Checker {
	private double[] y;
	private double b = 0, b1 = 0;
	
	public CheckSampleCorrelation(double[] y) {
		this.y = y;
		for(int i=0; i< y.length; i++) {
			b1 += y[i];
			b += y[i]*y[i];
		}
		b *= y.length;
	}

	public double check(double[] x) {
		double d = 0, a = 0;
		double a1 = 0;
		for(int i=0; i< y.length; i++) {
			d += x[i]*y[i];
			a1 += x[i];
			a += x[i]*x[i];
		}
		d *= y.length;
		a *= y.length;
		return -Math.abs(d-a1*b1)/Math.sqrt((a-a1*a1)*(b-b1*b1));		
	}
}

class CheckMaxError implements Checker {
	private double[] y;
	
	public CheckMaxError(double[] y) {
		this.y = y;
	}

	public double check(double[] x) {
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

class CheckPfound implements Checker {
	private double[] y;
	private int[] req;
	
	public CheckPfound(double[] y, int[] req) {
		this.y = y;
		this.req = req;
	}
	
	static class S {
		int req;
		double p;
		double s;
		public S(int req, double p, double s) {
			this.req = req;
			this.p = p;
			this.s = s;
		}
	}

	public double check(double[] x) {
		List<S> list = new ArrayList<S>();
		for(int i=0; i< y.length; ++i) {
			list.add(new S(req[i], x[i], y[i]));
		}
		Collections.sort(list, new Comparator<S>() {
			public int compare(S o1, S o2) {
				if(o1.req < o2.req)
					return -1;				
				if(o1.req > o2.req)
					return 1;				
				return -Double.compare(o1.p, o2.p);
			}
		});
		
		double d = 0;
		int rq = 0;
		int c = -1;
		double o8 = 1;
		int p10 = 0;
		for(int i=0; i < y.length; ++i) {
			S s = list.get(i);
			if(c != s.req) {
				++ rq;				
				c = s.req;
				o8 = 1;
				p10 = 0;
			}
			if(p10 < 10)
				d += o8*s.s;
			o8 *= 0.8;
			++ p10; 
		}
		return -d/rq;
	}
}
