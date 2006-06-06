package org.styskin.ca.model;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.styskin.ca.functions.Operator;
import org.styskin.ca.functions.complex.AdditiveOperator;
import org.styskin.ca.functions.complex.ComplexOperator;
import org.styskin.ca.functions.complex.MultiplicativeOperator;
import org.styskin.ca.functions.complex.PowerDoubleOperator;
import org.styskin.ca.functions.complex.PowerIIOperator;
import org.styskin.ca.functions.complex.PowerIOperator;

public class OperatorUtils {

	private static Map<OperatorType, Class> complexOperatorMapping;

	private static Map<String, OperatorType> nameOperatorMapping;

	static {
		complexOperatorMapping = new HashMap<OperatorType, Class>();
		complexOperatorMapping.put(OperatorType.ADDITIVE, AdditiveOperator.class);
		complexOperatorMapping.put(OperatorType.MULTIPLICATIVE, MultiplicativeOperator.class);
		complexOperatorMapping.put(OperatorType.POWER_Double, PowerDoubleOperator.class);
		complexOperatorMapping.put(OperatorType.POWER_I, PowerIOperator.class);
		complexOperatorMapping.put(OperatorType.POWER_II, PowerIIOperator.class);

		nameOperatorMapping = new HashMap<String, OperatorType>();
		nameOperatorMapping.put("add", OperatorType.ADDITIVE);
		nameOperatorMapping.put("mult", OperatorType.MULTIPLICATIVE);
		nameOperatorMapping.put("powerDouble", OperatorType.POWER_Double);
		nameOperatorMapping.put("powerI", OperatorType.POWER_I);
		nameOperatorMapping.put("powerII", OperatorType.POWER_II);
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

	public static ComplexOperator createOperator(OperatorType type, double L)	throws Exception {
		Class operatorClass = complexOperatorMapping.get(type);
		Constructor operatorConstructor = operatorClass.getConstructor(new Class[] {double.class});
		return (ComplexOperator) operatorConstructor.newInstance(L);
	}

	public static ComplexOperator createOperator(OperatorType type) throws Exception {
		return createOperator(type, 0.5);
	}

	public static OperatorType getOperatorTypeByName(String name) {
		return nameOperatorMapping.get(name);
	}
}
