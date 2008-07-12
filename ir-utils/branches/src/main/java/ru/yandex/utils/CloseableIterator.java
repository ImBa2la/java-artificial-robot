package ru.yandex.utils;

import java.io.Closeable;
import java.util.Iterator;

/**
 * Iterator with close() method.
 * 
 * @author Dmitry Semyonov <deemonster@yandex-team.ru>
 */
public interface CloseableIterator<T> extends Iterator<T>, Closeable {
}