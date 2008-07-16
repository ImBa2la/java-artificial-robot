package ru.styskin.poetry;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ru.styskin.poetry.dictionary.Dictionary;
import ru.styskin.poetry.dictionary.Dictionary.Direction;
import ru.styskin.poetry.poem.Chain;
import ru.styskin.poetry.poem.ChainGenerator;
import ru.styskin.poetry.utils.SingletonString;

public class Generator {
	
	private Dictionary dictionary;
	private String title;
	private int size;
	
	public Generator(String title, int size) {
		this.title = title;
		this.size = size;
	}
	
	public static void main(String[] args) throws Exception {
		(new Generator("Пушкин", 8)).run();
	}
	
	public void run() throws Exception {
		PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(title + ".txt"), "UTF-8")));
		init("../Karaoke/" + title);
		System.err.println("Initialization successful");
		ChainGenerator generator = new ChainGenerator(dictionary, size);		

		for(int i=0; i < 10; i++) {
			Chain chain = generator.generateChain(new Chain(), Direction.FORWARD);
			List<SingletonString> list = new ArrayList<SingletonString>(dictionary.getRifms(chain.last()));
			list.remove(chain.last());
			Collections.shuffle(list);
			Chain r = null;
			for(SingletonString s : list) {
				r = new Chain(s);
				if(( r = generator.generateChain(r, Direction.BACKWARD)) != null) {
					break;
				}
			}
			if(r != null) {
				out.println(chain);
				out.println(r);
			}
		}
		out.close();				
	}
	
	private void init(String source) throws IOException {
		dictionary = new Dictionary();
		File dir = new File(source);
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
