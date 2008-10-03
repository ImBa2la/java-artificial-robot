import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;


public class A {
	
	public int run(BufferedReader in) throws Exception {
		int answer = 0;
		int N = Integer.parseInt(in.readLine());
		for(int i=0; i < N; i++) {
			StringTokenizer st = new StringTokenizer(in.readLine());
			String name = st.nextToken();
			int M = Integer.parseInt(st.nextToken());
			int S = 0;
			for(int j=0; j < M; j++) {
				String n = st.nextToken();
				if(n.toUpperCase().equals(n)) {
					S ++;					
				}
			}
			answer = Math.max(answer, S);			
		}
		return answer + 1;
	}

	public static void main(String[] args) throws Exception {
		String name = "A";		
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
