package ru.yandex.utils.corbaOld;

public interface ServantProvider<S> {

	Servant<S> getServant();

	void setTargetClass(Class<S> targetClass);

	void setPath(String path);

	Class<S> getTargetClass();
}