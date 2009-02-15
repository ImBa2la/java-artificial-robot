/*
 *$Id: Optimizer.java 62 2007-05-09 16:19:07Z gutalin $
 */
package org.styskin.ca.math;

import org.styskin.ca.model.Constants;
import static java.lang.Math.abs;

public class SimpleMathUtils implements Constants {
	
	public static boolean doubleEquals(double a, double b) {
		return abs(a - b) < EPS; 		
	}

	public static boolean doubleMore(double a, double b) {
		return a > b + EPS;
	}

	public static boolean doubleLess(double a, double b) {
		return doubleMore(b, a);		
	}	
	
	public static double findMinimum(Function f, double a, double b) {
		double c;
		while(b - a > EPS) {
			c = (a + b)/2;
			if(f.getValue(c) < 0) {
				a = c;				
			} else {
				b = c;				
			}			
		}
		return a;
	}
	
	public static int findIndex(double[] f, double v) {
		int a = 0, b = f.length-1, c;
		if(v < f[a] || v > f[b]) 
			return -1;
		while(b-a > 1) {
			c = (a+b)/2;
			if(f[c] > v)
				b = c;
			if(f[c] <= v)
				a = c;
		}
		return a;
	}
}
