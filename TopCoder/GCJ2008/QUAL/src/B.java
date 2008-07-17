import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;


public class B {
	
	static class PairII implements Comparable<PairII> {
		int x,y;
		
		public PairII(int x, int y) {
			this.x = x;
			this.y = y;
		}

		public int compareTo(PairII o) {
			int c = Integer.valueOf(x).compareTo(o.x);
			return c == 0? Integer.valueOf(y).compareTo(o.y) : c;
		}
	}
	
	static PairII makePair(int x, int y) {
		return new PairII(x, y);
	}
	
	
	public String run(BufferedReader in) throws Exception {
		int T, NA, NB;
		int A = 0, B = 0;
		int BA = 0, BB = 0;
		T = Integer.parseInt(in.readLine());
		StringTokenizer st = new StringTokenizer(in.readLine());
		NA = Integer.parseInt(st.nextToken());
		NB = Integer.parseInt(st.nextToken());
		List<PairII> list = new ArrayList<PairII>();		
		for(int i=0; i < NA; i++) {
			st = new StringTokenizer(in.readLine());
			String s = st.nextToken();
			int a = 60*Integer.parseInt(s.substring(0, 2)) + Integer.parseInt(s.substring(3, 5));
			s = st.nextToken();
			int b = 60*Integer.parseInt(s.substring(0, 2)) + Integer.parseInt(s.substring(3, 5));
			list.add(makePair(a, 2));
			list.add(makePair(b+T, 0));
		}
		for(int i=0; i < NB; i++) {
			st = new StringTokenizer(in.readLine());
			String s = st.nextToken();
			int a = 60*Integer.parseInt(s.substring(0, 2)) + Integer.parseInt(s.substring(3, 5));
			s = st.nextToken();
			int b = 60*Integer.parseInt(s.substring(0, 2)) + Integer.parseInt(s.substring(3, 5));
			list.add(makePair(a, 3));
			list.add(makePair(b+T, 1));
		}
		Collections.sort(list);
		for(PairII p : list) {
			switch (p.y) {
				case 0:
					BB ++;
					break;
				case 1:	
					BA ++;
					break;
				case 2:
					if(BA == 0)
						A++;
					else
						BA --;
					break;
				case 3:	
					if(BB == 0)
						B++;
					else
						BB --;
					break;
			}			
		}
		return A + " " + B;
	}
	
	public static void main(String[] args) throws Exception {
		String name = "B-large";		
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
