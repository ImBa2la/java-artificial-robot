/*
 *$Id$
 */
package org.styskin.ca.functions.single;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.styskin.ca.functions.Operator;
import org.styskin.ca.math.Function;

public class SingleOperator implements Operator, Function, Cloneable {
	
	private static final Logger logger = Logger.getLogger(SingleOperator.class);
	
	private double fMax = 1, fMin = 0;
	
	public double getLFMax() {
		return fMax;
	}
	public double getLFMin() {
		return fMin;
	}
	public void setLFMax(double max) {
		fMax = max;
	}
	public void setLFMin(double min) {
		fMin = min;
	}	

	public double getValue(double[] X) throws Exception {
		return X[0];
	}
	
	public double getValue(double x) {
		double[] X = new double[1];
		X[0] = x;
		double res = 0;
		try {
			res = getValue(X);
		} catch (Exception e) {
			logger.error("Cannot calc value", e);
		}
		return res;
	}

	public Operator clone() throws CloneNotSupportedException {
		SingleOperator op = (SingleOperator) super.clone();
		return op;
	}
	
	private static Set<String> special = new HashSet<String>();

	static {
		special.add("name");
		special.add("weight");
		special.add("type");
		special.add("class");
	}

	public void loadParameters(Map<String, Double> lambda) {
		try {
			for(Map.Entry<String, Double> en : lambda.entrySet()) {
				String attr = en.getKey();			
				if (!special.contains(attr)) {
					Method method = getClass().getMethod("set" + Character.toUpperCase(attr.charAt(0)) + attr.substring(1), new Class[] {double.class});
					method.invoke(this, en.getValue());
				}
			}
		} catch(Exception e) {
			logger.error("Cannot set SingleOperator", e);			
		}
				
	}

	public void saveParameters(Map<String, Double> lambda) {
		try {
			for(Method method :	getClass().getMethods()) {
				if(method.getName().startsWith("get")) {
					String param = method.getName().substring(3);
					param = Character.toLowerCase(param.charAt(0)) + param.substring(1);
					if(!special.contains(param) && param.startsWith("l")) {					
						lambda.put(param, (Double) method.invoke(this));
					}				
				}
			}
		} catch(Exception e) {
			logger.error("Cannot save SingleOperator", e);			
		}
	}

}
