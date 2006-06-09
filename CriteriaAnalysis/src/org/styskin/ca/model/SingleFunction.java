package org.styskin.ca.model;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.styskin.ca.functions.single.Beta;
import org.styskin.ca.functions.single.BetaDistribution;
import org.styskin.ca.functions.single.Exponential;
import org.styskin.ca.functions.single.Gaussian;
import org.styskin.ca.functions.single.Linear;
import org.styskin.ca.functions.single.SingleOperator;
import org.xml.sax.Attributes;

public enum SingleFunction {
	LINEAR("linear", Linear.class),
	BETA("beta", Beta.class),
	GAUSSIAN("gaussian", Gaussian.class),
	BETA_DISTRIBUTION("beta_d", BetaDistribution.class),
	POWER("power", Exponential.class),
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

	private static Set<String> special = new HashSet<String>();

	static {
		special.add("name");
		special.add("weight");
		special.add("type");
		special.add("class");
	}

	public static SingleOperator getSingleOperator(String name, Attributes attributes) throws Exception {
		SingleFunction function = nameing.get(name);
		if (function == null) {
			throw new Exception("No operator with name " + name + " were found.");
		}
		Constructor<? extends SingleOperator> operatorConstructor = function.clazz.getConstructor(new Class[] {});
		SingleOperator operator = operatorConstructor.newInstance();

		for(int i=0; i < attributes.getLength(); i++) {
			if (!special.contains(attributes.getLocalName(i))) {
				String var = attributes.getLocalName(i);
				Method method = function.clazz.getMethod("set" + Character.toUpperCase(var.charAt(0)) + var.substring(1), new Class[] {double.class});
				// XXX fix Double.valueOf
				method.invoke(operator, Double.valueOf(attributes.getValue(i)));
			}
		}
		return operator;
	}

	public static String getOperatorName(Class<? extends SingleOperator> clazz) throws Exception {
		SingleFunction function = classing.get(clazz);
		return function.name;
	}
}
