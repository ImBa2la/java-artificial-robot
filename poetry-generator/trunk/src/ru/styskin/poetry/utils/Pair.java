package ru.styskin.poetry.utils;

public class Pair<F,S> {
	
	private F first;
	private S second;
	
	public Pair(F first, S second) {
		this.first = first;
		this.second = second;
	}
	
	public static <F, S> Pair<F,S> makePair(F f, S s) {
		return new Pair<F, S> (f, s); 		
	}
	
	public static <F extends Comparable<? super F>, S extends Comparable<? super S>> ComparedPair<F,S> makePair(F f, S s) {
		return new ComparedPair<F, S> (f, s); 		
	}
	
	public F getFirst() {
		return first;
	}

	public S getSecond() {
		return second;
	}
	
	public void setFirst(F f) {
		first = f;
	}

	public void setSecond(S s) {
		second = s;
	}
		
	public boolean equals(Pair<? extends F, ? extends S> pair) {
		return first.equals(pair.getFirst()) && second.equals(pair.getSecond());				
	}
	
	public int hashCode() {
		return first.hashCode() << 8 - 1 ^ second.hashCode();		
	}
}
