package ru.styskin.markov;

import java.util.Collection;

public interface MarkovChain<T> {
	
	void init(Iterable<Collection<T>> words);

	Collection<T> generateWord();
}