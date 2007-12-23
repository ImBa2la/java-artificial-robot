/*
 *$Id: Optimizer.java 62 2007-05-09 16:19:07Z gutalin $
 */
package org.styskin.ca.math;

import org.styskin.ca.model.Constants;
import static java.lang.Math.abs;

public class Math implements Constants {
	
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
}
