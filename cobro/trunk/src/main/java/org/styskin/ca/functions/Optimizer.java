package org.styskin.ca.functions;

import org.styskin.ca.model.CriteriaXMLParser.Optimize;

public interface Optimizer {

	Criteria optimize(Optimize op) throws Exception;

}