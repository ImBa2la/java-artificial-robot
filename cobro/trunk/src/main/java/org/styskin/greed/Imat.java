package org.styskin.greed;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;

import org.apache.log4j.helpers.FileWatchdog;
import org.styskin.ca.model.Pair;
import org.styskin.util.LoadInput;
import org.styskin.util.LoadInputImat;

public class Imat {
	
	public Formula optimize() throws Exception {
		Pair<LoadInput, LoadInput> pair = LoadInputImat.loadInput("cfg/imat/imat2009_learning.txt", 1);
		LoadInput input = pair.getFirst();
		LoadInput check = pair.getSecond();
		
		Formula fml = new Formula();
//		while(fml.iteration(input.B, input.M));
		for(int i=0; i < 12; i++) {
			fml.iteration(input.B, input.M);
			System.out.printf("===Iteration #%d\n", i);
			System.out.printf("LS:\t%f\n", fml.check(input));
			if(i%5 == 0)
				System.out.printf("DCG: %f\n", fml.dcg(input));
//			System.out.printf("Check LS:\t%f\t%f\n", fml.check(check), fml.dcg(check));
//			System.out.printf("%s\n", fml.toString(input));
		}
		System.out.printf("LS:\t%f\t%f\n", fml.check(input), fml.dcg(input));
		Optimizer optimizer = new OptimizeDcg();
		optimizer.optimize(fml, input);
		System.out.printf("LS:\t%f\t%f\n", fml.check(input), fml.dcg(input));
		return fml;
	}
	
	
	public static void main(String[] args) throws Exception {
		Formula fml = (new Imat()).optimize();
		Pair<LoadInput, LoadInput> pair = LoadInputImat.loadInput("cfg/imat/imat2009_test.txt", 1);
		double[] r = fml.result(pair.getFirst().M);
		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("cfg/imat/result2.txt")));
		for(int i=0; i < r.length; i++)
			out.println(r[i]);
		out.close();
	}

}
