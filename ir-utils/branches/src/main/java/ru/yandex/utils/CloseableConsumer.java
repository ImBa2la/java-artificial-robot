package ru.yandex.utils;

import java.io.Closeable;

/**
 * Consumer with close() method.
 * 
 * @author Dmitry Semyonov <deemonster@yandex-team.ru>
 */
public interface CloseableConsumer<T> extends Consumer<T>, Closeable {
}