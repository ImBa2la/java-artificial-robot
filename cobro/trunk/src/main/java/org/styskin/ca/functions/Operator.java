/*
 *$Id$
 */

package org.styskin.ca.functions;

import org.styskin.ca.model.Constants;

public abstract class Operator implements Constants, Cloneable {

	abstract public double getValue(double[] X) throws Exception;
}