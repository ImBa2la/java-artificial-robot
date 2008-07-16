package ru.styskin.poetry.utils;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * TODO javadoc
 *
 * @author Egor Azanov, <a href="mailto:krondix@yandex-team.ru">
 */
public class SingletonString implements Comparable<SingletonString> {
    private String string;
    private Integer index;
    private int length;
    private int size;
    
    private static int SINGLETON_STRING_INDEX = 0;

    public int getLength() {
		return length;
	}

	private SingletonString(String string) {
        this.string = string;
        this.index = SINGLETON_STRING_INDEX ++;        
        this.length = string.length();
        size = calculateSize(string);
    }

    private static Map<String, SingletonString> cache = Collections.synchronizedMap(new WeakHashMap<String, SingletonString>());
    private static Map<Integer, SingletonString> cacheByIndex = Collections.synchronizedMap(new WeakHashMap<Integer, SingletonString>());

    private static final Object monitor = new Object();

    public synchronized static boolean hasInstance(String string) {
        return cache.containsKey(string);
    }

    public static SingletonString getInstance(String string) {
        synchronized (monitor) {
            SingletonString singletonString = cache.get(string);
            if (singletonString == null) {
                singletonString = new SingletonString(string);
                cache.put(string, singletonString);
                cacheByIndex.put(singletonString.getIndex(), singletonString);
            }
            return singletonString;
        }
    }

    public static SingletonString getByIndex(int index) {
        return cacheByIndex.get(index);
    }

    public String getString() {
        return string;
    }

    public int compareTo(SingletonString o) {
        int compare = o.length - length;
        if (compare == 0) {
            compare = o.string.compareTo(string);
        }
        return compare;
    }

    public String toString() {
        return string;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public static void removeInstance(SingletonString string) {
        removeInstance(string.getIndex(), string.getString());
    }

    public static void removeInstance(int index, String string) {
        synchronized (monitor) {
            cache.remove(string);
            cacheByIndex.remove(index);
        }
    }

    public static int getCacheByIndexSize() {
        return cacheByIndex.size();
    }

    public static int getCacheSize() {
        return cache.size();
    }

    public static Collection<SingletonString> getCache() {
        return cache.values();
    }
    
    private static class SingletoneStringComparator implements Comparator<SingletonString> {

		public int compare(SingletonString o1, SingletonString o2) {			
			return o1.index.compareTo(o2.index);
		}    	
    }
    
    private static final SingletoneStringComparator SSC = new SingletoneStringComparator(); 

    public static Comparator<SingletonString> getComparator() {
    	return SSC;
    }    
    
    public int getSize() {
    	return size;    	
    }
    
    public static final String CONSONANT = "йцкнгшщзхъфвпрлджчсмтьб";
    public static final String VOCALIC = "аеёиоуыэюя";

    private static int CONSONANT_SET = 0;
    private static int VOCALIC_SET = 0;
    
    static {
    	for(int i=0; i < CONSONANT.length(); i++) {
    		CONSONANT_SET |= 1 << CONSONANT.charAt(i) - 'а';
    	}
    	for(int i=0; i < VOCALIC.length(); i++) {
    		VOCALIC_SET |= 1 << VOCALIC.charAt(i) - 'а';
    	}
    }
    
    private static int calculateSize(String s) {
    	int res = 0;
    	for(int i=0; i < s.length(); i++) {
    		if((VOCALIC_SET & (1 << (s.charAt(i)-'а'))) > 0) {
    			res ++;    			
    		}
    	}
		if(s.length() > 2 &&
			(VOCALIC_SET & (1 << (s.charAt(s.length()-1)-'а'))) > 0 && 
			(VOCALIC_SET & (1 << (s.charAt(s.length()-2)-'а'))) > 0) {
			res --;
		}
    	return res;
    }
    
}
