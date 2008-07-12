package ru.yandex.utils.corba;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.log4j.Logger;
import org.omg.CORBA.SystemException;
import org.omg.CORBA.UserException;
import org.springframework.beans.factory.annotation.Required;

import ru.yandex.spring.corba.PropertiesProvider;
import ru.yandex.spring.corba.client.CachingServiceFactory;
import ru.yandex.spring.corba.client.ServiceInformation;
import ru.yandex.spring.corba.client.template.DescribingServiceCallback;
import ru.yandex.spring.corba.support.CorbaExceptionTranslator;

public class ServantProxyInterceptor<S> implements MethodInterceptor {
	
	private static final Logger logger = Logger.getLogger(ServantProxyInterceptor.class);  
	
	private String nameServicePath;
	private PropertiesProvider propertiesProvider;

	private int servantsCount = 10;
	private int servantMaxUseCount = 100;
	
	private Class<S> targetClass;
	private String path;
	private List<String> pathList;
	
	private BlockingQueue<BlockingServiceTemplate<S>> serviceTemplateQueue = new LinkedBlockingQueue<BlockingServiceTemplate<S>>();
	private int currentPath = 0;
	private int servantCreated = 0;
	
	private Object monitor = new Object();
	private Object currentPathMonitor = new Object();
	
	public String getPath() {
		if(path != null) {
			return path;
		} else {
			synchronized (currentPathMonitor) {
				String p = pathList.get(currentPath++);
				currentPath %= pathList.size();
				return p;
			}
		}
	}
	
	protected BlockingServiceTemplate<S> getServiceTemplate() throws InterruptedException {
		BlockingServiceTemplate<S> serviceTemplate = null;
		if(servantCreated < servantsCount) {
			serviceTemplate = new BlockingServiceTemplate<S>(this);
			synchronized (monitor) {
				servantCreated ++;
			}
		} else {
			serviceTemplate = serviceTemplateQueue.take();
			if(!serviceTemplate.take()) {
				serviceTemplate.close();
				serviceTemplate = new BlockingServiceTemplate<S>(this);
			}
		}
		return serviceTemplate;
	}

	@SuppressWarnings("unchecked")
	public Object invoke(final MethodInvocation methodInvocation) throws Throwable {
		final BlockingServiceTemplate<S> serviceTemplate = getServiceTemplate();
        if (methodInvocation.getMethod().getDeclaringClass().equals(ServiceInformation.class)) {
            return methodInvocation.getMethod().invoke(serviceTemplate, methodInvocation.getArguments());
        }
        Object result = serviceTemplate.executePassingUserException(new DescribingServiceCallback() {
            public Object doInService(Object service) throws UserException {
                try {
                    return methodInvocation.getMethod().invoke(service, methodInvocation.getArguments());
                } catch (InvocationTargetException e) {
                    Throwable cause = e.getCause();
                    if (cause instanceof SystemException) {
                        throw (SystemException) cause;
                    }
                    if (cause instanceof UserException) {
                        throw (UserException) cause;
                    }
                    throw new CorbaExceptionTranslator().translateException(e);
                } catch (IllegalAccessException e) {
                    throw new CorbaExceptionTranslator().translateException(e);
                }
            }

            public String getDescription() {
                CachingServiceFactory serviceFactory = serviceTemplate.getServiceFactory();
                String invocationString = serviceFactory.getServiceClass().getName()
                        + "." + methodInvocation.getMethod().getName();
                return invocationString + " of " + serviceFactory.getServiceClass();
            }
        });
        serviceTemplate.returnToQueue();
        return result;
	}
	
	public void returnToQueue(BlockingServiceTemplate<S> serviceTemplate) {
		try {
			serviceTemplateQueue.put(serviceTemplate);
		} catch (InterruptedException e) {
			logger.error("Cannot return servant to queue", e);
		}
	}


	public Class<S> getTargetClass() {
		return targetClass;
	}

	@Required
	public void setTargetClass(Class<S> targetClass) {
		this.targetClass = targetClass;
	}

	public int getServantMaxUseCount() {
		return servantMaxUseCount;
	}
	
	public String getNameServicePath() {
		return nameServicePath;
	}

	public PropertiesProvider getPropertiesProvider() {
		return propertiesProvider;
	}

	@Required
	public void setPath(String path) {
		if(path.indexOf(',') >= 0) {
			StringTokenizer st = new StringTokenizer(path, ", \n\r\t");
			this.pathList = new ArrayList<String>();
			while(st.hasMoreTokens()) {
				pathList.add(st.nextToken());
			}				
		} else {
			this.path = path;
		}
	}

	@Required
	public void setNameServicePath(String nameServicePath) {
		this.nameServicePath = nameServicePath;
	}

	@Required
	public void setPropertiesProvider(PropertiesProvider propertiesProvider) {
		this.propertiesProvider = propertiesProvider;
	}

	@Required
	public void setServantsCount(int servantsCount) {
		this.servantsCount = servantsCount;
	}

	@Required
	public void setServantMaxUseCount(int servantMaxUseCount) {
		this.servantMaxUseCount = servantMaxUseCount;
	}

}
