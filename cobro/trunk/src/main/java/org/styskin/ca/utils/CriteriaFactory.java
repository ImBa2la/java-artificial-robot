package org.styskin.ca.utils;

import org.springframework.beans.factory.FactoryBean;
import org.styskin.ca.functions.Criteria;
import org.styskin.ca.model.CriteriaXMLParser;

public class CriteriaFactory implements FactoryBean {
	
	private String path;

	public Object getObject() throws Exception {
		return CriteriaXMLParser.loadXML(path);
	}

	public Class<?> getObjectType() {
		return Criteria.class;
	}

	public boolean isSingleton() {
		return false;
	}

	public void setPath(String path) {
		this.path = path;
	}

}
