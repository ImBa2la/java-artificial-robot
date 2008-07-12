package ru.yandex.utils.string;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeSet;

import javolution.context.ObjectFactory;
import ru.yandex.utils.PairIO;
import ru.yandex.utils.string.aho.Search;
import ru.yandex.utils.string.aho.Vertex;

public class ModernStringUtils {
	
	public static String fastReplace(final String str, final Vertex<Integer> root, final List<String> dict, final List<String> rep) {
		StringBuilder sb = new StringBuilder();
		List<PairIO<Integer>> search = Search.search(str.toLowerCase(), root);
		for(int i=0; i < search.size(); i++) {
			int ind = search.get(i).getSecond();				
			search.get(i).setFirst(search.get(i).getFirst() - dict.get(ind).length() + 1);			
		}
		int current = 0;
		for(int i=0; i < search.size(); i++) {
			if(search.get(i).getFirst() >= current) {
				sb.append(str.substring(current, search.get(i).getFirst()));
				int ind = search.get(i).getSecond();				
				current = search.get(i).getFirst() + dict.get(ind).length();
				sb.append(rep.get(ind));
			}
		}
		if(current < str.length()) {
			sb.append(str.substring(current, str.length()));
		}
		return sb.toString();
	}
	
	private static final String EMPTY_STRING = "";
	
	public static List<String> fastSplit(String string, String delimiters) {
        if (string == null) {
            return Collections.<String>emptyList();
        }		
		List<String> list = new ArrayList<String>();
		StringTokenizer st = new StringTokenizer(string, delimiters);
		while(st.hasMoreTokens()) {
			list.add(st.nextToken());			
		}
		return list;
	}	
	
	public static List<String> split(String string) {
		return fastSplit(normalize(string), " ");
	}

    /**
     * Ќормализует строку:<br>
     * 1. ”дал€ет лишние пробельные символы<br>
     * 2. ѕереводит в lowercase<br>
     * 3. ќбрамл€ет пробелами числа<br>
     * 4. «амен€ет русские символы на такие же по написанию английские ({@link #RUS_2_EN_REPLACEMENTS})<br>
     *
     * @param string строка дл€ нормализации
     * @return нормализованна€ строка, если string != null, пуста€ строка в противном случае
     */
    public static String normalize(String string) {
        if (string == null) {
            return EMPTY_STRING;
        }
        string = normalizeSimple(string);
    	StringTokenizer st = new StringTokenizer(normRus2En2(string), " ");
    	StringBuilder sb = new StringBuilder(10);
    	while(st.hasMoreTokens()) {
    		String s = st.nextToken();
    		int p = 0, c = 0;
    		for(int i=0; i < s.length(); i++) {
    			switch (s.charAt(i)) {
				case '.': p++; break;
				case ',': c++; break;
				}
    		}
    		if(p + c == 0) {
    			sb.append(s);
    		} else if(p + c == 1) {    		
        		for(int i=0; i < s.length(); i++) {
        			sb.append(s.charAt(i) == ','? '.' : s.charAt(i));
        		}
    		} else if(p == 1 && c >= 1) {
        		for(int i=0; i < s.length(); i++) {
        			sb.append(s.charAt(i) == ','? ' ' : s.charAt(i));
        		}
    		} else if(p + c > 1) {
        		for(int i=0; i < s.length(); i++) {
        			sb.append(s.charAt(i) == ',' || s.charAt(i) == '.' ? ' ' : s.charAt(i));
        		}
    		}
    		sb.append(' ');
    	}
    	if(sb.length() >= 1)
    		sb.delete(sb.length() - 1, sb.length());
        return sb.toString();
    }
    
	public static String normalizeSimple(String string) {
		string = string.toLowerCase();
//		string = beautify(string);
        StringBuilder sb = new StringBuilder(100);
        int state = -1, prev = -1;
        char prevC = 0;
        for(char c : string.toCharArray()) {
        	state = Character.isLetter(c) ? 1 : Character.isDigit(c)? 2 : c == '.' || c == ',' ? 3 : 0;
        	switch (prev) {
        	case 0:
        		if(state > 0) {
        			sb.append(' ');
        		}
    			prevC = c;
    			prev = state;
        		break;
			case 1:
				sb.append(prevC);
				if(state == 2) {
					sb.append(' ');
				}
				prevC = c;
				prev = state;
				break;
			case 2:
				sb.append(prevC);
				if(state == 1) {
					sb.append(' ');
				}
				prevC = c;
				prev = state;
				break;
			case 3:
				if(state == 2) {
					sb.append(prevC);
					prevC = c;
					prev = state;
				} else if(state == 3) {
					sb.append(' ');
					prevC = ' ';
					prev = 0;
				} else {
					prevC = c;
					prev = state;
				}
				break;
			default:
	        	prev = state;
				prevC = c;
				break;
			}
        }
    	if(state == 1 || state == 2)
    		sb.append(string.charAt(string.length()-1));
		return sb.toString();
	}

    /**
     * —троит набор синонимов дл€ заданной строки: по очереди убираем пробелы и нормализуем каждый вариант
     *
     * @param string строка
     * @return набор синонимов
     */	
    public static Set<String> generateAliases(String string) {
    	string = normalize(string);
        Set<String> withRemovedSpaces = applySpacesRemover(string);
        Set<String> aliases = new TreeSet<String>();
        for (String alias : withRemovedSpaces) {
            aliases.add(normalize(alias));
        }
        return aliases;
    }

    protected static Set<String> applySpacesRemover(String string) {
        Set<String> aliases = new HashSet<String>();
        aliases.add(string);
        List<String> list = split(string);
        for(int i=1; i < list.size() && i < 5; i ++) {
        	StringBuilder sb = new StringBuilder(string.length());
        	for(int j=0; j < list.size(); j ++) {
        		if(i != j)
        			sb.append(' ');
        		sb.append(list.get(j));        		
        	}
        	aliases.add(sb.toString());
        }
        return aliases;
    }

    private static ObjectFactory<StringBuilder> STRING_BUILDER_FACTORY = new ObjectFactory<StringBuilder>() {
        protected StringBuilder create() {
            return new StringBuilder(100);
        }
    };

    public static String nvl(final String s) {
        return null == s ? EMPTY_STRING : s;
    }
    
	public static String normRus2En2(final String s) {
		char[] res = new char[s.length()];
		final int l = s.length();
		for (int i = 0; i < l; i++) {
			final char ch = s.charAt(i);
			switch (ch) {
			case 'а': res[i] = 'a'; break;
			case 'в': res[i] = 'b'; break;
			case 'е': res[i] = 'e'; break;
			case 'Є': res[i] = 'e'; break;
			case 'й': res[i] = 'и'; break;
			case 'к': res[i] = 'k'; break;
			case 'м': res[i] = 'm'; break;
			case 'о': res[i] = 'o'; break;
			case 'р': res[i] = 'p'; break;
			case 'с': res[i] = 'c'; break;
			case 'т': res[i] = 't'; break;
			case 'у': res[i] = 'y'; break;
			case 'х': res[i] = 'x'; break;
			default : res[i] = ch;
			}
		}
		return new String(res);
	}
	
	public static String join(Iterator<?> it, char c) {
		return org.apache.commons.lang.StringUtils.join(it, c);				
	}
	
	public static String join(Iterator<?> it, String c) {
		return org.apache.commons.lang.StringUtils.join(it, c);				
	}
	

}
