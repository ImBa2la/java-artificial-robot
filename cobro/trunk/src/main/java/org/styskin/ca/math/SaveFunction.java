package org.styskin.ca.math;

import java.util.Map;

public interface SaveFunction extends Function {

	void loadParameters(Map<String, Double> lambda);

	void saveParameters(Map<String, Double> lambda);

}
