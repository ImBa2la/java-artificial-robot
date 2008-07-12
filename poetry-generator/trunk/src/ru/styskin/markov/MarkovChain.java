package ru.styskin.markov;

import java.util.Collection;

public interface MarkovChain<T> {

	public abstract void addWord(Collection<T> word);

	public abstract T getNextLetter(T letter);

	public abstract Collection<T> generateWord();

}