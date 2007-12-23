/*
 *$Id$
 */
package org.styskin.ca.model;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

import org.styskin.ca.functions.complex.AdditiveOperator;
import org.styskin.ca.functions.complex.ComplexOperator;
import org.styskin.ca.functions.complex.ExponentalHOperator;
import org.styskin.ca.functions.complex.PowerHOperator;
import org.styskin.ca.functions.complex.PowerIOperator;

public enum ComplexFunction {
	ADDITIVE("add", AdditiveOperator.class),
	POWER_H("pow_h", PowerHOperator.class),
	EXPONENTAL_H("exp_h", ExponentalHOperator.class),
	POWER_I("pow_I", PowerIOperator.class);

	private String name;
	private Class<? extends ComplexOperator> clazz;

	private static Map<String, ComplexFunction> nameing;
	private static Map<Class<? extends ComplexOperator>, ComplexFunction> classing;

	private ComplexFunction(String name, Class<? extends ComplexOperator> clazz) {
		this.name = name;
		this.clazz = clazz;
		index(name, clazz, this);
	}

	private static void index(String name, Class<? extends ComplexOperator> clazz, ComplexFunction function) {
		if (nameing == null) {
			nameing = new HashMap<String, ComplexFunction>();
		}
		if (classing == null) {
			 classing = new HashMap<Class<? extends ComplexOperator>, ComplexFunction>();
		}
		nameing.put(name, function);
		classing.put(clazz, function);
	}

	public ComplexOperator createOperator() throws Exception {
		Constructor<? extends ComplexOperator> operatorConstructor = clazz.getConstructor(new Class[] {});
		return operatorConstructor.newInstance();
	}

	public static ComplexOperator createOperator(String name) throws Exception {
		ComplexFunction function = nameing.get(name);
		if (function == null) {
			throw new Exception("No operator with name " + name + " were found.");
		}
		Constructor<? extends ComplexOperator> operatorConstructor = function.clazz.getConstructor(new Class[] {});
		return operatorConstructor.newInstance();
	}

	public static String getOperatorName(Class<? extends ComplexOperator> clazz) throws Exception {
		ComplexFunction function = classing.get(clazz);
		return function.name;
	}

	public static ComplexFunction getFunction(Class<? extends ComplexOperator> clazz) {
		return classing.get(clazz);
	}

	public String toString() {
		return name;
	}

}
