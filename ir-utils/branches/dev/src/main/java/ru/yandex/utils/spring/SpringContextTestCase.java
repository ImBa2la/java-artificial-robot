package ru.yandex.utils.spring;

import org.apache.log4j.xml.DOMConfigurator;
import org.springframework.test.AbstractDependencyInjectionSpringContextTests;

public abstract class SpringContextTestCase extends AbstractDependencyInjectionSpringContextTests {
	
	public SpringContextTestCase() {
		super();
		DOMConfigurator.configure("bin/log4j-config.xml");
		setAutowireMode(AUTOWIRE_BY_NAME);		
	}
	
	protected abstract String[] getConfigLocations();
}
