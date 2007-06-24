package ru.styskin.poetry;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
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
	
	public static void main(String[] args) throws Exception {
		(new Generator()).run();
	}
	
	public void run() throws Exception {
		init("../Karaoke/Џушкин");
/*		Dictionary d1 = dictionary;
		init("../Karaoke/Алиса");
		Dictionary d2 = dictionary;
		Set<SingletonString> set = new HashSet<SingletonString>(d1.getFrequencyStemmed().keySet());
		set.retainAll(d2.getFrequencyStemmed().keySet());*/
		ChainGenerator generator = new ChainGenerator(dictionary, 10);
		

		for(int i=0; i < 2; i++) {
			Chain chain = generator.generateChain(new Chain(), Direction.FORWARD);
			System.out.println(chain);
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
			System.out.println(r);
		}
		
				
		
/*		int SIZE = 10;
		for(int i=0; i < 4; i++) {
			Line line = Line.getLine(dictionary, SIZE);
			System.out.write(line.toString().getBytes());
			System.out.println("");
			System.out.write(Line.getLine(dictionary, SIZE, line.getWords().get(line.getWords().size()-1)).toString().getBytes());
			System.out.println("");
		}
		*/

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
