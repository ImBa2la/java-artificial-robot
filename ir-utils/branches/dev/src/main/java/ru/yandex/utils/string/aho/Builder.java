package ru.yandex.utils.string.aho;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import ru.yandex.utils.PairCO;

public class Builder {
	
	
	public static Vertex<String> build(final List<String> dict) {
		VertexAbstr<String> root = new VertexMap<String>();
		for(String s : dict) {
			Vertex<String> c = root;
			for(int i=0; i <= s.length(); i++) {
				c = c.addString(s, i, s);
			}
		}
		// Fail function
		constructFail(root);		
		return root;
	}
	
	public static Vertex<Integer> buildIndex(final List<String> dict) {
		VertexAbstr<Integer> root = new VertexMap<Integer>();
		for(int j = 0; j < dict.size(); j ++) {
			String s = dict.get(j);
			Vertex<Integer> c = root;
			for(int i=0; i <= s.length(); i++) {
				c = c.addString(s, i, j);
			}
		}
		// Fail function
		constructFail(root);		
		return root;
	}
	
	public static Vertex<Integer> buildIndex(final List<String> dict, final List<Integer> index) {
		VertexAbstr<Integer> root = new VertexMap<Integer>();
		for(int j = 0; j < dict.size(); j ++) {
			String s = dict.get(j);
			Vertex<Integer> c = root;
			for(int i=0; i <= s.length(); i++) {
				c = c.addString(s, i, index.get(j));
			}
		}
		// Fail function
		constructFail(root);		
		return root;
	}
	

	private static <S> void constructFail(VertexAbstr<S> root) {
		root.setFail(root);
		Queue<Vertex<S>> queue = new LinkedList<Vertex<S>>();
		for(Iterator<PairCO<Vertex<S>>> it = root.childrenIterator(); it.hasNext(); ) {
			Vertex<S> v = it.next().getSecond();
			v.setFail(root);
			queue.offer(v);
		}
		while(!queue.isEmpty()) {
			Vertex<S> c = queue.poll();
			outer: for(Iterator<PairCO<Vertex<S>>> it = c.childrenIterator(); it.hasNext(); ) {
				PairCO<Vertex<S>> pair = it.next();
				Vertex<S> v = pair.getSecond();
				char a = pair.getFirst();
				queue.offer(v);
				Vertex<S> h = c.fail();
				while(h.gotoFunction(a) == null) {
					if(h.fail() == h) {
						v.setFail(h);
						continue outer;
					}
					h = h.fail();
				}
				v.setFail(h.gotoFunction(a));
				v.addOut(h.gotoFunction(a).out());
			}			
		}
	}
	
	
}
