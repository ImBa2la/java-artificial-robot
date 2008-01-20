package ru.yandex.utils.corba;

import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Required;

import ru.yandex.spring.corba.client.ServiceInformation;

public class ServantProxyFactory<S> implements FactoryBean, ServiceInformation {
	
	private ServantProxyInterceptor<S> interceptor;
	private Class<S> targetClass;

	public Object getObject() throws Exception {
        ProxyFactory proxyFactory = new ProxyFactory();
        proxyFactory.addAdvice(interceptor);
        proxyFactory.addInterface(interceptor.getTargetClass());
        proxyFactory.addInterface(ServiceInformation.class);
        return proxyFactory.getProxy();	        
	}

	public Class<S> getObjectType() {
		return targetClass;
	}

	public boolean isSingleton() {
		return true;
	}

	public String getOrigin() {
		return interceptor.getPath();
	}

	@Required
	public void setInterceptor(ServantProxyInterceptor<S> interceptor) {
		this.interceptor = interceptor;
	}

	@Required
	public void setTargetClass(Class<S> targetClass) {
		this.targetClass = targetClass;
	}
	
}
