package org.styskin.greed;

import static org.styskin.greed.MatrixUtils.correlation;
import static org.styskin.greed.MatrixUtils.tr;

import org.styskin.util.LoadInput;

public class Educate {
	
	
	public static void main(String[] args) throws Exception {
		LoadInput input = LoadInput.loadInput("cfg/auto/cars.txt");
		float[][] t = tr(input.M);
		Formula fml = new Formula();		
		for(int i=0; i < input.size; i++) {
			
			System.out.printf("%s:\t%f\n", input.getName(i), correlation(t[i], input.B));
		}
	}

}
