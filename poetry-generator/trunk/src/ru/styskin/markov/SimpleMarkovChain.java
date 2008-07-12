package ru.styskin.markov;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.yandex.utils.Pair;
import ru.yandex.utils.SetUtils;

public class SimpleMarkovChain<T> implements MarkovChain<T> {
	
	private Map<T, List<Pair<Double, T>>> map = new HashMap<T, List<Pair<Double,T>>>();
	
	private T separator;
	
	public SimpleMarkovChain(T separator) {
		this.separator = separator;
	}	

	public void addWord(Collection<T> word) {		
		
	}
	
	public void init() {
		
	}
	
	private final Comparator<Pair<Double, T>> COMPARATOR = new Comparator<Pair<Double, T>>() {
		public int compare(Pair<Double, T> o1, Pair<Double, T> o2) {
			return Double.compare(o1.getFirst(), o2.getFirst());
		}
	};
	
	public T getNextLetter(T letter) {
		double r = Math.random();
		Pair<Double, T> p = new Pair<Double, T>(r, null);
		List<Pair<Double, T>> list = map.get(letter);
		int index = SetUtils.find(p, list, COMPARATOR);
		return list.get(index).getSecond();
	}
	
	public Collection<T> generateWord() {
		List<T> word = new ArrayList<T>();
		T current = separator;
		while(true) {
			current = getNextLetter(current);
			if(separator.equals(current)) {
				break;
			} else {
				word.add(current);
			}
		}
		return word; 
	}

}
