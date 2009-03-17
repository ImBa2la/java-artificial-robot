package org.styskin.util;

public class PairID implements Comparable<PairID> {
	
	public int first;
	public double second;
	
	public PairID(int first, double second) {
		this.first = first;
		this.second = second;
	}	
	
	public int compareTo(PairID o) {
		return (second < o.second? -1 : second > o.second ? 1 : Double.compare(first, o.first));
	}

}
