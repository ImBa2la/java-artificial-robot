package org.styskin.ca.functions;

import java.util.ArrayList;
import java.util.List;

import org.styskin.ca.model.OperatorType;
import org.styskin.ca.model.OperatorUtils;

public class ComplexCriteria extends Criteria {
	List<Criteria> children = new ArrayList<Criteria>();
	ComplexOperator operator;

	public ComplexCriteria(OperatorType type, double L) throws Exception {
		operator = OperatorUtils.createOperator(type, L);
	}

	public ComplexCriteria(OperatorType type) throws Exception {
		operator = OperatorUtils.createOperator(type);
	}

	public void addChild(Criteria criteria, double weight) {
		children.add(criteria);
		operator.addCriteria(criteria, weight);
	}

	protected void setValues(double[] X, int start, int end) throws Exception {
		int size = 0;
		for(Criteria criteria : children) {
			size += criteria.getSize();
		}
		if(size != end - start + 1) {
			throw new Exception("Incorrect arguments count");
		}

		for(Criteria criteria : children) {
			criteria.setValues(X, start, start + criteria.getSize() - 1);
			start += criteria.getSize();
		}
	}

	@Override
	public void refresh() {
		operator.refresh();
	}

	@Override
	protected double getValue() {
		return operator.getValue();
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
		return operator.toString();
	}
}
