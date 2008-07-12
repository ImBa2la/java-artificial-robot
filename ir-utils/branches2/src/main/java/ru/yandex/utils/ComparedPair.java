package ru.yandex.utils;

public class ComparedPair<F extends Comparable<? super F>, S extends Comparable<? super S>> extends Pair<F,S> implements Comparable<Pair<F,S>> {

	public ComparedPair(F first, S second) {
		super(first, second);
	}
	
	public static <F extends Comparable<? super F>, S extends Comparable<? super S>> ComparedPair<F,S> makePair(F f, S s) {
		return new ComparedPair<F, S> (f, s); 		
	}

	public int compareTo(Pair<F, S> o) {		
		return getFirst().compareTo(o.getFirst()) == 0? getSecond().compareTo(o.getSecond()) : getFirst().compareTo(o.getFirst());
	}
}
