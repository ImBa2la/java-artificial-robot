package ru.styskin.poetry.utils;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

/**
 * TODO javadoc
 *
 * @author Egor Azanov, <a href="mailto:krondix@yandex-team.ru">
 */
public class SingletonString implements Comparable<SingletonString> {
    private static int ID_COUNTER;

    private String string;
    private Integer index;
    private int length;
    private int size;

    public int getLength() {
		return length;
	}

	private SingletonString(String string) {
        this.string = string;
        this.length = string.length();
        size = calculateSize(string);
        index = ID_COUNTER++;
    }

    private static Map<String, Integer> refCount = Collections.synchronizedMap(new HashMap<String, Integer>());
    private static Map<String, SingletonString> cache = Collections.synchronizedMap(new HashMap<String, SingletonString>());
    private static Map<Integer, SingletonString> cacheByIndex = Collections.synchronizedMap(new HashMap<Integer, SingletonString>());

    private static final Object monitor = new Object();

    public static Map<String, Integer> getRefCount() {
        return refCount;
    }

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
                refCount.put(string, 0);
            }
            refCount.put(string, refCount.get(string) + 1);
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
            refCount.put(string, refCount.get(string) - 1);
            if (refCount.get(string) == 0) {
                //logger.debug("Removing string " + string);
                cache.remove(string);
                cacheByIndex.remove(index);
                refCount.remove(string);
            }
        }
    }

    public static int getCacheByIndexSize() {
        return cacheByIndex.size();
    }

    public static int getRefCountSize() {
        return refCount.size();
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
    
    public static final String CONSONANT = "éöêíãøùçõôâïðëäæáòìñ÷";
    public static final String VOCALIC = "àå¸èîóûýþÿ";

    private static int CONSONANT_SET = 0;
    private static int VOCALIC_SET = 0;
    
    static {
    	for(int i=0; i < CONSONANT.length(); i++) {
    		CONSONANT_SET |= 1 << CONSONANT.charAt(i) - 'à';
    	}
    	for(int i=0; i < VOCALIC.length(); i++) {
    		VOCALIC_SET |= 1 << VOCALIC.charAt(i) - 'à';
    	}
    }
    
    private static int calculateSize(String s) {
    	int res = 0;
    	for(int i=0; i < s.length(); i++) {
    		if((VOCALIC_SET & (1 << (s.charAt(i)-'à'))) > 0) {
    			res ++;    			
    		}
    	}
		if(s.length() > 2 &&
			(VOCALIC_SET & (1 << (s.charAt(s.length()-1)-'à'))) > 0 && 
			(VOCALIC_SET & (1 << (s.charAt(s.length()-2)-'à'))) > 0) {
			res --;
		}
    	return res;
    }
    
}
