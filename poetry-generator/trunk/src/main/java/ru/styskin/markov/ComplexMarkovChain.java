package ru.styskin.markov;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.yandex.utils.Pair;

public class ComplexMarkovChain<T> implements MarkovChain<T> {
	
	private Map<List<T>, List<Pair<Double, T>>> map = new HashMap<List<T>, List<Pair<Double,T>>>();
	
	public void init(Iterable<Collection<T>> words) {
		
	}


	public Collection<T> generateWord() {
		return null;
	}

}
