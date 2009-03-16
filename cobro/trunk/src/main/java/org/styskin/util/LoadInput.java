package org.styskin.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.styskin.ca.model.Pair;

public class LoadInput {
	
	private List<String> names = new ArrayList<String>();
	public int size;
	
	public double[][] M;
	public double[] B;
	
	private static String prepareToken(String s) {
		return s.startsWith("\"")? s.substring(1, s.length()-1) : s; 
	}
	
	public void init(String head) {
		StringTokenizer st = new StringTokenizer(head, "\t");
		boolean first = true;
		while(st.hasMoreTokens()) {
			String s = prepareToken(st.nextToken());
			if(first)
				first = false;
			else
				names.add(s);
		}
		size = names.size();
	}
	
	public static LoadInput loadInput(String file) throws Exception {
		BufferedReader in = new BufferedReader(new FileReader(file));
		String line = in.readLine();
		LoadInput input = new LoadInput();
		input.init(line);
		List<Double> B = new ArrayList<Double>();
		List<double[]> M = new ArrayList<double[]>();
		
		while((line = in.readLine()) != null) {
			Pair<Double, double[]> pair = input.parseLine(line);
			B.add(pair.getFirst());
			M.add(pair.getSecond());
		}
		input.B = new double[B.size()];
		input.M = new double[B.size()][];
		for(int i=0; i < B.size(); i++) {
			input.B[i] = B.get(i);
			input.M[i] = M.get(i);
		}
		return input;
	}
	
	Pair<Double, double[]> parseLine(String line) {
		double res = 0;
		double[] in = new double[size];
		int i=0;
		StringTokenizer st = new StringTokenizer(line, "\t");
		while(st.hasMoreTokens()) {
			double f = Double.parseDouble(prepareToken(st.nextToken()));
			if(i == 0)
				res = f; 
			else
				in[i-1] = f;
			i ++;
		}
		return new Pair<Double, double[]>(res, in);
	}
	
	public String getName(int index) {
		return names.get(index);		
	}	

}
