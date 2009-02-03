/*
 *$Id$
 */
package org.styskin.ca.functions;

import java.util.ArrayList;
import java.util.List;

import org.styskin.ca.functions.complex.ComplexOperator;

public class ComplexCriteria extends Criteria {
	private List<Criteria> children = new ArrayList<Criteria>();
	private ComplexOperator operator;

	private int totalSize = -1;

	public List<Criteria> getChildren() {
		return children;
	}

	public ComplexCriteria(ComplexOperator operator) throws Exception {
		this.operator = operator;
	}

	public void addChild(Criteria criteria, double weight) {
		children.add(criteria);
		operator.addCriteria(weight);
	}

	public void removeChild(Criteria criteria) {
		int index = children.indexOf(criteria);
		children.remove(index);
		operator.getWeights().remove(index);
		operator.refresh();
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
		if(totalSize >= 0) {
			return totalSize;
		} else {
			totalSize = 0;
			for(Criteria childCriteria : children) {
				totalSize += childCriteria.getTotalSize();
			}
			return totalSize;
		}
	}

	public ComplexOperator getOperator() {
		return operator;
	}

	public void setChildren(List<Criteria> children) {
		this.children = children;
	}

	public void setOperator(ComplexOperator operator) {
		this.operator = operator;
	}

	@Override
	public Criteria clone() throws CloneNotSupportedException {
		ComplexCriteria criteria = (ComplexCriteria) super.clone();
		List<Criteria> childrenClone = new ArrayList<Criteria>();
		for(Criteria child : children) {
			childrenClone.add(child.clone());
		}
		criteria.setChildren(childrenClone);
		criteria.setOperator(operator.clone());
		return criteria;
	}
	
	@Override
	public Criteria cloneEquals() throws InstantiationException, IllegalAccessException, CloneNotSupportedException {
		ComplexCriteria criteria = (ComplexCriteria) super.cloneEquals();
		List<Criteria> childrenClone = new ArrayList<Criteria>();
		for(Criteria child : children) {
			childrenClone.add(child.cloneEquals());
		}
		criteria.setChildren(childrenClone);
		criteria.setOperator(operator.cloneEquals());
		return criteria;
	}	
	
	//TODO: don't forget about this method
//	@Override
//	public String toString() {
//		StringBuffer sb = new StringBuffer();
//		sb.append("(").append(operator.toString()).append(' ');
//		int i = 0;
//		for(Criteria child : children) {
//			sb.append("\n{").append(FORMAT.format(operator.getWeights().get(i++))).append(" - ").append(child.toString()).append('}');
//		}
//		sb.append(')');
//		return sb.toString();
//	}
}
