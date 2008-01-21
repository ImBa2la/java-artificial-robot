package org.styskin.ca.functions;

public interface Optimizer {

	Criteria optimize(double[] base, double[][] F);
	
	Criteria optimize(Criteria base, double[][] F);	

}