package org.styskin.util;

public class PairDI implements Comparable<PairDI> {
	
	public double first;
	public int second;
	
	public PairDI(double first, int second) {
		this.first = first;
		this.second = second;
	}

	public int compareTo(PairDI o) {
		return Double.compare(first, o.first) == 0 ? (second < o.second? -1 : second > o.second ? 1 : 0) : Double.compare(first, o.first);
	}
}
