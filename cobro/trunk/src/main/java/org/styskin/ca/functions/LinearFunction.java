package org.styskin.ca.functions;

import java.util.Map;

import org.styskin.ca.math.SaveFunction;

public class LinearFunction implements SaveFunction {
	
	private double a;
	private double b;

	public LinearFunction() {
		a = 1;
		b = 0;
	}

	public double getValue(double x) {
		return a*x + b;
	}
	
	public void loadParameters(Map<String, Double> lambda) {
		a = lambda.containsKey("lA") ? lambda.get("lA") : 0;
		b = lambda.containsKey("lB") ? lambda.get("lB") : 0;
	}
	
	public void saveParameters(Map<String, Double> lambda) {
		lambda.put("lA", a);
		lambda.put("lB", b);		
	}

}
