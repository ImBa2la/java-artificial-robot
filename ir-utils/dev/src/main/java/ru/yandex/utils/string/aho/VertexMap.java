package ru.yandex.utils.string.aho;

import it.unimi.dsi.fastutil.chars.Char2ObjectArrayMap;
import it.unimi.dsi.fastutil.chars.Char2ObjectMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;

import java.util.Iterator;
import java.util.Map;

import ru.yandex.utils.PairCO;

public class VertexMap<O> extends VertexAbstr<O> {

	private Char2ObjectMap<Vertex<O>> gotoFunction = new Char2ObjectArrayMap<Vertex<O>>();

	public Vertex<O> gotoFunction(char c) {
		return gotoFunction.get(c);
	}

	@Override
	protected void addVertex(Vertex<O> v, char c) {
		gotoFunction.put(c, v);
	}

	public Iterator<PairCO<Vertex<O>>> childrenIterator() {
		return new Iterator<PairCO<Vertex<O>>>() {
			
			private ObjectIterator<Map.Entry<Character, Vertex<O>>> it = gotoFunction.entrySet().iterator(); 

			public boolean hasNext() {
				return it.hasNext();
			}

			public PairCO<Vertex<O>> next() {
				Map.Entry<Character, Vertex<O>> entry = it.next();				
				return new PairCO<Vertex<O>>(entry.getKey(), entry.getValue());
			}

			public void remove() {}
			
		};
	}
	
}
