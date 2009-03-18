package org.styskin.greed;

import junit.framework.TestCase;

import org.styskin.ca.model.Pair;
import org.styskin.util.LoadInput;
import org.styskin.util.LoadInputSimple;

public class AutoTest extends TestCase {
	
	public void testAuto() throws Exception {
//		Pair<LoadInput, LoadInput> inputs = LoadInputSimple.loadInput("cfg/auto/cars.txt", 0.75);
		Pair<LoadInput, LoadInput> inputs = LoadInputSimple.loadInput("cfg/imat/features.single.etalon", 0.75);
		LoadInput input = inputs.getFirst();
		LoadInput check = inputs.getSecond();
		
		Formula fml = new Formula();
//		while(fml.iteration(input.B, input.M));
		for(int i=0; i < 6; i++) {
			fml.iteration(input.B, input.M);
			System.out.printf("LS:\t%f\t%f\n", fml.check(input), fml.dcg(input));
			System.out.printf("Check LS:\t%f\t%f\n", fml.check(check), fml.dcg(check));
			System.out.printf("%s\n", fml.toString(input));
		}
	}

}