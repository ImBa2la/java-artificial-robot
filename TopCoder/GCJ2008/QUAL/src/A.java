import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


public class A {
	
	
	public int run(BufferedReader in) throws Exception {
		int S, Q;
		Map<String, Integer> map = new HashMap<String, Integer>();
		S = Integer.parseInt(in.readLine());
		for(int i=0; i < S; i++) {
			map.put(in.readLine(), i);			
		}
		Q = Integer.parseInt(in.readLine());
		int[][] M = new int[Q][S];
		for(int j=0; j < Q; j ++) {
			Arrays.fill(M[j], Integer.MAX_VALUE);
		}		
		for(int j=0; j < Q; j++) {
			String s = in.readLine();
			int i = map.get(s);
			if(j == 0) {
				Arrays.fill(M[0], 0);
				M[0][i] = -1;
			} else {
				M[j][i] = -1;
				for(int l = 0; l < S; l ++) {
					if(l != i) {
						for(int l2=0; l2 < S; l2++) {
							if(M[j-1][l2] >=0) {
								M[j][l] = Math.min(M[j][l], M[j-1][l2] + (l == l2? 0 : 1));
							}
						}
					}
				}
			}
		}
		if(Q == 0)
			return 0;		
		int m = Integer.MAX_VALUE;		
		for(int i=0; i < S; i++)
			if(M[Q-1][i] >= 0)
				m = Math.min(M[Q-1][i], m);
		return m;
	}
	
	public static void main(String[] args) throws Exception {
		String name = "A-large";		
		BufferedReader in = new BufferedReader(new FileReader(name + ".in"));
		PrintWriter out = new PrintWriter(new BufferedWriter( new FileWriter(name + ".out")));
		int n = Integer.parseInt(in.readLine());
		for(int i=1; i <= n; i++) {
			out.printf("Case #%d: %d\n", i, (new A()).run(in));						
		}				
		in.close();
		out.close();
	}

}
