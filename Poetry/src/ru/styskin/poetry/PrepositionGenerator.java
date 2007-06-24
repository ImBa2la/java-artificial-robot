package ru.styskin.poetry;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.styskin.poetry.utils.ComparedPair;
import ru.styskin.poetry.utils.DefaultStringProcessor;
import ru.styskin.poetry.utils.Pair;
import ru.styskin.poetry.utils.SetUtils;

public class PrepositionGenerator {
	
	private DefaultStringProcessor stringProcessor = new DefaultStringProcessor();
	
	private Map<String, Integer> words = new HashMap<String, Integer>();

	public static void main(String[] args) throws Exception {
		PrepositionGenerator generator = new PrepositionGenerator();
		generator.init("../Karaoke/Толстой");
		generator.init("../Karaoke/Достоевский");
		generator.output(1000);
	}

	public void init(String source) throws IOException {
		File dir = new File(source);
		for(File file : dir.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.toLowerCase().endsWith(".txt");
			}
		})) {
			appendFile(file);
		}
	}
	
	private void appendFile(File file) throws IOException {
		BufferedReader in = new BufferedReader(new FileReader(file));
		String s;
		while( (s = in.readLine()) != null) {
			for(String word : stringProcessor.splitString(s)) {
				SetUtils.increment(words, word);				
			}
		}		
		in.close();
	}
	
	private void output(int count) {
		List<ComparedPair<Integer, String>> list = new ArrayList<ComparedPair<Integer,String>>();
		for(Map.Entry<String, Integer> entity : words.entrySet()) {
			list.add(Pair.makePair(- entity.getValue(), entity.getKey()));						
		}
		Collections.sort(list);
		for(int i=0; i < count && i < list.size(); i++) {
			System.out.printf("%s\n", list.get(i).getSecond());
		}
	}
}
