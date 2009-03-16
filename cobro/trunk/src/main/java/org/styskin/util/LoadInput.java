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
	
	public float[][] M;
	public float[] B;
	
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
		List<Float> B = new ArrayList<Float>();
		List<float[]> M = new ArrayList<float[]>();
		
		while((line = in.readLine()) != null) {
			Pair<Float, float[]> pair = input.parseLine(line);
			B.add(pair.getFirst());
			M.add(pair.getSecond());
		}
		input.B = new float[B.size()];
		input.M = new float[B.size()][];
		for(int i=0; i < B.size(); i++) {
			input.B[i] = B.get(i);
			input.M[i] = M.get(i);
		}
		return input;
	}
	
	Pair<Float, float[]> parseLine(String line) {
		float res = 0;
		float[] in = new float[size];
		int i=0;
		StringTokenizer st = new StringTokenizer(line, "\t");
		while(st.hasMoreTokens()) {
			float f = Float.parseFloat(prepareToken(st.nextToken()));
			if(i == 0)
				res = f; 
			else
				in[i-1] = f;
			i ++;
		}
		return new Pair<Float, float[]>(res, in);
	}
	
	public String getName(int index) {
		return names.get(index);		
	}	

}
