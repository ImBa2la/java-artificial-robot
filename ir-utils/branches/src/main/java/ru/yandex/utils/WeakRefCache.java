package ru.yandex.utils;

import java.lang.ref.WeakReference;
import java.util.WeakHashMap;

/**
 * Object cache implementation, which uses WeakReference to hold objects.
 * If there is no strong references to cached value, it will be collected by GC.
 * This class does not holds strong references to cached values.
 *
 * @author Dmitry Semyonov, <a href="mailto:deemonster@yandex-team.ru">
 *
 */
public class WeakRefCache<T> implements ObjectCache<T> {
	private final WeakHashMap<T, WeakReference<T>> table;

	public WeakRefCache() {
		table = new WeakHashMap<T, WeakReference<T>>();
	}

	/**
	 * {@inheritDoc}
	 */
	public T getCached(T o) {
		WeakReference<T> ref = table.get(o);
		if (null == ref) {
			putToCache(o);
			return o;
		}
		T cached = ref.get();
		if (null == cached) {
			putToCache(o);
			return o;
		}

		return cached;
	}

	private void putToCache(T o) {
		table.put(o, new WeakReference<T>(o));
	}

	/**
	 * Returns a synchronized (thread-safe) cache backed by the specified cache.
	 * @param cache the cache to be "wrapped" in a synchronized cache.
	 * @return a synchronized view of the specified cache.
	 */
	public static <T> ObjectCache<T> getSyncronized(ObjectCache<T> cache) {
		return new SyncronizedCache<T>(cache);
	}

	private static class SyncronizedCache<T> implements ObjectCache<T> {
		private final Object lock;
		private final ObjectCache<T> cache;

		public SyncronizedCache(ObjectCache<T> nestedCache) {
			lock = this;
			cache = nestedCache;
		}

		public T getCached(T object) {
			synchronized (lock) {
				return cache.getCached(object);
			}
		}
	}
}
