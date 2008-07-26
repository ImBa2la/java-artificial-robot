import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.BigInteger;


public class C {
	
	int[] res = {};
	
	BigDecimal C(int i, int n) {
		BigDecimal v = BigDecimal.ONE;
		for(int j=i+1; j <= n; j++)
			v = v.multiply(BigDecimal.valueOf(j));
		for(int j=2; j <= n-i; j++)
			v = v.divide(BigDecimal.valueOf(j));
		return v;
	}

	BigDecimal calc(int n) {
		BigDecimal p3 = BigDecimal.valueOf(3);
		BigDecimal p5 = BigDecimal.valueOf(5);
		
		BigDecimal v3 = BigDecimal.ZERO;
		BigDecimal v5 = BigDecimal.ZERO;		
		for(int i=0; i <= n; i ++) {
			BigDecimal vv = p3.pow(i).multiply(p5.pow((n-i)/2)).multiply(C(i, n));			
			if((n-i) % 2 == 0)
				v3 = v3.add(vv);
			else
				v5 = v5.add(vv);
		}
		v5 = v5.multiply(v5).multiply(p5);
		v5 = bigRoot(v5, 2, 1000);
		
		return v3.add(v5);
	}
	
	public String run(BufferedReader in) throws Exception {
		int n = Integer.parseInt(in.readLine());
		String s = "0";
		BigDecimal a = calc(n);
		s = String.valueOf(a.remainder(BigDecimal.valueOf(1000)).intValue());
		while(s.length() < 3) {
			s = "0" + s;
		}
		return s;
	}
	
	public static void main(String[] args) throws Exception {
		String name = "C-small-attempt3";		
		BufferedReader in = new BufferedReader(new FileReader(name + ".in"));
		PrintWriter out = new PrintWriter(new BufferedWriter( new FileWriter(name + ".out")));
		int n = Integer.parseInt(in.readLine());
		for(int i=1; i <= n; i++) {
			out.printf("Case #%d: %s\n", i, (new C()).run(in));						
		}				
		in.close();
		out.close();
	}
	
	public static BigDecimal bigRoot(BigDecimal argument, int root, int 
			workingDecimalPlaceNumber)
			{

			/* returns uncorrected root of a BigDecimal - uses the Newton Raphson 
			method.
			* argument must be positive
			*/

			BigDecimal result;
			BigDecimal xn;
			BigDecimal oldxn;
			BigDecimal xnPlusOne;
			BigDecimal numerator;
			BigDecimal denominator;
			BigDecimal quotient;
			BigDecimal constant;

			int index;
			int runIndex;
			int iterationNumber = 200;
			constant = new BigDecimal(root);

			boolean halt;

			if (argument.compareTo(BigDecimal.ZERO) != 0) {
			xn = argument;
			oldxn = xn;
			halt = false;
			runIndex = 1;
			while ((halt == false) & (runIndex <= iterationNumber)) {
			oldxn = xn;
			numerator = xn;
			denominator = numerator;

			numerator = numerator.pow(root);
			denominator = denominator.pow(root - 1);

			denominator = (constant.multiply(denominator));
			numerator = (numerator.subtract(argument));

			if (denominator.compareTo(BigDecimal.ZERO) == 0) {
			halt = true;
			}
			else {
			quotient = (numerator.divide(denominator, 
			workingDecimalPlaceNumber, BigDecimal.ROUND_HALF_UP));

			xnPlusOne = (xn.subtract(quotient));
			xnPlusOne = xnPlusOne.setScale(workingDecimalPlaceNumber, 
			BigDecimal.ROUND_HALF_UP);

			xn = xnPlusOne;

			if (xnPlusOne.compareTo(oldxn) == 0) {
			halt = true;
			}
			}

			runIndex++;
			}
			result = xn;
			}
			else {
			result = BigDecimal.ZERO;
			}



			return(result);
			}

}
