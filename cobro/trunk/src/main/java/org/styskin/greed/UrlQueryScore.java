package org.styskin.greed;

import java.util.Comparator;

public class UrlQueryScore {
	int q;
	double score;
	double real;
	
	public UrlQueryScore(int q, double score, double real) {
		this.q = q;
		this.score = score;
		this.real = real;
	}
}

class UrlQueryScoreComparator implements Comparator<UrlQueryScore> {
	public int compare(UrlQueryScore o1, UrlQueryScore o2) {
		if(o1.q  == o2.q) {
			return -Double.compare(o1.score, o2.score);
			// Maximum dcg
//			return -Double.compare(o1.real, o2.real);										
		} else {				
			return o1.q < o2.q ? -1 : 1;
		}
	}

}