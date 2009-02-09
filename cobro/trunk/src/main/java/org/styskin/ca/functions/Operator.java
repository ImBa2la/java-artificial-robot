/*
 *$Id$
 */

package org.styskin.ca.functions;

import org.styskin.ca.model.Constants;

public interface Operator extends Constants, Cloneable, SaveLoadParameters {

	double getValue(double[] X) throws Exception;
}
