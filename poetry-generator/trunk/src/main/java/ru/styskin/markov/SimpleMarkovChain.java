package ru.styskin.markov;

import static ru.yandex.utils.IteratorUtils.DOUBLE_SUM;
import static ru.yandex.utils.IteratorUtils.reduce;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import ru.yandex.utils.IteratorUtils;
import ru.yandex.utils.Pair;
import ru.yandex.utils.SetUtils;
import ru.yandex.utils.Transform;

public class SimpleMarkovChain<T> implements MarkovChain<T> {
	
	private Map<T, List<Pair<Double, T>>> map = new HashMap<T, List<Pair<Double,T>>>();
	
	private T separator;
	
	public SimpleMarkovChain(T separator) {
		this.separator = separator;
	}	

	public void init(Iterable<Collection<T>> words) {
		Map<Pair<T, T>, Integer> m = new HashMap<Pair<T,T>, Integer>();
		for(Collection<T> w : words) {
			Iterator<T> it1 = w.iterator(), it2 = w.iterator();
			if(!it2.hasNext()) continue;
			it2.next();
			while(it2.hasNext()) {
				Pair<T, T> key = Pair.makePair(it1.next(), it2.next());
				SetUtils.increment(m, key);
			}
		}
		for(Map.Entry<Pair<T, T>, Integer> entry : m.entrySet()) {
			Pair<T, T> key = entry.getKey();
			List<Pair<Double, T>> list = map.get(key.getFirst());
			if(list == null) {
				list = new ArrayList<Pair<Double,T>>();
				map.put(key.getFirst(), list);
			}
			list.add(new Pair<Double, T>((double)entry.getValue().intValue(), key.getSecond()));		
		}
		final Transform<Pair<Double, T>, Double> TRANSFORM = new Transform<Pair<Double, T>, Double>() {
			public Double transform(Pair<Double, T> val) {
				return val.getFirst();
			}
		};
		for(Map.Entry<T, List<Pair<Double, T>>> entry : map.entrySet()) {
			List<Pair<Double, T>> list = entry.getValue();
			double sum = reduce(DOUBLE_SUM, IteratorUtils.map(list, TRANSFORM));
			double accum = 0;
			for(Pair<Double, T> pair : list) {
				double t = pair.getFirst();
				pair.setFirst(accum);
				accum += t/sum;
			}
		}
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
