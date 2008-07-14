package ru.styskin.markov;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.log4j.Logger;

import ru.styskin.dict.Dict;
import ru.yandex.utils.IteratorUtils;
import ru.yandex.utils.Pair;
import ru.yandex.utils.Transform;
import ru.yandex.utils.spring.SpringContextTestCase;

public class SimpleMarkovChainTest extends SpringContextTestCase {
	
	private static final Logger logger = Logger.getLogger(SimpleMarkovChainTest.class);
	
	private Dict dictionary;
	
	private final Transform<String, Collection<Character>> S2C = new Transform<String, Collection<Character>> () {
		public Collection<Character> transform(String val) {
			val = val.toLowerCase();
			if(val.indexOf('/') >= 0) {
				val = val.substring(0, val.indexOf('/'));
			}
			Collection<Character> c = new ArrayList<Character>(2 + val.length());
			c.add(' ');
			for(char cc : val.toCharArray()) {
				c.add(cc);
			}
			c.add(' ');
			return c;
		}
	};
	
	private final Transform<String, Collection<Integer>> S2C2 = new Transform<String, Collection<Integer>> () {
		public Collection<Integer> transform(String val) {
			val = val.toLowerCase();
			if(val.indexOf('/') >= 0) {
				val = val.substring(0, val.indexOf('/'));
			}
			if(val.length() % 2 == 1)
				val += " ";
			Collection<Integer> c = new ArrayList<Integer>(2 + val.length());
			c.add(code(' ', ' '));
			for(int i=0; i < val.length(); i+=2) {
				c.add(code(val.charAt(i), val.charAt(i+1)));
			}
			c.add(code(' ', ' '));
			return c;
		}
	};	

	
	private final Transform<Collection<Character>, String> C2S = new Transform<Collection<Character>, String>() {
		public String transform(Collection<Character> val) {
			StringBuilder sb = new StringBuilder(val.size());
			for(Character c : val) {
				sb.append(c);
			}
			return sb.toString();
		}		
	};
	
	private final Transform<Collection<Integer>, String> C2S2 = new Transform<Collection<Integer>, String>() {
		public String transform(Collection<Integer> val) {
			StringBuilder sb = new StringBuilder(val.size());
			for(Integer c : val) {
				Pair<Character, Character> p = decode(c);
				sb.append(p.getFirst()).append(p.getSecond());
			}
			return sb.toString();
		}		
	};

	
	public void testMarkov() throws Exception {
		SimpleMarkovChain<Character> markov = new SimpleMarkovChain<Character>(' ');
		markov.init(IteratorUtils.mapI(dictionary.getWords(), S2C));
		for(int i=0; i < 100; i ++) {		
//			logger.info(C2S.transform(markov.generateWord())); 
		}
	}
	
	public static Integer code(char a, char b) {
		int i = a;
		i <<= 16;
		i |= b;
		return i;
	}
	
	public Pair<Character, Character> decode(int i) {
		return new Pair<Character, Character>((char)(i>>>16),(char)i);
	}
	
	public void testMarkov2() throws Exception {
		SimpleMarkovChain<Integer> markov = new SimpleMarkovChain<Integer>(code(' ', ' '));
		markov.init(IteratorUtils.mapI(dictionary.getWords(), S2C2));
		for(int i=0; i < 100; i ++) {		
			logger.info(C2S2.transform(markov.generateWord()).trim()); 
		}
	}

	@Override
	protected String[] getConfigLocations() {
		return new String[] {"markov.xml"};
	}


	public void setDictionary(Dict dictionary) {
		this.dictionary = dictionary;
	}

}
