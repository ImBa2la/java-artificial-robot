package ru.styskin.poetry;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintStream;
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
	
	public Generator(String title) {
		this.title = title;
	}
	
	public static void main(String[] args) throws Exception {
		(new Generator("Пушкин")).run();
	}
	
	public void run() throws Exception {
		System.setOut(new PrintStream(new BufferedOutputStream(new FileOutputStream(title + ".txt"))));
		
		
		init("../Karaoke/" + title);
		System.err.println("Initialization successful");
/*		Dictionary d1 = dictionary;
		init("../Karaoke/Алиса");
		Dictionary d2 = dictionary;
		Set<SingletonString> set = new HashSet<SingletonString>(d1.getFrequencyStemmed().keySet());
		set.retainAll(d2.getFrequencyStemmed().keySet());*/
		ChainGenerator generator = new ChainGenerator(dictionary, 10);		

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
				System.out.println(chain);
				System.out.println(r);
			}
		}
		System.out.close();				
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
