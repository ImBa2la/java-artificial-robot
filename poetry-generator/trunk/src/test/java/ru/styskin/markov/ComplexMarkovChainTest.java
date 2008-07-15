package ru.styskin.markov;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.log4j.Logger;

import ru.styskin.dict.Dict;
import ru.yandex.utils.IteratorUtils;
import ru.yandex.utils.Transform;
import ru.yandex.utils.spring.SpringContextTestCase;

public class ComplexMarkovChainTest extends SpringContextTestCase {
	
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

	private final Transform<Collection<Character>, String> C2S = new Transform<Collection<Character>, String>() {
		public String transform(Collection<Character> val) {
			StringBuilder sb = new StringBuilder(val.size());
			for(Character c : val) {
				sb.append(c);
			}
			return sb.toString();
		}		
	};
	
	public void testMarkov() throws Exception {
		ComplexMarkovChain<Character> markov = new ComplexMarkovChain<Character>(' ');
		markov.init(IteratorUtils.mapI(dictionary.getWords(), S2C));
		for(int i=0; i < 100; i ++) {		
			logger.info(C2S.transform(markov.generateWord())); 
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
