package org.styskin.ca.model;

public class Pair<T1, T2> {
	T1 o1;
	T2 o2;

	public Pair(T1 o1, T2 o2) {
		this.o1 = o1;
		this.o2 = o2;
	}

	public T1 getFirst() {
		return o1;
	}

	public T2 getSecond() {
		return o2;
	}

	public void setFirst(T1 o1) {
		this.o1 = o1;
	}

	public void setSecond(T2 o2) {
		this.o2 = o2;
	}
	public boolean equals(Pair p2) {
		return o1.equals(p2.getFirst()) && o2.equals(p2.getSecond());
	}

	public String toString() {
		return "[" + o1.toString() + "; " + o2.toString() + "]";
	}
}