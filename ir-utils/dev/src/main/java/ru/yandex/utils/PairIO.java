package ru.yandex.utils;

public class PairIO<S> {
	
	private int first;
	private S second;
	
	public PairIO(int first, S second) {
		this.first = first;
		this.second = second;
	}

	public S getSecond() {
		return second;
	}

	public void setFirst(int f) {
		first = f;
	}

	public void setSecond(S s) {
		second = s;
	}
	
	public int getFirst() {
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
		final PairIO other = (PairIO) obj;
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
