import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Locale;
import java.util.StringTokenizer;


public class C {
	
	static final double EPS = 1E-10;
	
	static double sqr(double a) {
		return a*a;
	}
	
	static double dist(double x1, double y1, double x2, double y2) {
		return Math.sqrt(sqr(x1-x2) + sqr(y1-y2));
	}
	
	static double trArea(double x1, double y1, double x2, double y2, double x3, double y3) {
		double a = dist(x1,y1, x2,y2);
		double b = dist(x1,y1, x3,y3);
		double c = dist(x3,y3, x2,y2);
		double p = (a+b+c)/2;
		return Math.sqrt(p*(p-a)*(p-b)*(p-c));
	}
	
	static double specArea(double x1, double y1, double x2, double y2, double R) {
		double h = dist(x1,y1, x2,y2);
		double A = 2 * Math.asin(h/(2*R));
		return sqr(R)*(A - Math.sin(A))/2;
	}
	
	static double area(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4, double R) {
		if(sqr(x3) + sqr(y3) < sqr(R) + EPS) {
			return (x3-x1)*(y3-y1);
		} else if(sqr(x1) + sqr(y1) + EPS > sqr(R)) {
			return 0;			
		}
		double x2n = -1,y2n = -1, x4n = -1,y4n = -1;
		if(sqr(x2) + sqr(y2) < sqr(R) + EPS) {
			y2n = Math.sqrt(sqr(R) - sqr(x2));
		} else {
			x2n = Math.sqrt(sqr(R) - sqr(y2));
		}
		if(sqr(x4) + sqr(y4) < sqr(R) + EPS) {
			x4n = Math.sqrt(sqr(R) - sqr(y4));
		} else {
			y4n = Math.sqrt(sqr(R) - sqr(x4));
		}
		if(x2n > -EPS && y4n > -EPS) {
			return trArea(x1, y1, x2n, y2, x4, y4n) + specArea(x2n, y2, x4, y4n, R); 
		} else if(y2n > -EPS && x4n > -EPS) {
			return trArea(x1, y1, x2, y2, x4, y4) +
			trArea(x2, y2, x4, y4, x2, y2n) + trArea(x4, y4, x2, y2n, x4n, y4) + 
				specArea(x2, y2n, x4n, y4, R);
		} else if(y2n > -EPS && y4n > -EPS) {
			return trArea(x1, y1, x2, y2, x4, y4n) + trArea(x4, y4n, x2, y2, x2, y2n) + 
				specArea(x2, y2n, x4, y4n, R);
		} else if(x2n > -EPS && x4n > -EPS) {
			return trArea(x1, y1, x2n, y2, x4, y4) + trArea(x4, y4, x2n, y2, x4n, y4) + 
			specArea(x2n, y2, x4n, y4, R);
		}
		assert(false);
		return 0;
	}
	
	public double run(BufferedReader in) throws Exception {
		double L, S = 0;
		double f, R, t, r, g;
		StringTokenizer st = new StringTokenizer(in.readLine());
		f = Double.parseDouble(st.nextToken());
		R = Double.parseDouble(st.nextToken());
		t = Double.parseDouble(st.nextToken());
		r = Double.parseDouble(st.nextToken());
		g = Double.parseDouble(st.nextToken()); 
		L = Math.PI *sqr(R)/4;
		
		int MAX_STRING = 1500;
		double s = g - 2*f;
		if(s < EPS || R - t - f < EPS) {
			return 1;
		}
		for(int i=0; i < MAX_STRING; i ++) {
			for(int j=0; j < MAX_STRING; j ++) {
				double x = r + (g + 2*r)*i + f;
				double y = r + (g + 2*r)*j + f;
				S += area(x, y, x + s, y, x + s, y + s, x, y + s, R - t - f);
			}
		}
		return 1-S/L;
	}
	
	public static void main(String[] args) throws Exception {
		Locale.setDefault(Locale.US);		
		String name = "C-small-attempt0";		
		BufferedReader in = new BufferedReader(new FileReader(name + ".in"));
		PrintWriter out = new PrintWriter(new BufferedWriter( new FileWriter(name + ".out")));
		int n = Integer.parseInt(in.readLine());
		for(int i=1; i <= n; i++) {
			out.printf("Case #%d: %8.6f\n", i, (new C()).run(in));						
		}				
		in.close();
		out.close();
	}

}
