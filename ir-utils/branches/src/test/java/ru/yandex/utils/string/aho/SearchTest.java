package ru.yandex.utils.string.aho;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

import org.junit.Test;

public class SearchTest {
	
	@Test
	public void search() throws Exception {
		List<String> dict = new ArrayList<String>();
		dict.add("a");
		dict.add("aa");
		dict.add("b c");
		dict.add("b");
		Vertex<String> vertex = Builder.build(dict);
		assertEquals("[[0; a], [1; aa], [1; a], [3; b], [5; b c]]", Search.search("aa b c", vertex).toString());
	}
}
