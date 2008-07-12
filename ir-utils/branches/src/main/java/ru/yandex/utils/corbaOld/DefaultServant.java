package ru.yandex.utils.corbaOld;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

import org.aopalliance.intercept.Interceptor;
import org.apache.log4j.Logger;
import org.omg.CORBA.ORB;
import org.springframework.aop.framework.ProxyFactory;

import ru.yandex.spring.corba.NamingContextProvider;
import ru.yandex.spring.corba.NamingContextProviderImpl;
import ru.yandex.spring.corba.ObjectSource;
import ru.yandex.spring.corba.OrbProviderImpl;
import ru.yandex.spring.corba.ResolveNameObjectSource;
import ru.yandex.spring.corba.client.CachingServiceFactoryImpl;
import ru.yandex.spring.corba.client.NarrowServiceFactory;
import ru.yandex.spring.corba.client.ServiceInformation;

public class DefaultServant<S> implements Servant<S> {
	
	private static final Logger logger = Logger.getLogger(DefaultServant.class);
	
	private AtomicInteger counter;

	private ORB orb;
	private Class<S> targetClass;
	private S servant;
	
	private BlockingQueue<Servant<S>> servantQueue;
	
	@SuppressWarnings("unchecked")
	public DefaultServant(ServantProviderWithSLB<S> servantProvider) {
		targetClass = servantProvider.getTargetClass();
		try {
			OrbProviderImpl orbProvider = new OrbProviderImpl();
			orbProvider.setPropertiesProvider(servantProvider.getPropertiesProvider());		
			NamingContextProvider namingContextProvider = new NamingContextProviderImpl(orbProvider, servantProvider.getNameServicePath());
			ObjectSource objectSource = new ResolveNameObjectSource(servantProvider.getPath(), namingContextProvider);
			orb = orbProvider.getOrb();
			
			Interceptor rebindOnFailureInterceptor = new ExecuteWithServiceInterceptor<S>(new CachingServiceFactoryImpl<S>(new NarrowServiceFactory<S>(objectSource, targetClass))); 
	        ProxyFactory proxyFactory = new ProxyFactory();
	        proxyFactory.addAdvice(rebindOnFailureInterceptor);
	        proxyFactory.addInterface(targetClass);
	        proxyFactory.addInterface(ServiceInformation.class);
	        servant = (S) proxyFactory.getProxy();	        
			counter = new AtomicInteger(servantProvider.getServantMaxUseCount());
			servantQueue = servantProvider.getServantQueue();
		} catch(Throwable e) {
			counter = new AtomicInteger(0);
			logger.fatal("Cannot create servant", e);
		}
	}
	
	public boolean take() {
		return counter.decrementAndGet() >= 0;
	}

	public void close() {
		try {
			if(counter.get() == 0) {
				orb.shutdown(false);
				logger.debug("Shutdowning orb");
			} else {
				servantQueue.put(this);
			}
		} catch (Throwable e) {
			logger.error("Cannot return servant to queue", e);
		}
	}
	
	public S getServantInstance() {
		return servant;
	}

	public Class<S> getTargetClass() {
		return targetClass;
	}
}
