package org.styskin.greed;

import static org.styskin.greed.MatrixUtils.correlation;
import static org.styskin.greed.MatrixUtils.tr;

import java.util.ArrayList;
import java.util.List;

import org.styskin.greed.Formula.Monom;
import org.styskin.util.LoadInput;

public class Educate {
	
	
	public static void main(String[] args) throws Exception {
		LoadInput input = LoadInput.loadInput("cfg/auto/cars.txt");
		double[][] t = tr(input.M);
		Formula fml = new Formula();		
		for(int i=0; i < input.size; i++) {			
			System.out.printf("%s:\t%f\n", input.getName(i), correlation(t[i], input.B));
		}
		List<Monom> monoms = new ArrayList<Monom>();
		monoms.add(new Monom(2));
		monoms.add(new Monom(1));
		monoms.add(new Monom(0));
		monoms.add(new Monom(0, 1));
		monoms.add(new Monom(0, 2));
		monoms.add(new Monom(1, 2));
		monoms.add(new Monom(0, 1, 2));
		fml.addMonoms(monoms, input.B, input.M);
		System.out.printf("Optimisation: %s\t%f\n", fml, correlation(fml.result(input.M), input.B));
		
	}

}
