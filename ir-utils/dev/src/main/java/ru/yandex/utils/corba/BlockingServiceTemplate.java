package ru.yandex.utils.corba;

import org.omg.CORBA.ORB;

import ru.yandex.spring.corba.NamingContextProvider;
import ru.yandex.spring.corba.NamingContextProviderImpl;
import ru.yandex.spring.corba.ObjectSource;
import ru.yandex.spring.corba.OrbProviderImpl;
import ru.yandex.spring.corba.ResolveNameObjectSource;
import ru.yandex.spring.corba.client.CachingServiceFactoryImpl;
import ru.yandex.spring.corba.client.NarrowServiceFactory;
import ru.yandex.spring.corba.client.template.ServiceTemplate;

public class BlockingServiceTemplate<S> extends ServiceTemplate<S> {
	
	private int counter;
	private ORB orb;
	private ServantProxyInterceptor<S> servantProxyInterceptor;
	
	public BlockingServiceTemplate(ServantProxyInterceptor<S> servantProxyInterceptor) {
		this.servantProxyInterceptor = servantProxyInterceptor;
		Class<S> targetClass = servantProxyInterceptor.getTargetClass();
		OrbProviderImpl orbProvider = new OrbProviderImpl();
		orbProvider.setPropertiesProvider(servantProxyInterceptor.getPropertiesProvider());		
		NamingContextProvider namingContextProvider = new NamingContextProviderImpl(orbProvider, servantProxyInterceptor.getNameServicePath());
		ObjectSource objectSource = new ResolveNameObjectSource(servantProxyInterceptor.getPath(), namingContextProvider);
		orb = orbProvider.getOrb();
		serviceFactory = new CachingServiceFactoryImpl<S>(new NarrowServiceFactory<S>(objectSource, targetClass));
		counter = servantProxyInterceptor.getServantMaxUseCount();
	}
	
	public boolean take() {
		return --counter >= 0;
	}
	
	public void close() {
		orb.shutdown(false);
	}
	
	public void returnToQueue() {
		servantProxyInterceptor.returnToQueue(this);
	}
}
