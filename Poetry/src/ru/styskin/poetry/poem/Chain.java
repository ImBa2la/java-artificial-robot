package ru.styskin.poetry.poem;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import ru.styskin.poetry.utils.SingletonString;

public class Chain {
	
	public static final int SIZE = 3;
	
	private List<SingletonString> chain;

	public int compareTo(Chain c) {
		for(int i=0; i < SIZE; i++) {
			int cmp = chain.get(i).compareTo(c.chain.get(i));
			if(cmp != 0) {
				return cmp;
			}
		}
		return 0;
	}
	

	
	
	public static class ForwardComparator implements Comparator<Chain> {
		public int compare(Chain c1, Chain c2) {
			for(int i=0; i < SIZE-1; i++) {
				int cmp = c1.chain.get(i).compareTo(c2.chain.get(i));
				if(cmp != 0) {
					return cmp;
				}
			}
			return 0;
		}				
	}
	
	public static class BackwardComparator implements Comparator<Chain> {
		public int compare(Chain c1, Chain c2) {
			for(int i=SIZE-1; i > 0; i--) {
				int cmp = c1.chain.get(i).compareTo(c2.chain.get(i));
				if(cmp != 0) {
					return cmp;
				}
			}
			return 0;
		}
				
	}

	@Override
	public int hashCode() {
		return chain.hashCode();
	}

	public boolean equals(Chain c) {
		for(int i=0; i < SIZE; i++) {
			if(chain.get(i) != c.chain.get(i)) {
				return false;
			}
		}
		return true;
	}	
}
