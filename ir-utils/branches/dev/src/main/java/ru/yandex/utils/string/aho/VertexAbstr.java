package ru.yandex.utils.string.aho;


import java.util.LinkedList;
import java.util.List;

public abstract class VertexAbstr<O> implements Vertex<O> {
	
	private Vertex<O> fail;
	private List<O> out;

	public Vertex<O> fail() {
		return fail;
	}

	public List<O> out() {
		return out;
	}
	
	public void addOut(List<O> out) {
		if(out == null)
			return;
		if(this.out == null)
			this.out = new LinkedList<O>();
		this.out.addAll(out);		
	}
	
	public void setFail(Vertex<O> v) {
		fail = v;		
	}

	public Vertex<O> addString(final String s, final int position, final O o) {
		if(position >= s.length()) {
			if(out == null)
				out = new LinkedList<O>();
			out.add(o);
			return null;
		} else {
			char c = s.charAt(position);		
			Vertex<O> v = gotoFunction(c);
			if(v == null) {
				v = new VertexMap<O>();
				addVertex(v, c);
			}
			return v;
		}
	}
	
	protected abstract void addVertex(Vertex<O> v, char c);
}
