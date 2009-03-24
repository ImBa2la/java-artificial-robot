package org.styskin.greed;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;

import org.styskin.util.LoadInput;
import org.styskin.util.LoadInputImat;

public class Imat {
	
	public void testFml(Formula fml, String file) throws Exception {
		LoadInput test = LoadInputImat.loadInput("cfg/imat/imat2009_test.txt", 1).getFirst();
		double[] r = fml.result(test.M);
		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("cfg/imat/" + file)));
		for(int i=0; i < r.length; i++)
			out.println(r[i]);
		out.close();
	}
	
	public Formula optimize() throws Exception {
		LoadInput input = LoadInputImat.loadInput("cfg/imat/imat2009_learning.txt", 1).getFirst();
//		input.dumpInput("cfg/imat/imat2009_learning.csv");
		Formula fml = new Formula();
		for(int i=0; i < 20; i++) {
			fml.iteration(input);
			System.out.printf("%d:\t%f\t%f\t%d\n", i, fml.check(input), fml.dcg(input), fml.monoms.size());
//			System.out.printf("%s\n", fml.toString(input));
		}
		System.out.printf("Final:\t%f\t%f\n", fml.check(input), fml.dcg(input));
		testFml(fml, "result8_clean.txt");
		Optimizer optimizer = new OptimizeDcg();
		optimizer.optimize(fml, input);
		System.out.printf("Otimized:\t%f\t%f\n", fml.check(input), fml.dcg(input));
		testFml(fml, "result8_dcg.txt");
		return fml;
	}
	
	
	public static void main(String[] args) throws Exception {
		(new Imat()).optimize();
	}

}
