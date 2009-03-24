package org.styskin.greed;

import static org.styskin.greed.MatrixUtils.leastSqares;

import org.styskin.util.LoadInput;
import org.styskin.util.LoadInputSimple;

public class Educate {
	
	public void optimize(String path) throws Exception {
		LoadInput input = LoadInputSimple.loadInput("cfg/music/test_50/input.in", 1).getFirst();
		LoadInput check = LoadInputSimple.loadInput("cfg/music/test_50/input.test", 1).getFirst();
		
		Formula fml = new Formula();
//		while(fml.iteration(input.B, input.M));
		for(int i=0; i < 6; i++) {
			fml.iteration(input);
			System.out.printf("LS:\t%f\n", leastSqares(fml.result(input.M), input.B));
			System.out.printf("Check LS:\t%f\n", leastSqares(fml.result(check.M), check.B));
			System.out.printf("%s\n", fml.toString(input));
		}
	}
	
	
	public static void main(String[] args) throws Exception {
//		(new Educate()).optimize("cfg/auto/cars.txt");
		(new Educate()).optimize("cfg/music/input.txt");
	}

}
