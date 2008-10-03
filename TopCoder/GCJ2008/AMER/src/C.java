import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Locale;
import java.util.StringTokenizer;


public class C {
	
	int M;
	int Q;
	double[][] F;
	
	double probability(int C, int mask) {
		int[] it = new int[Q];
		while(true) {
			int nI = -1;
			for(int j=0; j < Q; j++) {
				if(((mask >>> j) & 1) == 1) {
					if(it[j] + 1 < 4) {
						if(nI < 0 || F[nI][it[nI] + 1] > F[j][it[j] + 1]) {
							nI = j;
						}
					}
				}
			}
			if(nI >= 0) {
				double P = 1;
				int TC = 1;
				int TC_CORRECT = 1;
				for(int j=0; j < Q; j++) {
					if(((mask >>> j) & 1) == 1) {
						TC *= it[j] + (j == nI ?2:1);
						TC_CORRECT *= it[j] + 1;
					}
				}				
				if(TC >= C) {
					if(TC == C)
						it[nI] ++;
					for(int j=0; j < Q; j++) {
						if(((mask >>> j) & 1) == 1) {
							double s = 0;
							for(int l=0; l <= it[j]; l++) {
								s += -F[j][l];
							}							
							P *= s;
						}
					}
					if(TC != C)					
						P += -F[nI][it[nI]+1]*probability(C - TC_CORRECT, mask ^ (1 << nI));	
					return P;
				}
				it[nI] ++;
			} else {
				break;
			}
		}
		return 1;
	}
	
	
	public double run(BufferedReader in) throws Exception {
		StringTokenizer st = new StringTokenizer(in.readLine());
		M = Integer.parseInt(st.nextToken());
		Q = Integer.parseInt(st.nextToken());
		F = new double[Q][4];
		for(int i=0; i < Q; i ++) {
			st = new StringTokenizer(in.readLine());
			for(int j=0; j < 4; j++)				
				F[i][j]	= -Double.parseDouble(st.nextToken());
			Arrays.sort(F[i]);			
		}
		return probability(M, ((1 << 31) - 1));
	}

	public static void main(String[] args) throws Exception {
		Locale.setDefault(Locale.US);
		String name = "C-small-practice";		
		BufferedReader in = new BufferedReader(new FileReader(name + ".in"));
		PrintWriter out = new PrintWriter(new BufferedWriter( new FileWriter(name + ".out")));
		int n = Integer.parseInt(in.readLine());
		for(int i=1; i <= n; i++) {
			out.printf("Case #%d: %19.18f\n", i, (new C()).run(in));						
		}				
		in.close();
		out.close();
	}

}
