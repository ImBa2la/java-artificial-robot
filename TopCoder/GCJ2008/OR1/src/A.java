import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;


public class A {
	
	int rec(int i, int n, List<Long> a, List<Long> b) {
		if(i == n) {
			int p = 0;
			for(int j=0; j < n; j++) {
				p += (int)(a.get(j) * b.get(j));
			}			
			return p;  			
		} else {
			int M = Integer.MAX_VALUE;
			for(int j=i; j < n; j++) {
				Long v = b.get(j);
				b.set(j, b.get(i));
				b.set(i, v);				
				M = Math.min(M, rec(i+1, n, a, b));
				v = b.get(j);
				b.set(j, b.get(i));
				b.set(i, v);
			}
			return M;
		}
	}
	
	
	public long run(BufferedReader in) throws Exception {
		int n = Integer.parseInt(in.readLine());
		List<Long> a = new ArrayList<Long>(), b = new ArrayList<Long>();
		StringTokenizer st = new StringTokenizer(in.readLine());		
		for(int i=0; i < n; i ++) {
			a.add(Long.parseLong(st.nextToken()));
		}
		st = new StringTokenizer(in.readLine());
		for(int i=0; i < n; i ++) {
			b.add(Long.parseLong(st.nextToken()));
		}
		Collections.sort(a);
		Collections.sort(b);
//		int A = rec(0, n, a, b);
		long B = 0;
		for(int i=0; i < n; i ++) {
			B += a.get(i) * b.get(n - i - 1);
		}
//		if(A != B) {
//			System.out.println("AAAAAAAAAAAa");
//		}		
		return B;
	}
	
	public static void main(String[] args) throws Exception {
		String name = "A-large";		
		BufferedReader in = new BufferedReader(new FileReader(name + ".in"));
		PrintWriter out = new PrintWriter(new BufferedWriter( new FileWriter(name + ".out")));
		int n = Integer.parseInt(in.readLine());
		for(int i=1; i <= n; i++) {
			out.printf("Case #%d: %s\n", i, String.valueOf((new A()).run(in)));						
		}				
		in.close();
		out.close();
	}

}
