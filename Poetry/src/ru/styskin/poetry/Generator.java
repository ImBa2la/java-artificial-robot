package ru.styskin.poetry;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import ru.styskin.poetry.dictionary.Dictionary;
import ru.styskin.poetry.poem.Line;
import ru.styskin.poetry.utils.SingletonString;

public class Generator {
	
	private Dictionary dictionary;
	
	public static void main(String[] args) throws Exception {
		(new Generator()).run();
	}
	
	public void run() throws Exception {
		init("../Karaoke/Я");
		Dictionary d1 = dictionary;
		init("../Karaoke/Алиса");
		Dictionary d2 = dictionary;
		Set<SingletonString> set = new HashSet<SingletonString>(d1.getFrequencyStemmed().keySet());
		set.retainAll(d2.getFrequencyStemmed().keySet());
				
		
		int SIZE = 10;
		for(int i=0; i < 4; i++) {
			Line line = Line.getLine(dictionary, SIZE);
			System.out.write(line.toString().getBytes());
			System.out.println("");
			System.out.write(Line.getLine(dictionary, SIZE, line.getWords().get(line.getWords().size()-1)).toString().getBytes());
			System.out.println("");
		}

	}
	
	private void init(String source) throws IOException {
		dictionary = new Dictionary();
		File dir = new File(source);
//		File dir = new File("text");
		for(File file : dir.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.toLowerCase().endsWith(".txt");
			}
		})) {
			dictionary.appendDictionary(file);						
		}
		dictionary.rebuildDictionaries();
	}

}
