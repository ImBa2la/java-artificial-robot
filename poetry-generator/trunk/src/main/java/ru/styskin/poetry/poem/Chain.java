package ru.styskin.poetry.poem;

import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import ru.styskin.poetry.dictionary.Dictionary.Direction;
import ru.styskin.poetry.utils.SingletonString;

public class Chain implements Comparable<Chain> {
	
	private LinkedList<SingletonString> chain;
	private int vocalLength;
	
	public Chain() {
		chain = new LinkedList<SingletonString>();		
	}
	
	public Chain(LinkedList<SingletonString> chain) {
		this.chain = chain;
	}
	
	public Chain(SingletonString s) {
		chain = new LinkedList<SingletonString>();
		chain.add(s);
		vocalLength = s.getSize();
	}
	
	public Chain(Chain chain) {
		this.chain = new LinkedList<SingletonString>(chain.chain);
		this.vocalLength = chain.vocalLength;
	}
	
	public Chain(Chain chain, int begin, int end) {
		this.chain = new LinkedList<SingletonString>();
		Iterator<SingletonString> it = chain.getChain().iterator();
		for(int i=0; i < begin; i++) {
			it.next();			
		}
		for(int i = begin; i < end; i++) {
			this.chain.add(it.next());
			vocalLength += chain.get(i).getSize();
		}	
	}
	
	public Chain(List<SingletonString> chain, int begin, int end) {
		this.chain = new LinkedList<SingletonString>();
		for(int i = begin; i < end; i++) {
			this.chain.add(chain.get(i));			
			vocalLength += chain.get(i).getSize();
		}
	}
	
	public Chain getShift(Direction direction, int size) {
		if(this.size() <= size) {
			return this;
		} else if(direction == Direction.FORWARD) {
			return new Chain(this, chain.size() - size, chain.size());			
		} else {
			return new Chain(this, 0, size);
		}
	}
	
	public List<SingletonString> getChain() {
		return chain;
	}	
	
	public SingletonString get(int i) {
		return chain.get(i);		
	}
	
	public void add(SingletonString s, Direction direction) {
		if(direction == Direction.FORWARD) {
			chain.addLast(s);
		} else {
			chain.addFirst(s);
		}
		vocalLength += s.getSize();
	}
	
	public SingletonString remove(Direction direction) {
		SingletonString s = direction == Direction.FORWARD? chain.removeLast() : chain.removeFirst();
		vocalLength -= s.getSize();		
		return s;
	}	
	
	public SingletonString first() {
		return chain.size() > 0? chain.get(0) : null;
	}
	
	public SingletonString last() {
		return chain.size() > 0? chain.get(chain.size() - 1) : null;
	}
	
	public SingletonString getWord(Direction direction) {
		return direction == Direction.FORWARD ? last() : first();
	}
	
	
	public int size() {
		return chain.size();
	}
	
	public int vocalLength() {
		return vocalLength;
	}
	
	public int compareTo(Chain c) {
		Iterator<SingletonString> it1 = chain.iterator();
		Iterator<SingletonString> it2 = c.chain.iterator();
		while(it1.hasNext() && it2.hasNext()) {
			int cmp = it1.next().compareTo(it2.next());
			if(cmp != 0) {
				return cmp;
			}
		}
		return Integer.valueOf(size()).compareTo(c.size());
	}
	
	// Must be the same size
	public static class ForwardComparator implements Comparator<Chain> {
		public int compare(Chain c1, Chain c2) {
			Iterator<SingletonString> it1 = c1.chain.iterator();
			Iterator<SingletonString> it2 = c2.chain.iterator();
			int size = c1.size() == c2.size() ? c1.size() - 1 : Math.min(c1.size(), c2.size());
			for(int i=0; i < size; i++) {
				int cmp = it1.next().compareTo(it2.next());
				if(cmp != 0) {
					return cmp;
				}
			}
			return 0;
		}				
	}
	
	public static class BackwardComparator implements Comparator<Chain> {
		public int compare(Chain c1, Chain c2) {
			ListIterator<SingletonString> it1 = c1.chain.listIterator(c1.chain.size());
			ListIterator<SingletonString> it2 = c2.chain.listIterator(c2.chain.size());
			int size = c1.size() == c2.size() ? c1.size() - 1 : Math.min(c1.size(), c2.size());
			for(int i=0; i < size; i++) {				
				int cmp = it1.previous().compareTo(it2.previous());
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
		for(int i=0; i < c.size() && i < size(); i++) {
			if(chain.get(i) != c.chain.get(i)) {
				return false;
			}
		}
		return c.size() == size();
	}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(Character.toUpperCase(chain.getFirst().getString().charAt(0))).append(chain.getFirst().getString().substring(1));
		Iterator<SingletonString> it = chain.iterator();
		it.next();		
		while(it.hasNext()) {
			sb.append(' ').append(it.next());
		}		
		return sb.toString();
	}
}
