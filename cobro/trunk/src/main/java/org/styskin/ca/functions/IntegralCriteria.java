package org.styskin.ca.functions;

import java.util.List;
import java.util.Map;

import org.styskin.ca.functions.complex.ComplexOperator;
import org.styskin.ca.math.Function;
import org.styskin.ca.model.Slice;

public class IntegralCriteria extends ComplexCriteria {
	
	private Function function;
	
	private ComplexOperator operator;
	
	private class IntegralOperator extends ComplexOperator {
		
		ComplexOperator op;
		
		public IntegralOperator(ComplexOperator op) throws Exception {
			this.op = op;			
		}

		@Override
		public double getKsi(double x) {
			return op.getKsi(x);
		}

		@Override
		public Slice getParameters() {
			return op.getParameters();
		}

		@Override
		public double getPhi(double x) {
			return op.getPhi(x);
		}

		@Override
		public void load(Map<String, Double> parameters) {
			op.load(parameters);			
		}

		@Override
		public Map<String, Double> save() {
			return op.save();
		}

		@Override
		public double getValue(double[] X) throws Exception {
			return function.getValue(op.getValue(X));
		}
		
		public void refresh() {
			op.refresh();
		}

		public void addCriteria(double weight) {
			op.addCriteria(weight);
		}

		public int getSize() {
			return op.getSize();
		}
		
		public String operatorType() {
			return op.operatorType();
		}
		
		@Override
		public ComplexOperator clone() throws CloneNotSupportedException {
			IntegralOperator n = (IntegralOperator) super.clone();
			n.op = op;
			return n;
		}
		
		public List<Double> getWeights() {
			return op.getWeights();
		}
		
		public void setWeights(List<Double> weights) {
			op.setWeights(weights);
		}
		
		@Override
		public String toString() {
			return op.toString();
		}
	}
	
	public ComplexOperator getOperator() {		
		return operator;
	}
	

	public IntegralCriteria(ComplexOperator operator, Function function) throws Exception {
		super(operator);
		this.function = function;
		this.operator = new IntegralOperator(operator);
	}

}
