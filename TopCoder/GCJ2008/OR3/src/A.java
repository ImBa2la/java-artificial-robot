import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;


public class A {
	
	private static final int MAX_P = 110;

	static class Point {
		public int x, y;

		public Point(int x, int y) {
			this.x = x;
			this.y = y;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + x;
			result = prime * result + y;
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			final Point other = (Point) obj;
			if (x != other.x)
				return false;
			if (y != other.y)
				return false;
			return true;
		}
		
		public String toString() {
			return "[" + x + ";" + y + "]";
		}		
	}
	
	
	List<Point> xsb = new ArrayList<Point>();
	List<Point> xse = new ArrayList<Point>();
	List<Point> ysb = new ArrayList<Point>();
	List<Point> yse = new ArrayList<Point>();
	
	
	public boolean check(int x, int y) {
		int[][] C = new int[2][2];
		int N = xsb.size();
		boolean[] xBit = new boolean[N];
		boolean[] yBit = new boolean[N];
		
		
		
		
		
				
		for(int i=0; i < N; i++) {
			if((a.x - x) * (b.x - x) < 0) {
				C[0][a.y - y > 0 ? 1 : 0]++;
			}
			if((a.y - y) * (b.y - y) < 0) {
				C[1][a.x - x > 0 ? 1 : 0]++;
			}
		}
		
		return (C[0][0] > 0 && C[0][1] > 0 && C[0][0]%2 == 0) || 
					(C[1][0] > 0 && C[1][1] > 0 && C[1][0]%2 == 0);
	}
	
	public String run(BufferedReader in) throws Exception {
		StringTokenizer st = new StringTokenizer(in.readLine());		
		int L = Integer.parseInt(st.nextToken());
		String[] LL = new String[L];
		int[] LD = new int[L];
		int ii = 0;
		while( ii < L) {
			st = new StringTokenizer(in.readLine());
			while(st.hasMoreTokens()) {
				LL[ii] = st.nextToken();
				LD[ii] = Integer.parseInt(st.nextToken());
				ii ++;				   
			}	
		}
		Set<Point> set = new HashSet<Point>();		
		List<Point> points = new ArrayList<Point>();
		points.add(new Point(0, 0));
		set.add(new Point(0, 0));
		
		int dx = 0, dy = 2;
		int x = 0, y = 0;		
		outer: for(int i =0; i < L; i++) {
			for(int j=0; j < LD[i]; j++) {
				for(int k =0; k < LL[i].length(); k++) {
					if(LL[i].charAt(k) == 'F') {
						x += dx;
						y += dy;
					} else if(LL[i].charAt(k) == 'R') {
						Point np = new Point(x,y);
						if(!np.equals(points.get(points.size()-1))) {
							if(set.contains(np)) {
								break outer;
							}							
							points.add(np);
							set.add(np);
						}
						if(dx == 0) {
							dx = dy;
							dy = 0;							
						} else {
							dy = -dx;
							dx = 0;
						}						
					} else if(LL[i].charAt(k) == 'L') {
						Point np = new Point(x,y);
						if(!np.equals(points.get(points.size()-1))) {
							if(set.contains(np)) {
								break outer;
							}							
							points.add(np);
							set.add(np);
						}
						if(dx == 0) {
							dx = -dy;
							dy = 0;							
						} else {
							dy = dx;
							dx = 0;
						}					
					}
				}
			}			
		}
		Point np = new Point(0, 0);
		if(!np.equals(points.get(points.size()-1))) {
			points.add(np);
		}
		
		
		
		
		for(int i=1; i < points.size(); i++) {
			Point a = points.get(i-1);			
			Point b = points.get(i);
			xsb.add(new Point(a.x, i-1));
			xse.add(new Point(b.x, i-1));
			ysb.add(new Point(a.y, i-1));
			yse.add(new Point(b.y, i-1));
		}
		
		
		
		int S = 0;
		for(int i=-MAX_P; i <= MAX_P; i ++) {
			for(int j=-MAX_P; j <= MAX_P; j ++) {
				if(check(2*i+1, 2*j+1)) {
					S++;
				}
			}
		}
		return String.valueOf(S);
	}
	
	public static void main(String[] args) throws Exception {
		String name = "A-small-practice";		
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
