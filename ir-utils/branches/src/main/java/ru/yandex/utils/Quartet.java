package ru.yandex.utils;

public class Quartet<A, B, C, D> {
	
	private A first;
	private B second;
	private C third;
	private D forth;
	
	public Quartet(A first, B second, C third, D forth) {
		this.first = first;
		this.second = second;
		this.third = third;
		this.forth = forth;
	}
	
	public static <A,B,C,D> Quartet<A,B,C,D>makeQuartet(A a, B b, C c, D d) {
		return new Quartet<A, B, C, D>(a, b, c, d);
	}	
	
	public A getFirst() {
		return first;
	}
	public void setFirst(A first) {
		this.first = first;
	}
	public B getSecond() {
		return second;
	}
	public void setSecond(B second) {
		this.second = second;
	}
	public C getThird() {
		return third;
	}
	public void setThird(C third) {
		this.third = third;
	}
	public D getForth() {
		return forth;
	}
	public void setForth(D forth) {
		this.forth = forth;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((first == null) ? 0 : first.hashCode());
		result = prime * result + ((forth == null) ? 0 : forth.hashCode());
		result = prime * result + ((second == null) ? 0 : second.hashCode());
		result = prime * result + ((third == null) ? 0 : third.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final Quartet other = (Quartet) obj;
		if (first == null) {
			if (other.first != null)
				return false;
		} else if (!first.equals(other.first))
			return false;
		if (forth == null) {
			if (other.forth != null)
				return false;
		} else if (!forth.equals(other.forth))
			return false;
		if (second == null) {
			if (other.second != null)
				return false;
		} else if (!second.equals(other.second))
			return false;
		if (third == null) {
			if (other.third != null)
				return false;
		} else if (!third.equals(other.third))
			return false;
		return true;
	}
	
	
	
	

}
