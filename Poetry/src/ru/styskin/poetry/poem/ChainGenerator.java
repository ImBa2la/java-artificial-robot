package ru.styskin.poetry.poem;

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
		while(true) {
			if(chain.size() >= size) {
				break;
			}
			
			
		}
		return chain;
	}
	
}
