package org.styskin.ca.functions;

import it.unimi.dsi.fastutil.doubles.DoubleList;

import java.util.Map;

import org.styskin.ca.functions.complex.ComplexOperator;
import org.styskin.ca.model.Slice;

public class IntegralCriteria extends ComplexCriteria {
	
	private SaveLoadFunction function;
	
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

		public void loadParameters(Map<String, Double> parameters) {
			op.loadParameters(parameters);
			function.loadParameters(parameters);
		}

		public void saveParameters(Map<String, Double> map) {
			op.saveParameters(map);
			function.saveParameters(map);
		}

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
			try {
				return new IntegralOperator(op.clone());
			} catch (Exception e) {
				throw new CloneNotSupportedException();
			}
		}

		@Override
		public ComplexOperator cloneEquals() throws Exception {
			try {
				return new IntegralOperator(op.cloneEquals());
			} catch (Exception e) {
				throw new CloneNotSupportedException();
			}
		}	
		
		
		public DoubleList getWeights() {
			return op.getWeights();
		}
		
		public void setWeight(int index, double w) {
			op.setWeight(index, w);
		}
		
		public void setWeights(DoubleList weights) {
			op.setWeights(weights);
		}
		
		@Override
		public String toString() {
			return op.toString();
		}
	}
	
	public double getValue(double[] X) throws Exception {		
		return function.getValue(getValues(X));
	}
	
	public ComplexOperator getOperator() {		
		return operator;
	}
	
	public IntegralCriteria(ComplexOperator operator) throws Exception {
		this(operator, new LinearFunction());		
	}

	public IntegralCriteria(ComplexOperator operator, SaveLoadFunction function) throws Exception {
		super(operator);
		this.operator = new IntegralOperator(operator);
		this.function = function;
	}

}
