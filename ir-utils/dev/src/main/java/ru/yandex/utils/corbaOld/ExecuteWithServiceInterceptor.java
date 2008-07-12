package ru.yandex.utils.corbaOld;

import java.lang.reflect.InvocationTargetException;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang.Validate;
import org.omg.CORBA.SystemException;
import org.omg.CORBA.UserException;

import ru.yandex.spring.corba.client.CachingServiceFactory;
import ru.yandex.spring.corba.client.ServiceInformation;
import ru.yandex.spring.corba.client.template.DescribingServiceCallback;
import ru.yandex.spring.corba.client.template.ServiceTemplate;
import ru.yandex.spring.corba.support.CorbaExceptionTranslator;

class ExecuteWithServiceInterceptor<S> implements MethodInterceptor {

    private ServiceTemplate<S> serviceTemplate;

    public ExecuteWithServiceInterceptor(CachingServiceFactory<S> serviceFactory) {
        Validate.notNull(serviceFactory);

        serviceTemplate = new ServiceTemplate<S>(serviceFactory);
    }


    @SuppressWarnings("unchecked")
	public Object invoke(final MethodInvocation methodInvocation) throws Throwable {
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
        return result;
    }
}
