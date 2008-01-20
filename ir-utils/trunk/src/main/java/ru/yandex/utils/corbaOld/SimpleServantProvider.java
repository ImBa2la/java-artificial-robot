package ru.yandex.utils.corbaOld;

import ru.yandex.spring.corba.NamingContextProvider;
import ru.yandex.spring.corba.client.dynamic.CorbaServiceFactoryBean;

public class SimpleServantProvider<S> implements ServantProvider<S> {
	
	private Servant<S> simpleServant;
	private Class<S> targetClass;
	private String path;
	private NamingContextProvider namingContextProvider; 
	
	@SuppressWarnings("unchecked")
	public void init() throws Exception {
		CorbaServiceFactoryBean factory = new CorbaServiceFactoryBean() ;
		factory.setNamingContextProvider(namingContextProvider);
		factory.setPath(path);
		factory.setTargetClass(targetClass);
		simpleServant = new SimpleServant<S>((S) factory.getObject(), targetClass);		
	}

	public Servant<S> getServant() {
		return simpleServant;
	}

	public Class<S> getTargetClass() {
		return targetClass;
	}

	public void setPath(String path) {
		this.path = path;		
	}

	public void setTargetClass(Class<S> targetClass) {
		this.targetClass = targetClass;		
	}

	public void setNamingContextProvider(NamingContextProvider namingContextProvider) {
		this.namingContextProvider = namingContextProvider;
	}	
}
