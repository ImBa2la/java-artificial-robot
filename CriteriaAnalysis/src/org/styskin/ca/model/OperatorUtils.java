package org.styskin.ca.model;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.styskin.ca.functions.Operator;
import org.styskin.ca.functions.complex.AdditiveOperator;
import org.styskin.ca.functions.complex.ComplexOperator;
import org.styskin.ca.functions.complex.MultiplicativeOperator;
import org.styskin.ca.functions.complex.PowerDoubleOperator;
import org.styskin.ca.functions.complex.PowerIIOperator;
import org.styskin.ca.functions.complex.PowerIOperator;
import org.styskin.ca.functions.single.Beta;
import org.styskin.ca.functions.single.BetaDistribution;
import org.styskin.ca.functions.single.Exponential;
import org.styskin.ca.functions.single.Gaussian;
import org.styskin.ca.functions.single.Linear;
import org.styskin.ca.functions.single.SingleOperator;
import org.xml.sax.Attributes;

public class OperatorUtils {

	private static Map<OperatorType, Class> complexOperatorMapping;
	private static Map<SingleFunction, Class> singleOperatorMapping;

	private static Map<String, OperatorType> complexOperatorNameing;
	private static Map<String, SingleFunction> singleOperatorNaming;

	static {
		complexOperatorMapping = new HashMap<OperatorType, Class>();
		complexOperatorMapping.put(OperatorType.ADDITIVE, AdditiveOperator.class);
		complexOperatorMapping.put(OperatorType.MULTIPLICATIVE, MultiplicativeOperator.class);
		complexOperatorMapping.put(OperatorType.POWER_Double, PowerDoubleOperator.class);
		complexOperatorMapping.put(OperatorType.POWER_I, PowerIOperator.class);
		complexOperatorMapping.put(OperatorType.POWER_II, PowerIIOperator.class);

		singleOperatorMapping = new HashMap<SingleFunction, Class>();
		singleOperatorMapping.put(SingleFunction.LINEAR, Linear.class);
		singleOperatorMapping.put(SingleFunction.GAUSSIAN, Gaussian.class);
		singleOperatorMapping.put(SingleFunction.BETA, Beta.class);
		singleOperatorMapping.put(SingleFunction.BETA_DISTRIBUTION, BetaDistribution.class);
		singleOperatorMapping.put(SingleFunction.POWER, Exponential.class);
		singleOperatorMapping.put(SingleFunction.SIMPLE, SingleOperator.class);

		complexOperatorNameing = new HashMap<String, OperatorType>();
		complexOperatorNameing.put("add", OperatorType.ADDITIVE);
		complexOperatorNameing.put("mult", OperatorType.MULTIPLICATIVE);
		complexOperatorNameing.put("powerDouble", OperatorType.POWER_Double);
		complexOperatorNameing.put("powerI", OperatorType.POWER_I);
		complexOperatorNameing.put("powerII", OperatorType.POWER_II);

		singleOperatorNaming = new HashMap<String, SingleFunction>();
		singleOperatorNaming.put("linear", SingleFunction.LINEAR);
		singleOperatorNaming.put("beta", SingleFunction.BETA);
		singleOperatorNaming.put("beta_d", SingleFunction.BETA_DISTRIBUTION);
		singleOperatorNaming.put("gaussian", SingleFunction.GAUSSIAN);
		singleOperatorNaming.put("power", SingleFunction.POWER);
		singleOperatorNaming.put("simple", SingleFunction.SIMPLE);
	}

	public static ComplexOperator createOperator(OperatorType type, double L, List<Pair<Double, Operator>> children)
		throws Exception {

		Class operatorClass = complexOperatorMapping.get(type);
		Constructor operatorConstructor = operatorClass.getConstructor(new Class[] {double.class, List.class});
		return (ComplexOperator) operatorConstructor.newInstance(L, children);
	}

	public static ComplexOperator createOperator(OperatorType type, List<Pair<Double, Operator>> children)
		throws Exception {
		return createOperator(type, 0.5, children);
	}

	public static ComplexOperator createOperator(OperatorType type, double L) throws Exception {
		Class operatorClass = complexOperatorMapping.get(type);
		Constructor operatorConstructor = operatorClass.getConstructor(new Class[] {double.class});
		return (ComplexOperator) operatorConstructor.newInstance(L);
	}

	public static ComplexOperator createOperator(OperatorType type) throws Exception {
		return createOperator(type, 0.5);
	}

	public static OperatorType getOperatorTypeByName(String name) {
		return complexOperatorNameing.get(name);
	}

	public static SingleOperator getSingleOperator(String name, Attributes attributes) throws Exception {
		Class operatorClass = singleOperatorMapping.get(singleOperatorNaming.get(name));
		Constructor operatorConstructor = operatorClass.getConstructor(new Class[] {});
		SingleOperator operator = (SingleOperator) operatorConstructor.newInstance();

		Set<String> special = new HashSet<String>();
		special.add("name");
		special.add("weight");
		special.add("type");
		special.add("class");
		for(int i=0; i < attributes.getLength(); i++) {
			if (!special.contains(attributes.getLocalName(i))) {
				String var = attributes.getLocalName(i);
				Method method = operatorClass.getMethod("set" + Character.toUpperCase(var.charAt(0)) + var.substring(1), new Class[] {double.class});
				method.invoke(operator, Double.valueOf(attributes.getValue(i)));
			}
		}

		return operator;
	}
}
