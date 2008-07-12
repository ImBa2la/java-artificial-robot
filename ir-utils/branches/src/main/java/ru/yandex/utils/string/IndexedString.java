package ru.yandex.utils.string;

import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ru.yandex.utils.Accept;

public class IndexedString {
	
	public static final double EPS = 1E-15;
	
	private static final StringProcessor processor = new StemmerProcessor();
	
	public static interface NumberEquals extends Accept<Number> {}
	
	public static final NumberEquals ANY_NUMBER = new NumberEquals() {
		public boolean accept(Number o) {
			return true;		
		}
	};	
	
	public static class PrecisionNumberEquals implements NumberEquals  {
		
		private double a;
		private double precision;
		
		public PrecisionNumberEquals(double a) {
			precision = 0.5;
			this.a = pRound(a);
		}
		
		public PrecisionNumberEquals(double a, double precision) {
			this.precision = precision;
			this.a = pRound(a);
		}
		
		public boolean accept(Number o) {
			return Math.abs(a - pRound(o.doubleValue())) < EPS;
		}

		private double pRound(double a) {
			return a - Math.floor(a) < precision? Math.floor(a) : Math.ceil(a);
		}
		
	}
	
	public static class ExactEquals implements NumberEquals  {
		
		private double a;

		public ExactEquals(double a) {
			this.a = a;
		}

		public boolean accept(Number o) {
			return Math.abs(a - o.doubleValue()) < EPS;
		}
	}
	
	
	public static class RangeNumberEquals implements NumberEquals  {
		
		private double a, b;

		private RangeNumberEquals(double a, double b) {
			this.a = Math.min(a, b);
			this.b = Math.max(a, b);
		}
				
		public boolean accept(Number A) {
			return A.doubleValue() > a - EPS && A.doubleValue() < b + EPS;
		}
		
	}
	
	public static class IndexedToken {
		
		private int start;
		private int end;
		private int type;
		
		public IndexedToken(int start, int end, int type) {
			this.start = start;
			this.end = end;
			this.type = type;
		}

		public int getStart() {
			return start;
		}

		public int getEnd() {
			return end;
		}

		public int getType() {
			return type;
		}
	}	
	
	private String stringToFind;
	private boolean regex = false;
	private List<IndexedToken> tokens;
	private SortedMap<Position, Number> numbers;
	private Int2IntMap startIndex = new Int2IntOpenHashMap(); {startIndex.defaultReturnValue(-1);}
	
	private String normalizedString;
	private Int2IntMap normalizeMap = new Int2IntOpenHashMap(); {normalizeMap.defaultReturnValue(-1);}
	private int firstReal = -1, lastReal = -1;
	private int firstExtra = -1, lastExtra = -1;
	
	IndexedString(String stringToFind, boolean regex) {
		this.stringToFind = stringToFind;
		this.regex = regex;
	}


	IndexedString(String stringToFind, List<IndexedToken> list, SortedMap<Position, Number> numbers, boolean steam) {
		this.numbers = numbers;
		this.stringToFind = stringToFind;
		tokens = list;
		StringBuilder sb = new StringBuilder(" ");
		String s = stringToFind.toLowerCase();
		int currentSpace = 0;
		for(int i=0; i < list.size(); i ++) {
			IndexedToken token = list.get(i);
			startIndex.put(token.start, i);
			if(token.type <= 1) {
				String ts = s.substring(token.start, token.end);
				if(steam)
					ts = processor.process(ts);
				sb.append(ts).append(' ');
				normalizeMap.put(currentSpace, i);
				currentSpace = sb.length() - 1;
				if(firstReal == -1)
					firstReal = i;
				lastReal = i;
			}
		}
		cycle: for(int i=firstReal-1; i >=0; i--) {
			IndexedToken token = list.get(i);
			switch (token.type) {
				case 3: continue cycle;
				case 2:
					firstExtra = i;
				default:
					break cycle;
			}
		}
		cycle: for(int i=lastReal+1; i < tokens.size(); i++) {
			IndexedToken token = list.get(i);
			switch (token.type) {
				case 3: continue cycle;
				case 2:
					lastExtra = i;
				default:
					break cycle;
			}
		}		
		normalizedString = sb.toString();
	}
	
	public List<Position> search(NumberEquals acceptor) {
		List<Position> list = new ArrayList<Position>();
		for(Map.Entry<Position, Number> entry :	numbers.entrySet()) {
			if(acceptor.accept(entry.getValue())) {
				list.add(entry.getKey());				
			}
		}		
		return list;
	}
	
	private int findTokenByIndex(int index) {
		int a = 0, b = tokens.size() -1, c;
		while(b - a > 1) {
			c = (a + b) /2;
			if(tokens.get(c).start <= index)
				a = c;
			else if(tokens.get(c).start > index)
				b = c;			
		}
		if(tokens.get(b).start == index)
			return b;
		else
			return a;
	}
	
	
	public List<Position> searchRegex(String regex) {
		Pattern pattern = Pattern.compile(regex, Pattern.UNICODE_CASE);
		Matcher matcher = pattern.matcher(stringToFind);
		List<Position> list = new ArrayList<Position>();
		int b = 0;
		while(matcher.find(b)) {
			int start = matcher.start();
			int end = matcher.end();
			list.add(new Position(findTokenByIndex(start+1), findTokenByIndex(end)-1));			
			b = end;
		}
		return list;
	}
	

	public List<Position> search(IndexedString search) {
		if(search.regex) {
			return searchRegex(search.stringToFind);
		}
		List<Position> list = new ArrayList<Position>();
		if(search.normalizedString.length() == 1) {
			if(search.lastExtra >= 0) {
				for(int i=0; i < tokens.size(); i ++) {
					if(tokens.get(i).type == 2 && showToken(i).startsWith(search.showToken(search.lastExtra))) {
						list.add(new Position(i, i));
						return list;
					}
				}
			}
		} else {		
			int begin = 0;
			while(begin < normalizedString.length() && begin >= 0) {
				begin = normalizedString.indexOf(search.normalizedString, begin);
				if(begin >= 0) {				
					int start = normalizeMap.get(begin);
					int end = normalizeMap.get(begin + search.normalizedString.length() - 1);				
					end = end <= 0? lastReal : end - 1;
					while(end > start && tokens.get(end).type > 1)
						end --;
					int a = start - 1;
					while(search.firstExtra >=0 && a >=0 && tokens.get(a).type > 2) a --;
					if(search.firstExtra >=0 && a >=0 && tokens.get(a).type == 2 && 
							showToken(a).startsWith(search.showToken(search.firstExtra)))
						start = a;
					a = end + 1;
					while(search.lastExtra >=0 && a < tokens.size() && tokens.get(a).type > 2) a ++;
					if(search.lastExtra >=0 && a < tokens.size() && tokens.get(a).type == 2 && 
							showToken(a).startsWith(search.showToken(search.lastExtra)))
						end = a;
					list.add(new Position(start, end));
					begin ++;
				}
			}
		}
		return list;
	}
	
	public String showToken(int index) {
		IndexedToken token = tokens.get(index);
		return stringToFind.substring(token.start, token.end);
	}	
	
	public String show(Position position) {
		return stringToFind.substring(tokens.get(position.getStart()).start, tokens.get(position.getEnd()).end);
	}
	
	public Number showNumber(Position position) {
		return numbers.get(position);
	}
	
	public List<IndexedToken> slice(int begin, int end) {
		return tokens.subList(begin, end + 1);
	}

	@Override
	public String toString() {
		return stringToFind;
	}

	public List<IndexedToken> getTokens() {
		return tokens;
	}

	@Override
	public int hashCode() {
		return stringToFind.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final IndexedString other = (IndexedString) obj;
		if (stringToFind == null) {
			if (other.stringToFind != null)
				return false;
		} else if (!stringToFind.equals(other.stringToFind))
			return false;
		return true;
	}


	public boolean isRegex() {
		return regex;
	}
	
	
	
}
