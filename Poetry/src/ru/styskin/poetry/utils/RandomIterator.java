package ru.styskin.poetry.utils;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

public class RandomIterator<T> implements Iterator<T> {
	
	private Map<Double, T> sort = new TreeMap<Double, T>();
	private Iterator<Double> it;
	
	public <S extends Collection<T>> RandomIterator(S collection, T ex) {
		for(T e : collection) {
			if(e != ex) {
				sort.put(Math.random(), e);
				
			}
		}
		it = sort.keySet().iterator();
	}

	public boolean hasNext() {
		return it.hasNext();
	}

	public T next() {
		return sort.get(it.next());
	}

	public void remove() {
	}

}
