package ru.yandex.utils;

import java.util.Collection;

public interface MultiMap<K, V> {	
	
	boolean put(K k, V v);
	
	Collection<V> get(K k);
	
}
