package ru.styskin.poetry.poem;

import java.util.Comparator;
import java.util.List;

import ru.styskin.poetry.utils.SingletonString;

public class Chain {
	
	private List<SingletonString> chain;
	
	private Chain() {}
	
	public Chain(List<SingletonString> chain) {
		this.chain = chain;
	}
	
	public int size() {
		return chain.size();
	}
	
	public int compareTo(Chain c) {
		for(int i=0; i < size() && i < c.size(); i++) {
			int cmp = chain.get(i).compareTo(c.chain.get(i));
			if(cmp != 0) {
				return cmp;
			}
		}
		return Integer.valueOf(size()).compareTo(c.size());
	}
	
	public static class ForwardComparator implements Comparator<Chain> {
		public int compare(Chain c1, Chain c2) {
			int i=c1.size()-2, j = c2.size()-2;
			for(; i >=0 && j >=0; i--, j--) {
				int cmp = c1.chain.get(i).compareTo(c2.chain.get(j));
				if(cmp != 0) {
					return cmp;
				}
			}
			return i != j? Integer.valueOf(i).compareTo(j) : 0;
		}				
	}
	
	public static class BackwardComparator implements Comparator<Chain> {
		public int compare(Chain c1, Chain c2) {
			int i=1, j = 1;
			for(; i < c1.size()-1 && j < c2.size() - 1; i++, j ++) {
				int cmp = c1.chain.get(i).compareTo(c2.chain.get(j));
				if(cmp != 0) {
					return cmp;
				}
			}
			return c1.size() == c2.size()? 0 : Integer.valueOf(c1.size()).compareTo(c2.size());
		}			
	}

	@Override
	public int hashCode() {
		return chain.hashCode();
	}

	public boolean equals(Chain c) {
		for(int i=0; i < c.size() && i < size(); i++) {
			if(chain.get(i) != c.chain.get(i)) {
				return false;
			}
		}
		return c.size() == size();
	}
}
