/*
 *$Id$
 */
package org.styskin.ca.model;

import java.text.NumberFormat;
import java.util.Locale;

public interface Constants {
	final public double EPS = 1E-7;
	final public double MAX_DOUBLE = 1E7;
	
	final public NumberFormat FORMAT = NumberFormat.getNumberInstance(Locale.US);
}