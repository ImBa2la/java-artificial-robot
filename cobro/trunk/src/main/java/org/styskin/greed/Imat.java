package org.styskin.greed;

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
		for(int i=0; i < 20; i++) {
			fml.iteration(input.B, input.M);
			System.out.printf("===Iteration #%d\n", i);
			System.out.printf("LS:\t%f\t%f\n", fml.check(input), fml.dcg(input));
			System.out.printf("Check LS:\t%f\t%f\n", fml.check(check), fml.dcg(check));
//			System.out.printf("%s\n", fml.toString(input));
		}
		return fml;
	}
	
	
	public static void main(String[] args) throws Exception {
		(new Imat()).optimize();
	}

}
