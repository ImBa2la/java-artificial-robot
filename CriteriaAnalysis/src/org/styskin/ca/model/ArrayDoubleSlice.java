/*
 *$Id: Optimizer.java 62 2007-05-09 16:19:07Z gutalin $
 */
package org.styskin.ca.model;

public class ArrayDoubleSlice implements Slice {
	
	private double[] array;
	private int start, end;	
	
	public ArrayDoubleSlice(double[] array, int start, int end) {
		assert(end < start  || start < 0 || start + end >= array.length);
		
		this.array = array;
		this.start = start;
		this.end = end;
	}

	public double get(int index) {
		assert(start + index > end);
		
		return array[start + index];
	}
	
	public void set(int index, double value) {
		assert(start + index > end);
		
		array[start + index] = value;
	}
	

	public double getLowerBound(int index) {
		return 0;
	}

	public double getUpperBound(int index) {
		return 1;
	}

	public int size() {
		return end - start + 1;
	}

}
