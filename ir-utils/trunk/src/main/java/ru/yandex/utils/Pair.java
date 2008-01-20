package ru.yandex.utils;

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

	@Override
	public String toString() {
		return "[" + String.valueOf(first) + "; " + String.valueOf(second) + "]";
	}
		
	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final Pair pair = (Pair) obj;
		return first.equals(pair.getFirst()) && second.equals(pair.getSecond());				
	}
	
	public int hashCode() {
		return first.hashCode() << 8 - 1 ^ second.hashCode();		
	}
}
