/*
 *$Id: Optimizer.java 62 2007-05-09 16:19:07Z gutalin $
 */
package org.styskin.ca.functions.complex;

import static java.lang.Math.exp;
import static java.lang.Math.log;
import static java.lang.Math.pow;
import static org.styskin.ca.math.SimpleMathUtils.doubleEquals;
import static org.styskin.ca.math.SimpleMathUtils.doubleLess;
import static org.styskin.ca.math.SimpleMathUtils.doubleMore;
import static org.styskin.ca.math.SimpleMathUtils.findMinimum;
import it.unimi.dsi.fastutil.doubles.DoubleList;

import org.styskin.ca.math.Function;

public class ExponentalHOperator extends ComplexHOperator {
	
	private double A, B, C, xMax; 
	
	public ExponentalHOperator() throws Exception{
		super();
	}
	
	public ExponentalHOperator(DoubleList weights) throws Exception{
		super(weights);
	}
	
	@Override
	public String operatorType() {
		return "exp_h";
	}
	

	@Override
	public double getKsi(double x) {
		if(doubleEquals(lPhi, 0.5) && doubleEquals(lKsi, 0.5)) {
			return x;			
		} else if (doubleEquals(lPhi, 0.5) && doubleLess(lKsi, 0.5)) {
			
		} else if (doubleEquals(lPhi, 0.5) && doubleMore(lKsi, 0.5)) {
		} else if (doubleEquals(lKsi, 0.5) && doubleLess(lPhi, 0.5)) {
		} else if (doubleEquals(lKsi, 0.5) && doubleMore(lPhi, 0.5)) {
		} else if (doubleLess(lPhi, 0.5) && doubleLess(lKsi, 0.5)) {			
		} else if (doubleMore(lPhi, 0.5) && doubleLess(lKsi, 0.5)) {
		} else if (doubleMore(lPhi, 0.5) && doubleMore(lKsi, 0.5)) {
		} else if (doubleLess(lPhi, 0.5) && doubleMore(lKsi, 0.5)) {
		}		
		return 0;
	}

	@Override
	public double getPhi(double x) {
		if(doubleEquals(lPhi, 0.5) && doubleEquals(lKsi, 0.5)) {
		} else if (doubleEquals(lPhi, 0.5) && doubleLess(lKsi, 0.5)) {
		} else if (doubleEquals(lPhi, 0.5) && doubleMore(lKsi, 0.5)) {
		} else if (doubleEquals(lKsi, 0.5) && doubleLess(lPhi, 0.5)) {
		} else if (doubleEquals(lKsi, 0.5) && doubleMore(lPhi, 0.5)) {
		} else if (doubleLess(lPhi, 0.5) && doubleLess(lKsi, 0.5)) {			
		} else if (doubleMore(lPhi, 0.5) && doubleLess(lKsi, 0.5)) {
		} else if (doubleMore(lPhi, 0.5) && doubleMore(lKsi, 0.5)) {
		} else if (doubleLess(lPhi, 0.5) && doubleMore(lKsi, 0.5)) {
		}		
		return 0;
	}

	public double getValue(double[] X) throws Exception {
		assert(X.length != weights.size());			
		
		if(doubleEquals(lPhi, 0.5) && doubleEquals(lKsi, 0.5)) {
			double y = 0;		
			for(int i = 0; i < weights.size(); i++)
				y += weights.getDouble(i) * X[i];
			return y;
		} else if (doubleEquals(lPhi, 0.5) && doubleLess(lKsi, 0.5)) {
			double y = 0;
			for(int i=0; i < weights.size(); i++) {
				y += weights.getDouble(i)*X[i];				
			}
			return (exp((xMax*A)*y)-1)/xMax; 
		} else if (doubleEquals(lPhi, 0.5) && doubleMore(lKsi, 0.5)) {
			double y = 0;
			for(int i=0; i < weights.size(); i++) {
				y += weights.getDouble(i)*X[i];				
			}
			return (1-exp((-xMax*A)*y))/xMax;
		} else if (doubleEquals(lKsi, 0.5) && doubleMore(lPhi, 0.5)) {
            double y = 0;
            for(int i=0; i < weights.size(); i++) {
            	y += weights.getDouble(i) * log(1 - C*X[i]/B);
            }
            return -y/C;			
		} else if (doubleEquals(lKsi, 0.5) && doubleLess(lPhi, 0.5)) {
			double y = 0;
			for(int i=0; i < weights.size(); i++) {
                y += weights.getDouble(i)*log(C*X[i]/B+1);
			}
			return y/C;
		} else if (doubleLess(lPhi, 0.5) && doubleLess(lKsi, 0.5)) {			
            double y = 1;
            for(int i=0; i < weights.size(); i++) {
                y = y * pow(1+C*X[i]/B, A*weights.getDouble(i));         
            }
            y -= 1;
            y /= C;
            return y;			
		} else if (doubleMore(lPhi, 0.5) && doubleLess(lKsi, 0.5)) {
            double y = 1;
            for(int i=0; i < weights.size(); i++) {
                y = y * pow(1-C*X[i]/B, -A*weights.getDouble(i));         
            }
            y -= 1;
            y /= C;
            return y;
		} else if (doubleMore(lPhi, 0.5) && doubleMore(lKsi, 0.5)) {
            double y = 1;
            for(int i=0; i < weights.size(); i++) {
                y *= pow(1-C*X[i]/B, A*weights.getDouble(i));         
            }
            y = 1 - y;
            y /= C;
            return y;			
		} else if (doubleLess(lPhi, 0.5) && doubleMore(lKsi, 0.5)) {
            double y = 1;
            for(int i=0; i < weights.size(); i++) {
                y *= pow(1+X[i]/B, -A*weights.getDouble(i));         
            }
            y = 1 - y;
            y /= C;
            return y;
		}
		assert(true);
		return 0;
	}

	class FindXmaxPL1 implements Function {		
		public double getValue(double x) {
			return log(lKsi*x+1)/(1-lKsi)-log(x+1);
		}
	}	
	class FindXmaxPL2 implements Function {		
		public double getValue(double x) {
			return log(1-lKsi*x)/(1-lKsi)-log(1-x);
		}
	}	
	class FindXmaxLP1 implements Function {		
		public double getValue(double x) {
			return -lPhi*x/(1-exp(-x*(1-lPhi))) + x/(1-exp(-x)); 
		}
	}	
	class FindXmaxLP2 implements Function {		
		public double getValue(double x) {
			return lPhi*x/(exp(x*(1-lPhi))-1) - x/(exp(x)-1);
		}
	}	
	
	class FindXmax11 implements Function {
		public double getValue(double x) {
			return lPhi * (exp(x)-1) - exp(x*(1-lPhi)) + 1;
		}		
	}
	class FindA11 implements Function {
		public double getValue(double a) {
			return lKsi*(exp(a*xMax)-1)-exp(a*xMax*(1-lKsi))+1;
		}		
	}
	class FindXmax12 implements Function {
		public double getValue(double x) {
			return  lPhi * (1 - exp(-x)) + exp(-x*(1-lPhi)) - 1;
		}		
	}
	class FindA12 implements Function {
		public double getValue(double a) {
			return lKsi*(exp(a*xMax)-1)-exp(a*(xMax*(1-lKsi)))+1;
		}		
	}
	class FindXmax21 implements Function {
		public double getValue(double x) {
			return lPhi * (1-exp(-x)) + exp(-x*(1-lPhi)) - 1;
		}		
	}
	class FindA21 implements Function {
		public double getValue(double a) {
			return lKsi*(1-exp(-a*xMax)) + exp(-a*xMax*(1-lKsi)) - 1;
		}		
	}
	class FindXmax22 implements Function {
		public double getValue(double x) {
			return  lPhi * (exp(x)-1) - exp(x*(1-lPhi)) + 1;
		}
	}
	class FindA22 implements Function {
		public double getValue(double a) {
			return lKsi*(1-exp(-a*xMax))+exp(-a*xMax*(1-lKsi))-1;
		}
	}
	
	@Override
	public void initialize() {
		if(doubleEquals(lPhi, 0.5) && doubleEquals(lKsi, 0.5)) {
			
		} else if (doubleEquals(lPhi, 0.5) && doubleLess(lKsi, 0.5)) {
			xMax = findMinimum(new FindXmaxPL1(), EPS, MAX_DOUBLE);
	        A = log(xMax+1)/xMax;
		} else if (doubleEquals(lPhi, 0.5) && doubleMore(lKsi, 0.5)) {
			xMax = findMinimum(new FindXmaxPL2(), EPS, 1 - EPS);
	        A = -log(1-xMax)/xMax;
		} else if (doubleEquals(lKsi, 0.5) && doubleMore(lPhi, 0.5)) {
			xMax = findMinimum(new FindXmaxLP1(), EPS, MAX_DOUBLE);
	        C = xMax;
	        B = xMax/(1-exp(-xMax));
		} else if (doubleEquals(lKsi, 0.5) && doubleLess(lPhi, 0.5)) {
			xMax = findMinimum(new FindXmaxLP2(), EPS, MAX_DOUBLE);
	        C = xMax;
	        B = xMax/(exp(xMax)-1);
		} else if (doubleLess(lPhi, 0.5) && doubleLess(lKsi, 0.5)) {
			xMax = findMinimum(new FindXmax11(), EPS, MAX_DOUBLE);
			A = findMinimum(new FindA11(), EPS, MAX_DOUBLE);
		    B = (exp(A*xMax)-1)/(exp(xMax)-1);    
		    C = B*(exp(xMax)-1);
		} else if (doubleMore(lPhi, 0.5) && doubleLess(lKsi, 0.5)) {
			xMax = findMinimum(new FindXmax12(), EPS, MAX_DOUBLE);
			A = findMinimum(new FindA12(), EPS, MAX_DOUBLE);
	        B = (exp(A*xMax)-1)/(1-exp(-xMax));        
	        C = exp(A*xMax)-1;			
		} else if (doubleMore(lPhi, 0.5) && doubleMore(lKsi, 0.5)) {
			xMax = findMinimum(new FindXmax21(), EPS, MAX_DOUBLE);
			A = findMinimum(new FindA21(), EPS, MAX_DOUBLE);			
	        B = (1-exp(-A*xMax))/(1-exp(-xMax));    
	        C = B*(1-exp(-xMax));
		} else if (doubleLess(lPhi, 0.5) && doubleMore(lKsi, 0.5)) {
			xMax = findMinimum(new FindXmax22(), EPS, MAX_DOUBLE);
			A = findMinimum(new FindA22(), EPS, MAX_DOUBLE);		
	        B = (1-exp(-A*xMax))/(exp(xMax)-1);        
	        C = 1 - exp(-A*xMax);			
		}		
	}
}
