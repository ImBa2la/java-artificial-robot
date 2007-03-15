package ru.styskin.poetry.poem;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import ru.styskin.poetry.dictionary.Dictionary;
import ru.styskin.poetry.dictionary.Dictionary.Direction;
import ru.styskin.poetry.utils.RandomIterator;
import ru.styskin.poetry.utils.SingletonString;

public class Line {

	private List<SingletonString> words = new ArrayList<SingletonString>();

	// TODO: Exclude duplicated words
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
		return forward(prev, size, Direction.FORWARD);
	}
	
	private boolean forward(SingletonString prev, int size, Direction direction) {
		if(size == 0) {
			List<SingletonString> list = dictionary.getRifms(prev);   
			return list != null && list.size() > 1;
		} else if(size < 0) {
			return false;						
		} else {
			if(dictionary.getPhrase(prev, direction) == null) {
				return false;				
			}
			Iterator<SingletonString> it = new RandomIterator<SingletonString>(dictionary.getPhrase(prev, direction), null);
			while(it.hasNext()) {
				SingletonString next = it.next();
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


		Iterator<SingletonString> it = new RandomIterator<SingletonString>(dictionary.getRifms(rifm), rifm);
		while(it.hasNext()) {
			SingletonString next = it.next();			
			line.words.add(next);
			if(line.forward(next, size - next.getSize(), Direction.BACKWARD)) {
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
