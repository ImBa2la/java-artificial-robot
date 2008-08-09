import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.StringTokenizer;


public class D {
	
	final static int MOD = 10007;
	
	public String run(BufferedReader in) throws Exception {
		StringTokenizer st = new StringTokenizer(in.readLine());		
		int H = Integer.parseInt(st.nextToken());
		int W = Integer.parseInt(st.nextToken());
		int R = Integer.parseInt(st.nextToken());
		
		int[][] M = new int[H][W];
		for(int i=0; i < R; i++) {
			st = new StringTokenizer(in.readLine());
			int r = Integer.parseInt(st.nextToken());
			int c = Integer.parseInt(st.nextToken());
			M[r-1][c-1] = -1;
		}
		int ni,nj;
		M[0][0] = 1;
		for(int i=0; i < H; i++) {
			for(int j=0; j < W; j++) {
				if(M[i][j] > 0) {
					ni = i + 1;
					nj = j + 2;
					if(ni < H && nj < W && M[ni][nj] >= 0) {
						M[ni][nj] += M[i][j];
						M[ni][nj] %= MOD;
					}
					ni = i + 2;
					nj = j + 1;
					if(ni < H && nj < W && M[ni][nj] >= 0) {
						M[ni][nj] += M[i][j];
						M[ni][nj] %= MOD;
					}
				}
			}			
		}
		int v = M[H-1][W-1] < 0 ? 0 : M[H-1][W-1];		
		return String.valueOf(v);
	}
	
	public static void main(String[] args) throws Exception {
		String name = "D-small-attempt0";		
		BufferedReader in = new BufferedReader(new FileReader(name + ".in"));
		PrintWriter out = new PrintWriter(new BufferedWriter( new FileWriter(name + ".out")));
		int n = Integer.parseInt(in.readLine());
		for(int i=1; i <= n; i++) {
			out.printf("Case #%d: %s\n", i, (new D()).run(in));						
		}				
		in.close();
		out.close();
	}

}
