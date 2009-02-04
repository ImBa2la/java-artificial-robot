package org.styskin.ca.functions;

import java.util.Map;

public interface SaveLoadParameters {

	void loadParameters(Map<String, Double> lambda);

	void saveParameters(Map<String, Double> lambda);

}
