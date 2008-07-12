package ru.yandex.utils;

import static ru.yandex.utils.IteratorUtils.filter;
import static ru.yandex.utils.IteratorUtils.olist;
import static ru.yandex.utils.IteratorUtils.toList;

import java.util.List;

import junit.framework.TestCase;

public class IteratorUtilsTest extends TestCase {

	
	public void testFilter() throws Exception {
		List<Integer> list = toList(filter(olist(1,2,3), new Accept<Integer>() {
			public boolean accept(Integer o) {
				return true;
			}
		}));
		System.out.println(list);
	}
}
