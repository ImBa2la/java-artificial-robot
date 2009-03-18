package org.styskin.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.styskin.ca.model.Pair;

public class LoadInputSimple extends LoadInput {
	
	public void init(String head) {
		StringTokenizer st = new StringTokenizer(head, "\t");
		boolean first = true;
		while(st.hasMoreTokens()) {
			String s = prepareToken(st.nextToken());
			if(first) {
				first = false;
				names.add("ONE");
			} else {
				names.add(s);
			}
		}
		size = names.size();
	}
	
	@SuppressWarnings("unchecked")
	public static Pair<LoadInput, LoadInput> loadInput(String file, double ratio) throws Exception {
		BufferedReader in = new BufferedReader(new FileReader(file));
		String line = in.readLine();
		int TWO = 2;
		
		LoadInputSimple[] input = new LoadInputSimple[TWO];
		List<Double>[] B = new List[TWO];
		List<double[]>[] M = new List[TWO];
		for(int i=0; i < TWO; i++) {
			input[i] = new LoadInputSimple();
			input[i].init(line);
			B[i] = new ArrayList<Double>();
			M[i] = new ArrayList<double[]>();
		}
		
		while((line = in.readLine()) != null) {
			Pair<Double, double[]> pair = input[0].parseLine(line);
			int ind = Math.random() > ratio ? 1 : 0;			
			B[ind].add(pair.getFirst());
			M[ind].add(pair.getSecond());
		}
		for(int j=0; j < TWO; j++) {
			input[j].B = new double[B[j].size()];
			input[j].M = new double[B[j].size()][];
			for(int i=0; i < B[j].size(); i++) {
				input[j].B[i] = B[j].get(i);
				input[j].M[i] = M[j].get(i);
			}
		}
		return new Pair<LoadInput, LoadInput>(input[0], input[1]);
	}
	
	Pair<Double, double[]> parseLine(String line) {
		double res = 0;
		double[] in = new double[size];
		int i=0;
		StringTokenizer st = new StringTokenizer(line, "\t");
		while(st.hasMoreTokens()) {
			double f = Double.parseDouble(prepareToken(st.nextToken()));
			if(i == 0) {
				res = f;
				in[0] = 1;
			} else {
				in[i] = f;
			}
			i ++;
		}
		return new Pair<Double, double[]>(res, in);
	}
}
