import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;


public class B {
	
	public String run(BufferedReader in) throws Exception {		
		return "";
	}
	
	public static void main(String[] args) throws Exception {
		String name = "B";		
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
