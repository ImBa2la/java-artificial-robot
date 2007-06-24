package ru.styskin.poetry.poem;

import java.util.Collections;
import java.util.List;

import ru.styskin.poetry.dictionary.Dictionary;
import ru.styskin.poetry.dictionary.Dictionary.Direction;

public class ChainGenerator {
	
	private Dictionary dictionary;
	private int size;

	public ChainGenerator(Dictionary dictionary, int size) {
		this.dictionary = dictionary;
		this.size = size;
	}
	
	public Chain generateChain(Chain chain, Direction direction) {
		if(chain == null || chain.size() == 0) {
			List<Chain> start = dictionary.getStartPrases();
			Collections.shuffle(start);
			for(Chain c : start) {
				if(forward(c, direction)) {
					return c;
				}
			}
		} else if(forward(chain, direction)) {
			return chain;
		}
		return null;
	}
	
	public boolean forward(Chain chain, Direction direction) {
		if(chain.vocalLength() > size) {
			return false;
		} else if(chain.vocalLength() == size) {
			if(direction == Direction.FORWARD) {
				return dictionary.getRifms(chain.getWord(direction)) != null && dictionary.getRifms(chain.getWord(direction)).size() > 1;				
			}			
			return true;
		}
		List<Chain> phrases = dictionary.getPhrase(chain.getShift(direction), direction);
		Collections.shuffle(phrases);
		for(Chain c : phrases) {
			chain.add(c.getWord(direction), direction);
			if(forward(chain, direction)) {
				return true;
			}
			chain.remove(direction);
		}
		return false;
	}
}
