import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.StringTokenizer;


public class A {
	
	int get(int a, int b) {
		if(a < 0 || b < 0)
			return -1;
		else
			return a + b;
	}
	
	public String run(BufferedReader in) throws Exception {
		StringTokenizer st = new StringTokenizer(in.readLine());		
		int M = Integer.parseInt(st.nextToken());
		int V = Integer.parseInt(st.nextToken());
		
		int[][] VV = new int[2][M];
		Arrays.fill(VV[0], -1);
		Arrays.fill(VV[1], -1);
		int[] G = new int[M];
		int[] C = new int[M];
		for(int i=0; i < M; i++) {
			st = new StringTokenizer(in.readLine());		
			if(i < (M-1)/2) {
				G[i] = Integer.parseInt(st.nextToken());
				C[i] = Integer.parseInt(st.nextToken());				
			} else {
				VV[Integer.parseInt(st.nextToken())][i] = 0;
			}
		}
		for(int i=(M-1)/2-1; i>=0; i--) {			
			if(G[i] == 0 || C[i] == 1) {
				int s = G[i] != 0 ? 1 : 0;				
				int t = get(VV[0][2*i+1], VV[0][2*i+2]);
				if(t >= 0)
					VV[0][i] = VV[0][i] < 0 ? t + s : Math.min(VV[0][i], t + s);
				for(int a=0; a <= 1; a++) {
					for(int b=0; b<=1; b++) {
						if(a + b > 0) {
							t = get(VV[a][2*i+1], VV[b][2*i+2]);
							if(t >= 0)
								VV[1][i] = VV[1][i] < 0? t + s :Math.min(VV[1][i], t + s); 
						}
					}
				}
			} 
			if(G[i] == 1 || C[i] == 1) {
				int s = G[i] != 1 ? 1 : 0;
				for(int a=0; a <= 1; a++) {
					for(int b=0; b<=1; b++) {
						if(a + b < 2) {
							int t = get(VV[a][2*i+1], VV[b][2*i+2]);
							if(t >= 0)
								VV[0][i] = VV[0][i] < 0? t + s :Math.min(VV[0][i], t + s); 
						}
					}
				}
				int t = get(VV[1][2*i+1], VV[1][2*i+2]);
				if(t >= 0)
					VV[1][i] = VV[1][i] < 0 ? t + s : Math.min(VV[1][i], t + s); 
			}
		}
		return VV[V][0] < 0? "IMPOSSIBLE" : String.valueOf(VV[V][0]);
	}
	
	public static void main(String[] args) throws Exception {
		String name = "A-large";		
		BufferedReader in = new BufferedReader(new FileReader(name + ".in"));
		PrintWriter out = new PrintWriter(new BufferedWriter( new FileWriter(name + ".out")));
		int n = Integer.parseInt(in.readLine());
		for(int i=1; i <= n; i++) {
			out.printf("Case #%d: %s\n", i, (new A()).run(in));						
		}				
		in.close();
		out.close();
	}

}
