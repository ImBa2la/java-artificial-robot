/*
 *$Id$
 */
package org.styskin.ca.model;

public class Pair<T1, T2> {
	private T1 o1;
	private T2 o2;

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
	
	public String toString() {
		return "[" + o1.toString() + "; " + o2.toString() + "]";
	}
	
	public static <T1,T2> Pair<T1, T2> makePair(T1 o1, T2 o2) {
		return new Pair<T1, T2> (o1, o2);	
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((o1 == null) ? 0 : o1.hashCode());
		result = prime * result + ((o2 == null) ? 0 : o2.hashCode());
		return result;
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
		final Pair other = (Pair) obj;
		if (o1 == null) {
			if (other.o1 != null)
				return false;
		} else if (!o1.equals(other.o1))
			return false;
		if (o2 == null) {
			if (other.o2 != null)
				return false;
		} else if (!o2.equals(other.o2))
			return false;
		return true;
	}
	
	
}
