package ru.yandex.utils;

/**
 * Consumer interface - some object wich can consume objects.
 *
 * @author Dmitry Semyonov <deemonster@yandex-team.ru>
 */
public interface Consumer<T> {
	void add(T o);
}