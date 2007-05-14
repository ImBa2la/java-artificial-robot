/*
 *$Id: Optimizer.java 62 2007-05-09 16:19:07Z gutalin $
 */
package org.styskin.ca.model;

public interface Slice {
	
	double getLowerBound(int index);

	double getUpperBound(int index);
	
	double get(int index);
	
	void set(int index, double value);
	
	int size();
	
}
