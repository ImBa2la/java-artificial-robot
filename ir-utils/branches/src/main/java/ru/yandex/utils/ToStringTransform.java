package ru.yandex.utils;

/**
 * Use this to transform Objects to Strings with calling toString() method.
 * 
 * @author Dmitry Semyonov <deemonster@yandex-team.ru>
 */
public class ToStringTransform<T> implements Transform<T, String> {
	public String transform(T val) {
		return val.toString();
	}
}