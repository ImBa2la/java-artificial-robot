package org.styskin.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.styskin.ca.model.Pair;

import ru.yandex.utils.Triple;

public class LoadInputImat extends LoadInput {
	
	private void init() {
		size = 246;
		for(int i=0; i < size; i++)
			names.add(Integer.toString(i));
	}
	
	
	
	@SuppressWarnings("unchecked")
	public static Pair<LoadInput, LoadInput> loadInput(String file, double ratio) throws Exception {
		
		BufferedReader in = new BufferedReader(new FileReader(file));
		String line;
		int TWO = 2;
		
		LoadInputImat[] input = new LoadInputImat[TWO];
		List<Integer>[] Q = new List[TWO];
		List<Double>[] B = new List[TWO];
		List<double[]>[] M = new List[TWO];
		for(int i=0; i < TWO; i++) {
			input[i] = new LoadInputImat();
			input[i].init();
			B[i] = new ArrayList<Double>();
			M[i] = new ArrayList<double[]>();
			Q[i] = new ArrayList<Integer>();
		}
		
		while((line = in.readLine()) != null) {
			Triple<Double, Integer, double[]> pair = input[0].parseLine(line);
			int ind = Math.random() > ratio ? 1 : 0;			
			B[ind].add(pair.getFirst());
			Q[ind].add(pair.getSecond());
			M[ind].add(pair.getThird());
		}
		for(int j=0; j < TWO; j++) {
			input[j].Q = new int[B[j].size()];
			input[j].B = new double[B[j].size()];
			input[j].M = new double[B[j].size()][];
			for(int i=0; i < B[j].size(); i++) {
				input[j].Q[i] = Q[j].get(i);
				input[j].B[i] = B[j].get(i);
				input[j].M[i] = M[j].get(i);
			}
		}
		return new Pair<LoadInput, LoadInput>(input[0], input[1]);
	}
	
	Triple<Double, Integer, double[]> parseLine(String line) {
		double res = 0;
		int q = 0;
		double[] in = new double[size];
		StringTokenizer st = new StringTokenizer(line, " ");
		if(st.hasMoreTokens()) {
			res = Double.parseDouble(prepareToken(st.nextToken()));
			in[0] = 1;
		}		
		while(st.hasMoreTokens()) {
			String s = st.nextToken();
			if(s.indexOf(':') < 0)
				break;
			int ind = Integer.valueOf(s.substring(0, s.indexOf(':')));
			double f = Double.valueOf(s.substring(s.indexOf(':')+1));
			in[ind] = f;
		}
		if(st.hasMoreTokens())
			q = Integer.parseInt(st.nextToken());						
		return new Triple<Double, Integer, double[]>(res, q, in);
	}
}
