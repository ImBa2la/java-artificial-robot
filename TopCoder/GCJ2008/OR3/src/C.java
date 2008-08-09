import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.StringTokenizer;


public class C {
	
	static int[] MASK = new int[10];
	
	static {
		int o = 1;
		for(int i=0; i < MASK.length; i ++) {
			MASK[i] = o;
			o <<= 1;
		}
	}
	
	static boolean getB(int a, int p) {
		return (a & MASK[p]) > 0;
	}
	
	static int count(int N, int p) {
		int s = 0;
		for(int i=0; i < N; i++) {
			if(getB(p, i)) {
				s ++;
			}
		}
		return s;		
	}
	
	boolean lineCheck(int N, int b) {
		for(int i=0; i < N; i++) {
			if(i > 0 && getB(b, i) && getB(b, i-1)) {
				return false;				
			}
		}
		return true;
	}
	
	boolean canFollow(int N, int a, int b) {
		for(int i=0; i < N; i++) {
			if(i > 0 && getB(b, i) && getB(b, i-1)) {
				return false;				
			}
			if(i > 0 && getB(b, i) && getB(a, i-1)) {
				return false;				
			}
			if(i < N-1 && getB(b, i) && getB(a, i+1)) {
				return false;				
			}
		}
		return true;
	}
	
	
	
	
	public String run(BufferedReader in) throws Exception {
		StringTokenizer st = new StringTokenizer(in.readLine());		
		int M = Integer.parseInt(st.nextToken());
		int N = Integer.parseInt(st.nextToken());
		int[] B = new int[M];
		for(int i=0; i < M; i++) {
			int a = 0;
			String s = in.readLine();
			int o = 1;
			for(int j=0; j < N; j ++) {
				if(s.charAt(j) == 'x') {
					a |= o;
				}
				o <<= 1;
			}
			B[i] = a;
		}
		int S = 1 << N;
		int[] COUNT = new int[S];
		for(int i=0; i < S; i++) {
			COUNT[i] = count(N, i);
		}
		int[][] D = new int[M][S];
		for(int i=0; i < M; i++) {
			Arrays.fill(D[i], -1);
		}
		for(int i=0; i < S; i++) {
			if((i & B[0]) == 0 && lineCheck(N, i)) {
				D[0][i] = COUNT[i];
			}
		}
		for(int i=0; i < M-1; i++) {
			for(int j=0; j < S; j ++) {
				for(int k=0; k < S; k ++) {
					if((k & B[i+1]) == 0 && canFollow(N, j, k)) {
						D[i+1][k] = Math.max(D[i+1][k], D[i][j] + COUNT[k]);
					}					
				}				
			}
		}
		int mm = D[M-1][0];
		for(int i=1; i < S; i++) {
			mm = Math.max(mm, D[M-1][i]);
		}
		return String.valueOf(mm);
	}
	
	public static void main(String[] args) throws Exception {
		String name = "C-small-attempt0";		
		BufferedReader in = new BufferedReader(new FileReader(name + ".in"));
		PrintWriter out = new PrintWriter(new BufferedWriter( new FileWriter(name + ".out")));
		int n = Integer.parseInt(in.readLine());
		for(int i=1; i <= n; i++) {
			out.printf("Case #%d: %s\n", i, (new C()).run(in));						
		}				
		in.close();
		out.close();
	}

}
