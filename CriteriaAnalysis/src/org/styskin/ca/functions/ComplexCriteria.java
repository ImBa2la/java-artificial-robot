package org.styskin.ca.functions;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import org.styskin.ca.model.OperatorType;
import org.styskin.ca.model.OperatorUtils;

public class ComplexCriteria extends Criteria {	
	List<Criteria> children = new ArrayList<Criteria>();
	public ComplexOperator operator;
	
	static protected NumberFormat FORMAT = DecimalFormat.getInstance();
	

	public ComplexCriteria(OperatorType type, double L) throws Exception {
		operator = OperatorUtils.createOperator(type, L);
	}

	public ComplexCriteria(OperatorType type) throws Exception {
		operator = OperatorUtils.createOperator(type);
	}

	public void addChild(Criteria criteria, double weight) {
		children.add(criteria);
		operator.addCriteria(weight);
	}

	protected void setValues(double[] X, int start, int end) throws Exception {
		int size = 0;
		for(Criteria criteria : children) {
			size += criteria.getSize();
		}
		if(size != end - start + 1) {
			throw new Exception("Incorrect arguments count");
		}

	}

	@Override
	protected double getValue(double[] X, int start, int end) throws Exception {
		double[] Y = new double[children.size()];

		int i = 0;
		for(Criteria criteria : children) {
			Y[i++] = criteria.getValue(X, start, start + criteria.getSize() - 1);
			start += criteria.getSize();
		}
		
		return operator.getValue(Y);
	}

	@Override
	protected int getSize() {
		return children.size();
	}

	@Override
	public int getTotalSize() {
		int size = 0;
		for(Criteria childCriteria : children) {
			size += childCriteria.getTotalSize();
		}
		return size;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("(").append(operator.operatorType()).append(", L=").append(FORMAT.format(operator.lambda)).append(' ');

		int i = 0;
		for(Criteria child : children) {
			sb.append('{').append(FORMAT.format(operator.weights.get(i++))).append(" - ").append(child.toString()).append('}');
		}
		sb.append(')');
		return sb.toString();
	}
}
