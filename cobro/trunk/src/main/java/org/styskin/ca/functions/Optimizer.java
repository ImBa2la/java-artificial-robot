package org.styskin.ca.functions;

public interface Optimizer {

	Criteria optimize(double[] base, double[][] F) throws Exception;
	
	Criteria optimize(Criteria base, double[][] F) throws Exception;	

}