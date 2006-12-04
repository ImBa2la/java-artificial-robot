package org.styskin.ca.model;

public interface Slice {
	
	double getLowerBound(int index);

	double getUpperBound(int index);
	
	double get(int index);
	
	void set(int index, double value);
	
	int size();
	
}
