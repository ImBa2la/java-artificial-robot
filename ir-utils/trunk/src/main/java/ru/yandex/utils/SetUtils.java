package ru.yandex.utils;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SetUtils {
	
	public static <T> int intersectonList(List<T> a, List<T> b) {
		int M = a.size(), N = b.size();		
        int[][] opt = new int[M+1][N+1];
        // compute length of LCS and all subproblems via dynamic programming
        for (int i = M-1; i >= 0; i--) {
            for (int j = N-1; j >= 0; j--) {
                if (a.get(i).equals(b.get(j))) {
                    opt[i][j] = opt[i+1][j+1] + 1;
                } else { 
                    opt[i][j] = Math.max(opt[i+1][j], opt[i][j+1]);
                }
            }
        }
		return opt[0][0]; 		
	}	

	public static <T> int intersecton(Set<T> a, Collection<T> b) {
		int r = 0;
		for (T s : b) {
			if (a.contains(s)) {
				r++;
			}
		}
		return r;		
	}
	
	
	public static <T> int intersecton2(Set<T> a, Set<T> b) {
		if(a.size() < b.size()) {
			Set<T> c = a;
			a = b;
			b = c;								
		}
		return intersecton(a, b);
	}
	
	public static <T> void increment(Map<T, Integer> dict, T key) {
		if(dict.containsKey(key)) {
			dict.put(key, dict.get(key) + 1);
		} else {
			dict.put(key, 1);
		}
	}
	
	public static <T> void increment(Map<T, Integer> dict, T key, int v) {
		if(dict.containsKey(key)) {
			dict.put(key, dict.get(key) + v);
		} else {
			dict.put(key, v);
		}
	}
	
	public static long createLong(int a, int b) {
		return ((long) a << 32) | b;		
	}
	
	public static int getFirst(long a) {
		return (int) (a >> 32);
	}
	
	public static int getSecond(long a) {
		return (int) a;
	}

	
	public static <T> T getMaxKey(Map<T, Integer> dict) {
		if(dict.size() == 0) {
			return null;
		}		
		Int2ObjectMap<T> map = new Int2ObjectOpenHashMap<T>();
		long[] m = new long[dict.size()];
		int index = 0;
		for(T key : dict.keySet()) {
			map.put(index, key);
			m[index] = createLong(-dict.get(key), index);
			index ++;
		}
		Arrays.sort(m);
		return map.get((int)m[0]);	
	}
	
	public static <T> T getMinKey(Map<T, Integer> dict) {
		if(dict.size() == 0) {
			return null;
		}		
		Int2ObjectMap<T> map = new Int2ObjectOpenHashMap<T>();
		long[] m = new long[dict.size()];
		int index = 0;
		for(T key : dict.keySet()) {
			map.put(index, key);
			m[index] = createLong( dict.get(key), index);
			index ++;
		}
		Arrays.sort(m);
		return map.get(m[0]);	
	}	
	
	public static <T> Map<T, Integer> calcDistribution(List<T> distribution) {
		Map<T, Integer> map = new HashMap<T, Integer>();
		for(T key : distribution) {
			increment(map, key);	
		}
		return map;
	}
}
