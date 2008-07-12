package ru.yandex.utils;

/**
 * An object that holds cached objects. One can retrieve object from cache
 * by getCached() method.
 *
 * @author Dmitry Semyonov, <a href="mailto:deemonster@yandex-team.ru">
 */
public interface ObjectCache<T> {
	/**
	 * Retrieve object from cache. If cache have an object, then equals to given
	 * object, cached value will be given. Otherwise, given object will be cached and returned.
	 * @param object some object, which will be cached, if haven't cached yet.
	 * @return cached value.
	 */
	public T getCached(T object);
}
