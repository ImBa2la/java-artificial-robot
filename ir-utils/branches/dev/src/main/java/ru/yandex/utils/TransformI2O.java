package ru.yandex.utils;
import ru.yandex.utils.Transform;


/**
 * Typed transform - transofrm integer value to Object value.
 *
 * @author Dmitry Semyonov <deemonster@yandex-team.ru>
 */
public interface TransformI2O<T> extends Transform<Integer, T> {
	T transform(int from);
}
