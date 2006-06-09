package org.styskin.ca.functions;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import org.styskin.ca.functions.complex.ComplexOperator;
import org.styskin.ca.model.OperatorType;
import org.styskin.ca.model.OperatorUtils;

public class ComplexCriteria extends Criteria {
	List<Criteria> children = new ArrayList<Criteria>();
	public ComplexOperator operator;

	private int totalSize = -1;

	static protected NumberFormat FORMAT = DecimalFormat.getInstance();

	public List<Criteria> getChildren() {
		return children;
	}

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

	@Override
	protected double getValue(double[] X, int start, int end) throws Exception {
		double[] Y = new double[children.size()];

		int i = 0;
		for(Criteria criteria : children) {
			Y[i++] = criteria.getValue(X, start, start + criteria.getTotalSize() - 1);
			start += criteria.getTotalSize();
		}

		return operator.getValue(Y);
	}

	@Override
	protected int getSize() {
		return children.size();
	}

	@Override
	public int getTotalSize() {
		if(totalSize > 0) {
			return totalSize;
		} else {
			totalSize = 0;
			for(Criteria childCriteria : children) {
				totalSize += childCriteria.getTotalSize();
			}
			return totalSize;
		}
	}

/*	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("(").append(operator.operatorType()).append(", L=").append(FORMAT.format(operator.lambda)).append(' ');

		int i = 0;
		for(Criteria child : children) {
			sb.append('{').append(FORMAT.format(operator.weights.get(i++))).append(" - ").append(child.toString()).append('}');
		}
		sb.append(')');
		return sb.toString();
	}*/
}
