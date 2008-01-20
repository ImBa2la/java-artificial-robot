package ru.yandex.utils.corbaOld;

public class SimpleServant<S> implements Servant<S> {
	
	private S servant;
	private Class<S> targetClass;
	
	public SimpleServant(S servant, Class<S> targetClass) {
		this.servant = servant;		
	}

	public void close() {
		// nothing
	}

	public S getServantInstance() {
		return servant;
	}

	public Class<S> getTargetClass() {
		return targetClass;
	}

	public boolean take() {
		return true;
	}
}
