package ru.yandex.utils;

public class PairCO<S> {
	
	private char first;
	private S second;
	
	public PairCO(char first, S second) {
		super();
		this.first = first;
		this.second = second;
	}

	public char getFirst() {
		return first;
	}

	public S getSecond() {
		return second;
	}

	public void setFirst(char f) {
		first = f;
	}

	public void setSecond(S s) {
		second = s;
	}
	
	public int getFirstI() {
		return first;
	}

	@Override
	public int hashCode() {
		return first << 8 - 1 ^ getSecond().hashCode();		
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		final PairCO other = (PairCO) obj;
		if (first != other.first)
			return false;
		if (second == null) {
			if (other.second != null)
				return false;
		} else if (!second.equals(other.second))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "[" + String.valueOf(first) + "; " + String.valueOf(second) + "]";
	}

	
}
