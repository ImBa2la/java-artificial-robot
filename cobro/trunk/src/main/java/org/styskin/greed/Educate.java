package org.styskin.greed;

import org.styskin.ca.model.Pair;
import org.styskin.util.LoadInput;
import static org.styskin.greed.MatrixUtils.*;

public class Educate {
	
	public void optimize(String path) throws Exception {
		Pair<LoadInput, LoadInput> inputs = LoadInput.loadInput(path, 0.75);
		LoadInput input = inputs.getFirst();
		LoadInput check = inputs.getSecond();
		
		Formula fml = new Formula();
//		while(fml.iteration(input.B, input.M));
		for(int i=0; i < 6; i++) {
			fml.iteration(input.B, input.M);
			System.out.printf("LS:\t%f\n", leastSqares(fml.result(input.M), input.B));
			System.out.printf("Check LS:\t%f\n", leastSqares(fml.result(check.M), check.B));
			System.out.printf("%s\n", fml);
		}
	}
	
	
	public static void main(String[] args) throws Exception {
		(new Educate()).optimize("cfg/music/input.txt");
	}

}
