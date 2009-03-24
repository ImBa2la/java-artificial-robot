package org.styskin.greed;

import static java.lang.Math.log;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.styskin.ca.model.Pair;
import org.styskin.util.LoadInput;
import org.styskin.util.LoadInputImat;

public class CheckImat {
	
	static double check(double[] r, double[] b, int[] q) {
		List<UrlQueryScore> list = new ArrayList<UrlQueryScore>();
		for(int i=0; i < r.length; i++)
			list.add(new UrlQueryScore(q[i], r[i], b[i]));
		Collections.sort(list, new UrlQueryScoreComparator());
		double dcg = 0;
		int N = -1;
		int cq = -1, j = 0;
		for(int i=0; i < r.length; i++) {
			UrlQueryScore t = list.get(i);
			if(t.q != cq) {
				j = 1;
				cq = t.q;
				++ N;
			}
			dcg += t.real/(log(j)/log(2) + 1);
			j++;
		}		
		return dcg/N;		
	}

	
	public static void main(String[] args) throws Exception {
		Pair<LoadInput, LoadInput> pair = LoadInputImat.loadInput("cfg/imat/imat2009_learning.txt", 1);
		LoadInput input = pair.getFirst();
		double[] R = new double[input.B.length];
		{
			int i=0;
			BufferedReader in = new BufferedReader(new FileReader("answer.txt"));
			String s;
			while((s = in.readLine()) != null) {
				R[i] = Double.valueOf(s.trim());				
				i++;
			}
			in.close();
		}
		System.out.println(check(R, input.B, input.Q));						
	}

}
