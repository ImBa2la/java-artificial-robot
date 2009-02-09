/*
 *$Id: Optimizer.java 62 2007-05-09 16:19:07Z gutalin $
 */
package org.styskin.ca.model;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

import org.styskin.ca.functions.single.Beta;
import org.styskin.ca.functions.single.BetaDistribution;
import org.styskin.ca.functions.single.Exponential;
import org.styskin.ca.functions.single.Gaussian;
import org.styskin.ca.functions.single.InvFunction;
import org.styskin.ca.functions.single.Linear;
import org.styskin.ca.functions.single.SingleOperator;

public enum SingleFunction {
	LINEAR("linear", Linear.class),
	BETA("beta", Beta.class),
	GAUSSIAN("gaussian", Gaussian.class),
	BETA_DISTRIBUTION("beta_d", BetaDistribution.class),
	POWER("power", Exponential.class),
	INV("inv", InvFunction.class),
	SIMPLE("simple", SingleOperator.class);

	private String name;
	private Class<? extends SingleOperator> clazz;

	private static Map<String, SingleFunction> nameing;
	private static Map<Class<? extends SingleOperator>, SingleFunction> classing;

	private SingleFunction(String name, Class<? extends SingleOperator> clazz) {
		this.name = name;
		this.clazz = clazz;
		index(name, clazz, this);
	}

	private static void index(String name, Class<? extends SingleOperator> clazz, SingleFunction function) {
		if (nameing == null) {
			nameing = new HashMap<String, SingleFunction>();
		}
		if (classing == null) {
			 classing = new HashMap<Class<? extends SingleOperator>, SingleFunction>();
		}
		nameing.put(name, function);
		classing.put(clazz, function);
	}
	
	public SingleOperator createOperator() throws Exception {
		Constructor<? extends SingleOperator> operatorConstructor = clazz.getConstructor(new Class[] {});
		return operatorConstructor.newInstance();
	}	

	public static SingleOperator getSingleOperator(String name) throws Exception {
		SingleFunction function = nameing.get(name);
		if (function == null) {
			throw new Exception("No operator with name " + name + " were found.");
		}
		Constructor<? extends SingleOperator> operatorConstructor = function.clazz.getConstructor(new Class[] {});
		SingleOperator operator = operatorConstructor.newInstance();
		return operator;
	}

	public static String getOperatorName(Class<? extends SingleOperator> clazz) throws Exception {
		SingleFunction function = classing.get(clazz);
		return function.name;
	}
	
	public static SingleFunction getFunction(Class<? extends SingleOperator> clazz) {
		return classing.get(clazz);
	}	
	
}
