import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.StringTokenizer;


public class B {
	
	static final double EPS = 1E-2;
	
	
	
	static int square(int x1, int y1, int x2, int y2, int x3, int y3) {
		return Math.abs(x2*y3-x3*y2);		
	}
	
	public String run(BufferedReader in) throws Exception {
		StringTokenizer st = new StringTokenizer(in.readLine());
		int N = Integer.parseInt(st.nextToken());
		int M = Integer.parseInt(st.nextToken());
		int A = Integer.parseInt(st.nextToken());
		int x1,x2,y1,y2;
		x1 = x2 = y1 = y2 = 0;
		if(N*M < A) {
			return "IMPOSSIBLE";
		} else if(N*M == A) {
			x1 = N;
			y2 = M;
		} else if(A % N == 0) {
			x1 = N;
			y2 = A/N;
		} else {
			x1 = N;
			y1 = 1;
			y2 = 1 + A/N;
			x2 = N - A%N;
		}		
		return "0 0 " + x1 + " " + y1 + " " + x2 + " " + y2; 
	}
	
	public static void main(String[] args) throws Exception {
//		String name = "B-small-practice";
		String name = "B-large-practice";
		BufferedReader in = new BufferedReader(new FileReader(name + ".in"));
		PrintWriter out = new PrintWriter(new BufferedWriter( new FileWriter(name + ".out")));
		int n = Integer.parseInt(in.readLine());
		for(int i=1; i <= n; i++) {
			String s = String.valueOf((new B()).run(in));
//			System.out.printf("Case #%d: %s\n", i, s);
			out.printf("Case #%d: %s\n", i, s);						
		}				
		in.close();
		out.close();
	}

}
