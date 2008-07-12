package ru.yandex.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultMultiMap<K, V> implements MultiMap<K, V> {
	
	private Map<K, List<V>> map = new HashMap<K, List<V>>();

	public Collection<V> get(K k) {
		return map.get(k);
	}

	public boolean put(K k, V v) {
		List<V> list = map.get(k);
		if(list == null) {
			list = new ArrayList<V>();
			map.put(k, list);
		}
		list.add(v);
		return true;
	}

	public boolean containsKey(K key) {
		return map.containsKey(key);
	}

}
