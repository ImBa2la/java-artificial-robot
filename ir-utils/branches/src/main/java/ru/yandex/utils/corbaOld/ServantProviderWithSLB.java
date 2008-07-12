package ru.yandex.utils.corbaOld;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.log4j.Logger;

import ru.yandex.spring.corba.PropertiesProvider;

public class ServantProviderWithSLB<S> implements ServantProvider<S> {
	
	private static final Logger logger = Logger.getLogger(ServantProviderWithSLB.class); 
	
	private String nameServicePath;
	private PropertiesProvider propertiesProvider;

	private int servantsCount = 10;
	
	private int servantMaxUseCount = 100;
	
	private Class<S> targetClass;
	private String path;
	// For hand-made SLB:
	private List<String> pathList;
	
	private BlockingQueue<Servant<S>> servantQueue;
	private int currentPath = 0;
	private int servantCreated = 0;
	
	protected String getPath() {
		if(path != null) {
			return path;
		} else {
			String p = pathList.get(currentPath++);
			currentPath %= pathList.size();
			return p;
		}
	}

	/* (non-Javadoc)
	 * @see ru.yandex.market.supercontroller.corba.ServantProvider#getServant()
	 */
	public synchronized Servant<S> getServant() {
		Servant<S> servant = null;
		if(servantCreated < servantsCount) {
			servant = new DefaultServant<S>(this);
			servantCreated ++;
		} else {
			try {
				servant = servantQueue.take();
			} catch (InterruptedException e) {
				logger.fatal("Waiting for CORBA client was interrupted", e);
			}
			if(servant == null || !servant.take()) {
				servant = new DefaultServant<S>(this);
			}
		}
		return servant;
	}
	public void setWorkerPoolSize(int workedPoolSize) {
		servantsCount = workedPoolSize;
		servantQueue = new LinkedBlockingQueue<Servant<S>>();
	}

	/* (non-Javadoc)
	 * @see ru.yandex.market.supercontroller.corba.ServantProvider#setTargetClass(java.lang.Class)
	 */
	public void setTargetClass(Class<S> targetClass) {
		this.targetClass = targetClass;
	}

	/* (non-Javadoc)
	 * @see ru.yandex.market.supercontroller.corba.ServantProvider#setPath(java.lang.String)
	 */
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
	
	public void setServantMaxUseCount(int servantMaxUseCount) {
		this.servantMaxUseCount = servantMaxUseCount;
	}
	
	public void setPropertiesProvider(PropertiesProvider propertiesProvider) {
		this.propertiesProvider = propertiesProvider;
	}
	
	/* (non-Javadoc)
	 * @see ru.yandex.market.supercontroller.corba.ServantProvider#setNameServicePath(java.lang.String)
	 */
	public void setNameServicePath(String nameServicePath) {
		this.nameServicePath = nameServicePath;
	}

	/* (non-Javadoc)
	 * @see ru.yandex.market.supercontroller.corba.ServantProvider#getNameServicePath()
	 */
	public String getNameServicePath() {
		return nameServicePath;
	}

	public PropertiesProvider getPropertiesProvider() {
		return propertiesProvider;
	}

	public int getServantMaxUseCount() {
		return servantMaxUseCount;
	}

	public BlockingQueue<Servant<S>> getServantQueue() {
		return servantQueue;
	}

	/* (non-Javadoc)
	 * @see ru.yandex.market.supercontroller.corba.ServantProvider#getTargetClass()
	 */
	public Class<S> getTargetClass() {
		return targetClass;
	}
	
}
