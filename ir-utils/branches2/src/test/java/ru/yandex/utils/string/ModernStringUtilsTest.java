package ru.yandex.utils.string;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.util.StopWatch;

import ru.yandex.utils.string.aho.Builder;
import ru.yandex.utils.string.aho.Vertex;

public class ModernStringUtilsTest {
	
	@Test
	public void fastReplace() throws Exception {
		List<String> dict = new ArrayList<String>(), rep = new ArrayList<String>();
		dict.add("aa");			rep.add("bb");
		dict.add(" quot; ");	rep.add("\"");		
		Vertex<Integer> vertex = Builder.buildIndex(dict);
		assertEquals(" bb sadasd\"sd", ModernStringUtils.fastReplace(" aa sadasd quot; sd", vertex, dict, rep));
	}
	
	@Test
	public void beutify() throws Exception {
		int M = 100000;
		
		StopWatch watch = new StopWatch();
		watch.start("me");
		for(int i=0; i < M; i ++) {
//			assertEquals("Пушкин \"Капитанская дочка\"", ModernStringUtils.beautify("Пушкин &quot;Капитанская дочка&quot; "));
		}
		watch.stop();
		watch.start("utils");
		for(int i=0; i < M; i ++) {
			assertEquals("Пушкин \"Капитанская дочка\"", "Пушкин &quot;Капитанская дочка&quot; ".trim().replaceAll("&quot;", "\"").replaceAll("&amp;", "&").replaceAll("&gt;", ">"));
		}
		watch.stop();
		System.out.println(watch.prettyPrint());
	}

}
