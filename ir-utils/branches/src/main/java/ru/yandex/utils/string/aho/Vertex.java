package ru.yandex.utils.string.aho;


import java.util.Iterator;
import java.util.List;

import ru.yandex.utils.PairCO;

public interface Vertex<O> {
	
	List<O> out();
	Vertex<O> fail();
	Vertex<O> gotoFunction(char c);

	Vertex<O> addString(final String s, final int position, final O o);
	Iterator<PairCO<Vertex<O>>> childrenIterator();
	
	void setFail(Vertex<O> v);
	void addOut(List<O> list);

}
