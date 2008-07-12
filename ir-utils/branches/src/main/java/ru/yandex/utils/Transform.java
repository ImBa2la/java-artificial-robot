package ru.yandex.utils;

/**
 * Use this with map function.
 *
 * @author Dmitry Semyonov <deemonster@yandex-team.ru>
 */
public interface Transform<F, T> {
	T transform(F val);
}