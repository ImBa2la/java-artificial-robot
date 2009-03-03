package org.styskin.ca.functions;

import java.util.Map;


public class LinearFunction implements SaveLoadFunction {
	
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
		if(lambda.containsKey("lA"))
			a = lambda.get("lA");
		if(lambda.containsKey("lB"))
			b = lambda.get("lB");
	}
	
	public void saveParameters(Map<String, Double> lambda) {
		lambda.put("lA", a);
		lambda.put("lB", b);		
	}

}
