package ru.yandex.utils.string;

import org.junit.Test;

public class IndexedStringFactoryTest {
	
	
	@Test
	public void createIndexedString() throws Exception {
		String extra = "*+-?!:";
		IndexedStringFactory factory = new IndexedStringFactory();
		factory.setExtraSymbols(extra);
		factory.setStringProcessor(new DefaultStringProcessor());
		
		
		IndexedString a = factory.createIndexedString("tfdgfdg Eten+ (something)");
		IndexedString b = factory.createIndexedString("eten\\+", true);
		System.out.println(a.search(b));
		System.out.println(a.show(a.search(b).get(0)));
	}

}
