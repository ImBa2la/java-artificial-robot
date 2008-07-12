package ru.styskin.markov;

import java.util.Collection;

public interface MarkovChain<T> {


	T getNextLetter(T letter);

	Collection<T> generateWord();
}