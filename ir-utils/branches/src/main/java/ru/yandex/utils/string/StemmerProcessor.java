package ru.yandex.utils.string;

import org.tartarus.snowball.ext.russianStemmer;

public class StemmerProcessor implements StringProcessor {
	
	@SuppressWarnings("unused")
	private final ThreadLocal<russianStemmer> stemmer = new ThreadLocal<russianStemmer>() {
		public russianStemmer initialValue() {
			return new russianStemmer();
		}
	};

	public String process(String s) {
		russianStemmer sn = stemmer.get();
		sn.setCurrent(s);
		sn.stem();
		return sn.getCurrent();
	}

}
