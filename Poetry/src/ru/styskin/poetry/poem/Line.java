package ru.styskin.poetry.poem;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import ru.styskin.poetry.dictionary.Dictionary;
import ru.styskin.poetry.utils.SingletonString;

public class Line {

	//TODO: Get Random Permutation
	
	private List<SingletonString> words = new ArrayList<SingletonString>();
	private Set<SingletonString> wordSet = new TreeSet<SingletonString>(SingletonString.getComparator());
	
	private Dictionary dictionary;
	private List<SingletonString> dictionaryWords;
	
	
	private Line() {
		
	}
	
	public static Line getLine(Dictionary dictionary, int size) {
		Line line = new Line();
		line.dictionary = dictionary;
		line.dictionaryWords = new ArrayList<SingletonString>(dictionary.getFrequency().keySet());

		while(true) {
			SingletonString first = line.dictionaryWords.get((int)(Math.random()*line.dictionaryWords.size()));
			line.words.add(first);
			if(line.forward(first, size - first.getSize())) {
				break;
			}
			line.words.remove(line.words.size()-1);
		}
		return line;
	}
	
	private boolean forward(SingletonString prev, int size) {
		if(size == 0) {
			List<SingletonString> list = dictionary.getRifms(prev);   
			return list != null && list.size() > 1;
		} else if(size < 0) {
			return false;						
		} else {
			Map<Double, SingletonString> nextWords = new TreeMap<Double, SingletonString>();
			if(dictionary.getPhrase(prev) == null) {
				return false;				
			}
			for(SingletonString s : dictionary.getPhrase(prev)) {
				nextWords.put(Math.random(), s);				
			}
			for(Double key : nextWords.keySet()) {
				SingletonString next = nextWords.get(key);
				words.add(next);
				if(forward(next, size - next.getSize())) {
					return true;
				}
				words.remove(words.size()-1);
			}			
		}
		return false;		
	}
	
	// TODO: join with forward
	private boolean backward(SingletonString prev, int size) {
		if(size == 0) {
			return true;
		} else if(size < 0) {
			return false;						
		} else {
			Map<Double, SingletonString> nextWords = new TreeMap<Double, SingletonString>();
			for(SingletonString s : dictionary.getPhraseRev(prev)) {
				nextWords.put(Math.random(), s);				
			}
			for(Double key : nextWords.keySet()) {
				SingletonString next = nextWords.get(key);
				words.add(next);
				if(forward(next, size - next.getSize())) {
					return true;
				}
				words.remove(words.size()-1);
			}			
		}
		return false;		
	}	
	
	
	public static Line getLine(Dictionary dictionary, int size, SingletonString rifm) {
		Line line = new Line();
		line.dictionary = dictionary;
		line.dictionaryWords = new ArrayList<SingletonString>(dictionary.getFrequency().keySet());

		Map<Double, SingletonString> nextWords = new TreeMap<Double, SingletonString>();
		for(SingletonString s : dictionary.getRifms(rifm)) {
			if(s != rifm) {
				nextWords.put(Math.random(), s);
			}
		}
		for(Double key : nextWords.keySet()) {
			SingletonString next = nextWords.get(key);			
			line.words.add(next);
			if(line.backward(next, size - next.getSize())) {
				List<SingletonString> copy = line.words;
				line.words = new ArrayList<SingletonString>();
				for(int i=copy.size()-1; i >=0; i--) {
					line.words.add(copy.get(i));
				}
				return line;
			}
			line.words.remove(line.words.size()-1);			
		}
		return null;
	}

	public List<SingletonString> getWords() {
		return words;
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(Character.toUpperCase(words.get(0).getString().charAt(0))).append(words.get(0).getString().substring(1));
		for(int i=1; i < words.size(); i++) {
			sb.append(' ').append(words.get(i));
		}		
		return sb.toString();
	}
}
