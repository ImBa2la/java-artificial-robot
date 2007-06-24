package ru.styskin.poetry.dictionary;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import ru.styskin.poetry.dictionary.Dictionary.Direction;
import ru.styskin.poetry.poem.Chain;
import ru.styskin.poetry.utils.SingletonString;

public class ChainContainer {
	
	private Map<Direction, Map<Integer, Map<Chain, Set<Chain>>>> phrases = new IdentityHashMap<Direction, Map<Integer,Map<Chain,Set<Chain>>>>();
	
	private static final Map<Direction, Comparator<Chain>> COMPARATORS = new IdentityHashMap<Direction, Comparator<Chain>>();
	
	static {
		COMPARATORS.put(Direction.FORWARD, new Chain.ForwardComparator());
		COMPARATORS.put(Direction.BACKWARD, new Chain.BackwardComparator());
	}
	
	private Set<Chain> chains = new TreeSet<Chain>();
	
	public ChainContainer() {
		for(Direction d : Direction.values()) {
			phrases.put(d, new HashMap<Integer, Map<Chain,Set<Chain>>>());
		}
	}
	
	public void addChain(Chain chain) {
		if((new HashSet<SingletonString>(chain.getChain())).size() < chain.size()) {
			return;
		}
		chains.add(chain);
		for(Direction d : Direction.values()) {
			Map<Chain, Set<Chain>> base = phrases.get(d).get(chain.size());
			if(base == null) {
				base = new TreeMap<Chain, Set<Chain>>(COMPARATORS.get(d));
				phrases.get(d).put(chain.size(), base);
			}
			Set<Chain> chains = base.get(chain);
			if(chains == null) {
				chains = new TreeSet<Chain>();
				base.put(chain, chains);
			}
			chains.add(chain);
		}
	}
	
	public List<Chain> getChains(Direction d, Chain original) {
		List<Chain> variants = new ArrayList<Chain>(2);
		for(Integer size :  phrases.get(d).keySet()) {
			Set<Chain> chains = phrases.get(d).get(size).get(original.getShift(d, size - 1));
			if(chains != null) {
				variants.addAll(chains);
			}
		}
		return variants;
	}
	
	public List<Chain> getStartChains() {
		return new ArrayList<Chain>(chains);
	}

}
