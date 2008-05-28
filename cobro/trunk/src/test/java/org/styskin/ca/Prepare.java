package org.styskin.ca;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class Prepare {

	
	public static void main(String[] args) throws Exception {
		BufferedReader in = new BufferedReader(new FileReader("in.txt"));
		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("in2.txt")));		
		String s;
		List<List<Double>> M = new ArrayList<List<Double>>();
		while((s = in.readLine()) != null) {
			List<Double> l = new ArrayList<Double>();
			StringTokenizer st = new StringTokenizer(s);
			while(st.hasMoreTokens())
				l.add(Double.valueOf(st.nextToken().replace(',', '.')));			
			M.add(l);
		}
		for(int j=0; j < M.get(0).size(); j ++) {
			double max = M.get(0).get(j), avg = 0;			
			for(int i=0; i < M.size(); i ++) {
				max = Math.max(M.get(i).get(j), max);
				avg += M.get(i).get(j);
			}
			avg /= M.size();
			double B = Math.log(9.0/5)/Math.log(max/avg);
			double A = 0.9/Math.pow(max, B);
			for(int i=0; i < M.size(); i ++) {
				M.get(i).set(j, A * Math.pow(M.get(i).get(j), B));
			}
		}
		for(int i=0; i < M.size(); i ++) {
			for(int j=0; j < M.get(i).size(); j ++) {
				out.printf("%f\t",M.get(i).get(j));				
			}
			out.println();
		}
		
		in.close();
		out.close();		
	}
}
