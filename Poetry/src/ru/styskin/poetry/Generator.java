package ru.styskin.poetry;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

import ru.styskin.poetry.dictionary.Dictionary;
import ru.styskin.poetry.poem.Line;

public class Generator {
	
	private Dictionary dictionary = new Dictionary();
	
	public static void main(String[] args) throws Exception {
		(new Generator()).run();		
	}
	
	public void run() throws Exception {
		init();
		int SIZE = 8;
		for(int i=0; i < 4; i++) {
			Line line = Line.getLine(dictionary, SIZE);
			System.out.println(line);
			System.out.println(Line.getLine(dictionary, SIZE, line.getWords().get(line.getWords().size()-1)));
		}

	}
	
	private void init() throws IOException {
		File dir = new File("text");
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
