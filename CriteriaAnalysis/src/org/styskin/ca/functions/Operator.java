package org.styskin.ca.functions;

import org.styskin.ca.model.Constants;

strictfp public abstract class Operator implements Constants {

	abstract public double getValue(double[] X) throws Exception;
}
