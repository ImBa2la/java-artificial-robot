package ru.styskin.poetry.dictionary;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import ru.styskin.poetry.utils.DefaultStringProcessor;
import ru.styskin.poetry.utils.SetUtils;
import ru.styskin.poetry.utils.SingletonString;
import ru.styskin.poetry.utils.StringUtils;

public class Dictionary {
	
	public enum Direction {
		FORWARD, BACKWARD
	}
	
	private Map<Direction, Map<SingletonString, Set<SingletonString>>> phrase = new TreeMap<Direction, Map<SingletonString,Set<SingletonString>>>();
	
	{
		for(Direction direction : Direction.values()) {
			phrase.put(direction, new TreeMap<SingletonString, Set<SingletonString>>(SingletonString.getComparator()));
		}
	}
	
	private Map<SingletonString, SingletonString> rifms = new TreeMap<SingletonString, SingletonString>(SingletonString.getComparator());
	
	private Map<SingletonString, Integer> frequency = new TreeMap<SingletonString, Integer>(SingletonString.getComparator());
	
	private Map<SingletonString, List<SingletonString>> rifmsMap;
	
	public void appendDictionary(File file) throws IOException {
		BufferedReader in = new BufferedReader(new FileReader(file));
		DefaultStringProcessor processor = DefaultStringProcessor.getProcessor();
		{
			List<List<SingletonString>> abstr = new ArrayList<List<SingletonString>>();
			String s;
			while((s = in.readLine()) != null) {
				List<SingletonString> line = processor.split(s);
				if(abstr.size() > 0 && line.size() == 0) {
					processAbstract(abstr);
					abstr.clear();
				} else if(line.size() > 0){
					abstr.add(line);
				}
			}
			if(abstr.size() > 0) {
				processAbstract(abstr);
			}
		}		
		in.close();		
	}
	
	public void rebuildDictionaries() {
		rifmsMap = new TreeMap<SingletonString, List<SingletonString>>(SingletonString.getComparator());
		for(SingletonString word : rifms.keySet()) {
			SingletonString key = getRifm(word);
			List<SingletonString> list = rifmsMap.get(key);
			if(list == null) {
				list = new ArrayList<SingletonString>();
				rifmsMap.put(key, list);
			}
			list.add(word);
		}
		List<SingletonString> keys = new LinkedList<SingletonString>(rifmsMap.keySet());
		for(SingletonString key : keys) {
			List<SingletonString> list = rifmsMap.get(key);
			for(SingletonString word : list) {
				rifmsMap.put(word, list);
			}
		}
	}
	
	
	private void processAbstract(List<List<SingletonString>> abstr) {
		if(abstr.size() < 4) {
			return;
		}
		for(int i=0; i < abstr.size(); i++) {
			List<SingletonString> list = abstr.get(i);
			for(int j=0; j < list.size(); j++) {
				SetUtils.increment(frequency, list.get(j));				
			}
			for(int j=0; j < list.size() -1; j++) {
				putWord(list.get(j), list.get(j+1));				
			}
		}
		int fs = StringUtils.matchFromEnd(abstr.get(0).get(abstr.get(0).size()-1), abstr.get(1).get(abstr.get(1).size()-1));
		int ft = StringUtils.matchFromEnd(abstr.get(0).get(abstr.get(0).size()-1), abstr.get(2).get(abstr.get(2).size()-1));
		if(fs >= ft && fs >= 2) {
			for(int i=0; i<= abstr.size()-2; i += 2) {
				SingletonString a = abstr.get(i).get(abstr.get(i).size()-1);				
				SingletonString b = abstr.get(i+1).get(abstr.get(i+1).size()-1);
				if(StringUtils.matchFromEnd(a, b) >=2) {
					putRifm(a, b);
				}
			}			
		} else if(ft > fs && ft >= 2) {
			for(int i=0; i<= abstr.size()-4; i += 4) {
				for(int j=0; j< 2; j ++) {
					SingletonString a = abstr.get(i + j).get(abstr.get(i+j).size()-1);				
					SingletonString b = abstr.get(i + j + 2).get(abstr.get(i+j+2).size()-1);
					if(StringUtils.matchFromEnd(a, b) >=2) {
						putRifm(a, b);
					}
				}
			}
		}
	}
	
	private void putWord(SingletonString key, SingletonString value) {
		putWord(key, value, Direction.FORWARD);
		putWord(value, key, Direction.BACKWARD);
	}
	
	private void putWord(SingletonString key, SingletonString value, Direction direction) {
		Set<SingletonString> set = phrase.get(direction).get(key);		
		if(set == null) {
			set = new TreeSet<SingletonString>(SingletonString.getComparator());
			phrase.get(direction).put(key, set);
		}
		set.add(value);
	}
		
	private void putRifm(SingletonString a, SingletonString b) {
		a = getRifm(a);
		b = getRifm(b);
		rifms.put(a, b);
	}
	
	private SingletonString getRifm(SingletonString ss) {
		if(rifms.containsKey(ss)) {
			while(rifms.get(ss) != ss) {
				ss = rifms.get(ss); 				
			}
		} else {
			rifms.put(ss, ss);
		}
		return ss;
	}

	public Map<SingletonString, Integer> getFrequency() {
		return frequency;
	}

	public Set<SingletonString> getPhrase(SingletonString s, Direction direction) {
		return phrase.get(direction).get(s);
	}

	public Map<SingletonString, List<SingletonString>> getRifmsMap() {
		return rifmsMap;
	}
	
	public List<SingletonString> getRifms(SingletonString s) {
		return rifmsMap.get(s);
	}
}
