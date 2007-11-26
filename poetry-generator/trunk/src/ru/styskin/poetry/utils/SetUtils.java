package ru.styskin.poetry.utils;

import java.util.Collection;
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
                if (a.get(i).equals(b.get(j)))
                    opt[i][j] = opt[i+1][j+1] + 1;
                else 
                    opt[i][j] = Math.max(opt[i+1][j], opt[i][j+1]);
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
	
}
