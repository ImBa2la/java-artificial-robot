/*
 *$Id: Optimizer.java 62 2007-05-09 16:19:07Z gutalin $
 */
package org.styskin.ca.functions.complex;

import java.util.List;

public abstract class ComplexGOperator extends ComplexOperator {
	
	public ComplexGOperator() throws Exception{
		super();
	}
	
	public ComplexGOperator(List<Double> weights) throws Exception{
		super(weights);
	}	

}
