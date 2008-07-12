package ru.yandex.utils.string;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.log4j.Logger;

import ru.yandex.utils.string.IndexedString.IndexedToken;

public class IndexedStringFactory {
	
	private static final Logger logger = Logger.getLogger(IndexedStringFactory.class); 
	
	private AcceptC[] acceptors2;
	
	private StringProcessor stringProcessor;
	private boolean steam = true;
	
	public IndexedString createIndexedString(String s, boolean regex) {
		if(regex) {
			return new IndexedString(s, regex);
		} else {
			return createIndexedString(s);
		}
	}
	
	public IndexedString createIndexedString(String s) {
		List<IndexedToken> list = new ArrayList<IndexedToken>();		
		StringSplitter splitter = new StringSplitter(stringProcessor.process(s), acceptors2);
		while(splitter.next()) {
			list.add(new IndexedToken(splitter.begin(), splitter.end(), splitter.tokenClass()));
		}		
		SortedMap<Position, Number> numbers = extractNumbers(s, list);
		IndexedString indexedString = new IndexedString(s, list, numbers, steam);
		return indexedString;
	}

	private SortedMap<Position, Number> extractNumbers(String s, List<IndexedToken> list) {
		SortedMap<Position, Number> numbers = new TreeMap<Position, Number>();
		int start = -1, pc = 0, cc = 0;
		StringBuilder sb = new StringBuilder();
		for(int i=0; i < list.size(); i++) {
			String c = s.substring(list.get(i).getStart(), list.get(i).getEnd());			
			if(".".equals(c) || ",".equals(c) || list.get(i).getType() == 1) {
				if(start < 0) {
					start = i;
				}
				sb.append(c);
				if(".".equals(c)) pc ++;
				if(",".equals(c)) cc ++;
			} else if(start >= 0) {
				checkNumbers(s, list, numbers, start, pc, cc, sb, i);
				start = -1; pc = 0; cc = 0;
				sb = new StringBuilder();
			}
		}
		if(start >= 0) {
			checkNumbers(s, list, numbers, start, pc, cc, sb, list.size());
		}
		return numbers;
	}

	private void checkNumbers(String s, List<IndexedToken> list,
			SortedMap<Position, Number> numbers, int start, int pc, int cc,
			StringBuilder sb, int i) {
		String c;
		if(pc + cc <= 1) {
			try {
				Number n = Double.valueOf(sb.toString().replace(',', '.'));
				numbers.put(new Position(start, i-1), n);
			} catch (Exception e) {}
		} else if(pc <= 1) {
			int sj = start, j = start;
			sb = new StringBuilder();
			while(j < i) {
				c = s.substring(list.get(j).getStart(), list.get(j).getEnd());
				while(!",".equals(c)) {
					sb.append(c);
					if(j < i-1)  {
						j ++;
						c = s.substring(list.get(j).getStart(), list.get(j).getEnd());
					} else {
						break;
					}
				}
				String sn = sb.toString();
				if(sn.length() > 0) {
					try {
						Number n = Double.valueOf(sb.toString().replace(',', '.'));
						numbers.put(new Position(sj, j-1), n);
					} catch (Exception e) {}
				}
				sb = new StringBuilder();
				j ++;
				sj = j;
			}
		}
	}

	public void setStringProcessor(StringProcessor stringProcessor) {
		this.stringProcessor = stringProcessor;
	}

	public void setExtraSymbols(String extraSymbols) {
		acceptors2 = new AcceptC[] {CharacterAcceptors.createAlphaA(), CharacterAcceptors.createDigitsA(), CharacterAcceptors.createDelimitersA(extraSymbols), CharacterAcceptors.createEverythingA()};
	}

	public void setSteam(boolean steam) {
		this.steam = steam;
	}

}
