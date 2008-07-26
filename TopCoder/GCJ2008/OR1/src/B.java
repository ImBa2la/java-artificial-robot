import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;


public class B {
	
	boolean build(int[][] M, int B[], int s, boolean was[]) {
		for(int i=0; i < M[s].length; i++) {
			int o = M[s][i];
			if(B[o] == -1) {
				B[o] = s;
				return true;				
			}
			if(!was[o]) {
				int next = B[o];
				B[o] = s;
				was[o] = true;
				boolean a = build(M, B, next, was);
				if(a)
					return true;
				was[o] = false;
				B[o] = next;
			}
		}
		return false;		
	}
	
	public String run(BufferedReader in) throws Exception {
		int n = Integer.parseInt(in.readLine());
		int m = Integer.parseInt(in.readLine());
		int[][] M = new int[m][];
		
		int[] B = new int[2*n];
		Arrays.fill(B, -1);
		
		for(int i=0; i < m; i++) {
			StringTokenizer st = new StringTokenizer(in.readLine());
			int t = Integer.parseInt(st.nextToken());
			M[i] = new int[t];
			
			for(int j=0; j < t; j++) {
				int x = Integer.parseInt(st.nextToken());
				int y = Integer.parseInt(st.nextToken());
				M[i][j] = y*n + x - 1;
			}
		}
		for(int i=0; i < m; i ++) {
			boolean[] was = new boolean[2*n];
			if(!build(M, B, i, was)) {
				return "IMPOSSIBLE";
			}
		}
		StringBuilder sb = new StringBuilder();
		for(int i=0; i <= 2*n; i++) {
			sb.append();			
		}		
	}
	
	public static void main(String[] args) throws Exception {
		String name = "B";		
		BufferedReader in = new BufferedReader(new FileReader(name + ".in"));
		PrintWriter out = new PrintWriter(new BufferedWriter( new FileWriter(name + ".out")));
		int n = Integer.parseInt(in.readLine());
		for(int i=1; i <= n; i++) {
			out.printf("Case #%d: %s\n", i, (new B()).run(in));						
		}				
		in.close();
		out.close();
	}

}
