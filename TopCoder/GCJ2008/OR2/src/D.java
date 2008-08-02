import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.StringTokenizer;


public class D {
	
	char[] s;
	char[] res;
	int k;
	
	int compress(int[] m) {
		for(int i=0; i < s.length/k; i++) {
			for(int j=0; j < k; j++) {
				res[k*i + j] = s[k*i + m[j]];				
			}
		}
		int d = 1;
		for(int i=1; i < res.length; i++) {
			if(res[i] != res[i-1])
				d++;
		}
		return d;
	}
	
	
	int calc(int[] m, int p) {
		if(p >= m.length) {
			return compress(m);			
		} else {
			int r = Integer.MAX_VALUE;
			for(int i=p; i < m.length; i++) {
				int t = m[p];
				m[p] = m[i];
				m[i] = t;
				r = Math.min(r, calc(m, p+1));
				t = m[p];
				m[p] = m[i];
				m[i] = t;
			}
			return r;
		}		
	}
	
	public int run(BufferedReader in) throws Exception {
		k = Integer.parseInt(in.readLine());
		int[] m = new int[k];
		for(int i=0; i < k; i++)
			m[i] = i;		
		s = in.readLine().toCharArray();
		res = new char[s.length];		
		return calc(m,0);
	}
	
	public static void main(String[] args) throws Exception {
		String name = "D-small-attempt0";
		BufferedReader in = new BufferedReader(new FileReader(name + ".in"));
		PrintWriter out = new PrintWriter(new BufferedWriter( new FileWriter(name + ".out")));
		int n = Integer.parseInt(in.readLine());
		for(int i=1; i <= n; i++) {
			String s = String.valueOf((new D()).run(in));
			System.out.printf("Case #%d: %s\n", i, s);
			out.printf("Case #%d: %s\n", i, s);						
		}				
		in.close();
		out.close();
	}

}
