package ru.yandex.utils.corbaOld;

public interface Servant<S> {
	
	Class<S> getTargetClass();
	
	S getServantInstance();
	
	boolean take();
	
	void close();

}
