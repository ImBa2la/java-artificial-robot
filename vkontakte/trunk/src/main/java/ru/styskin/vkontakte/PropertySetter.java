package ru.styskin.vkontakte;

public interface PropertySetter<U, S> {
	
	void workOn(U u, S s);
}
