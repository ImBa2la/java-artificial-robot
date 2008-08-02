import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.StringTokenizer;


public class B {
	
	static final double EPS = 1E-12;
	
	static double sqr(double a) {
		return a*a;		
	}
	
	static double dist(double x1, double y1, double x2, double y2) {
		return Math.sqrt(sqr(x1-x2) + sqr(y1-y2));
	}
	
	static double square(int x1, int y1, int x2, int y2, int x3, int y3) {
		double A = dist(x1,y1,x2,y2);
		double B = dist(x1,y1,x3,y3);
		double C = dist(x3,y3,x2,y2);
		double p = (A+ B+ C)/2;
		return Math.sqrt(p*(p-A)*(p-B)*(p-C));		
	}
	
	static String[][][] answer = new String[51][51][2501]; 
	
	static void init() {
		for(int N=50; N <= 50; N++) {
			for(int M=N; M <= 50; M++) {
				Arrays.fill(answer[N][M], "IMPOSSIBLE");
				int x1 = 0;
				int y1 = 0;
				int x2 = 0;
				int y2 = 0;
				int x3 = 0;
				int y3 = 0;
				for(x2=0; x2 <= M; x2++) {
					for(y2= 0; y2 <= N; y2++) {
						for(x3=0; x3 <= M; x3++) {						
							for(y3= y1; y3 <= N; y3++) {
								double S = square(x1, y1, x2, y2, x3, y3)*2;
								if(Math.abs(S - Math.round(S)) < EPS)
									answer[N][M][(int)Math.round(S)] = ""+x1+" "+y1+" "+x2+" "+y2+" "+x3+" "+y3; 
							}			
						}		
						
					}			
				}
				x1 = 0;
				y1 = 0;
				x2 = 0;
				y2 = 0;
				x3 = 0;
				y3 = 0;
				for(x1=0; x2 <= M; x2++) {
					for(y2= 0; y2 <= N; y2++) {
						for(x3=0; x3 <= M; x3++) {						
							for(y3= y1; y3 <= N; y3++) {
								double S = square(x1, y1, x2, y2, x3, y3)*2;
								if(Math.abs(S - Math.round(S)) < EPS)
									answer[N][M][(int)Math.round(S)] = ""+x1+" "+y1+" "+x2+" "+y2+" "+x3+" "+y3; 
							}			
						}		
						
					}			
				}
				System.out.println(N+ " " + M);
				System.out.flush();
			}
		}		
	}
	
	
	public String run(BufferedReader in) throws Exception {
		StringTokenizer st = new StringTokenizer(in.readLine());
		int N = Integer.parseInt(st.nextToken());
		int M = Integer.parseInt(st.nextToken());
		if(N > M) {
			int t = N;
			N = M;
			M = t;
		}
		int A = Integer.parseInt(st.nextToken());
		if(answer[N][M][A] != null) {
			return answer[N][M][A];
		}
		int x1 = 0;
		int y1 = 0;
		int x2 = 0;
		int y2 = 0;
		int x3 = 0;
		int y3 = 0;
		for(x2=0; x2 <= M; x2++) {
			for(y2= 0; y2 <= N; y2++) {
				for(x3=0; x3 <= M; x3++) {						
					for(y3= y1; y3 <= N; y3++) {
						double S = square(x1, y1, x2, y2, x3, y3)*2;
						if(Math.abs(S - A) < EPS)
							return ""+x1+" "+y1+" "+x2+" "+y2+" "+x3+" "+y3;
					}			
				}		
				
			}			
		}
		x1 = 0;
		y1 = 0;
		x2 = 0;
		y2 = 0;
		x3 = 0;
		y3 = 0;
		for(x1=0; x2 <= M; x2++) {
			for(y2= 0; y2 <= N; y2++) {
				for(x3=0; x3 <= M; x3++) {						
					for(y3= y1; y3 <= N; y3++) {
						double S = square(x1, y1, x2, y2, x3, y3)*2;
						if(Math.abs(S - A) < EPS)
							return ""+x1+" "+y1+" "+x2+" "+y2+" "+x3+" "+y3;
					}			
				}		
				
			}			
		}
		return "IMPOSSIBLE";
	}
	
	public static void main(String[] args) throws Exception {
		String name = "B-small-attempt2";
		BufferedReader in = new BufferedReader(new FileReader(name + ".in"));
		PrintWriter out = new PrintWriter(new BufferedWriter( new FileWriter(name + ".out")));
		int n = Integer.parseInt(in.readLine());
		init();
		for(int i=1; i <= n; i++) {
			String s = String.valueOf((new B()).run(in));
			System.out.printf("Case #%d: %s\n", i, s);
			out.printf("Case #%d: %s\n", i, s);						
		}				
		in.close();
		out.close();
	}

}
