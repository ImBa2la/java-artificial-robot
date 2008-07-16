package ru.styskin.markov;

import static ru.yandex.utils.IteratorUtils.DOUBLE_SUM;
import static ru.yandex.utils.IteratorUtils.reduce;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.yandex.utils.Accept;
import ru.yandex.utils.IteratorUtils;
import ru.yandex.utils.Pair;
import ru.yandex.utils.SetUtils;
import ru.yandex.utils.Transform;

public class ComplexMarkovChain<T> implements MarkovChain<T> {
	
	private List<Pair<List<T>, Double>> firstT = new ArrayList<Pair<List<T>,Double>>();
	private Map<List<T>, List<Pair<Double, T>>> map = new HashMap<List<T>, List<Pair<Double,T>>>();
	private int maxWord = 4;
	private double probability = 0.8;
	
	private Accept<List<T>> acceptor;
	
	public ComplexMarkovChain(final T separator) {
		acceptor = new Accept<List<T>>() {
			public boolean accept(List<T> o) {			
				return separator.equals(o.get(o.size()-1));
			}
		};
	}
	
	public ComplexMarkovChain(Accept<List<T>> acceptor) {
		this.acceptor = acceptor;
	}

	public void init(Iterable<Collection<T>> words) {
		Map<List<T>, Integer> m = new HashMap<List<T>, Integer>();
		Map<List<T>, Integer> fm = new HashMap<List<T>, Integer>();
		for(Collection<T> c : words) {
			List<T> list = new ArrayList<T>(c);
			for(int i=0; i < list.size(); i++) {
				for(int j=i+2; j <= list.size() && j <= i + maxWord + 1; j++) {
					if(i == 0)
						SetUtils.increment(fm, list.subList(i, j));
					SetUtils.increment(m, list.subList(i, j));
				}
			}
		}		
		for(Map.Entry<List<T>, Integer> entry : m.entrySet()) {
			List<T> key = entry.getKey();
			List<T> first = key.subList(0, key.size()-1);
			T second = key.get(key.size()-1); 
			List<Pair<Double, T>> list = map.get(first);
			if(list == null) {
				list = new ArrayList<Pair<Double,T>>();
				map.put(first, list);
			}
			list.add(new Pair<Double, T>((double)entry.getValue().intValue(), second));		
		}
		final Transform<Pair<Double, T>, Double> TRANSFORM = new Transform<Pair<Double, T>, Double>() {
			public Double transform(Pair<Double, T> val) {
				return val.getFirst();
			}
		};
		final Transform<Map.Entry<List<T>, Integer>, Double> TRANSFORM2 = new Transform<Map.Entry<List<T>, Integer>, Double>() {
			public Double transform(Map.Entry<List<T>, Integer> val) {
				return (double)val.getValue().intValue();
			}
		};

		double sum2 = reduce(DOUBLE_SUM, IteratorUtils.map(fm.entrySet().iterator(), TRANSFORM2));
		double accum2 = 0;
		for(List<T> key : fm.keySet()) {
			double t = fm.get(key);
			firstT.add(new Pair<List<T>, Double>(key, accum2));
			accum2 += t/sum2;
		}
		for(Map.Entry<List<T>, List<Pair<Double, T>>> entry : map.entrySet()) {
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
	
	public T extractLetter(List<T> list, int size) {
		List<T> sub = list.subList(list.size()-size, list.size());
		List<Pair<Double, T>> l = map.get(sub);
		if(l == null)
			return null;
		double rr = Math.random();
		Pair<Double, T> p = new Pair<Double, T>(rr, null);
		int index = SetUtils.find(p, l, COMPARATOR);
		return l.get(index).getSecond();		
	}

	public T getNextLetter(List<T> list) {
		for(int size = Math.min(maxWord, list.size()); size >= 1; size--) {
			double r = Math.random();
			if(size > 2 && r > probability) {
				continue;
			}
			T t = extractLetter(list, size);
			if(t != null)
				return t;
		}
		return null;
	}
	
	private final Comparator<Pair<List<T>, Double>> FIRST_CMP = new Comparator<Pair<List<T>, Double>>() {
		public int compare(Pair<List<T>, Double> o1, Pair<List<T>, Double> o2) {
			return Double.compare(o1.getSecond(), o2.getSecond());
		}
	};
	
	private final Comparator<Pair<Double, T>> COMPARATOR = new Comparator<Pair<Double, T>>() {
		public int compare(Pair<Double, T> o1, Pair<Double, T> o2) {
			return Double.compare(o1.getFirst(), o2.getFirst());
		}
	};

	public Collection<T> generateWord() {
		double r = Math.random();
		int index = SetUtils.find(new Pair<List<T>, Double>(null, r), firstT, FIRST_CMP);
		List<T> word = new ArrayList<T>(firstT.get(index).getFirst());
		while(!acceptor.accept(word)) {
			T next = getNextLetter(word);
			word.add(next);
		}
		return word;
	}

}
