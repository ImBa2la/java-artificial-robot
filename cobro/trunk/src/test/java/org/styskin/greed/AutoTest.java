package org.styskin.greed;

import junit.framework.TestCase;

import org.styskin.ca.model.Pair;
import org.styskin.util.LoadInput;
import org.styskin.util.LoadInputSimple;

public class AutoTest extends TestCase {
	
	public void testAuto() throws Exception {
//		Pair<LoadInput, LoadInput> inputs = LoadInputSimple.loadInput("cfg/auto/cars.txt", 0.75);
		Pair<LoadInput, LoadInput> inputs = LoadInputSimple.loadInput("cfg/imat/features.txt", 1);
		LoadInput input = inputs.getFirst();
		LoadInput check = inputs.getSecond();
		
		Formula fml = new Formula();
//		while(fml.iteration(input.B, input.M));
		Optimizer optimizer = new OptimizeCorrelation();
		for(int i=0; i < 30; i++) {
			fml.iteration(input.B, input.M);
			System.out.printf("LS:\t%f\t%f\n", fml.check(input), fml.pfound(input));
			System.out.printf("Check LS:\t%f\t%f\n", fml.check(check), fml.pfound(check));
//			for(int j=0; j < fml.weight.size(); j++)
//				fml.weight.set(j, 1);
//			System.out.printf("Clean LS:\t%f\t%f\n", fml.check(input), fml.pfound(input));
//			System.out.printf("Clean Check LS:\t%f\t%f\n", fml.check(check), fml.pfound(check));
//			optimizer.optimize(fml, input);
//			System.out.printf("After LS:\t%f\t%f\n", fml.check(input), fml.pfound(input));
//			System.out.printf("After Check LS:\t%f\t%f\n", fml.check(check), fml.pfound(check));
//			System.out.printf("%s\n", fml.toString(input));
		}
	}

}