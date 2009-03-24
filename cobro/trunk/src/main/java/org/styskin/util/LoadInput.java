package org.styskin.util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class LoadInput {
	
	protected List<String> names = new ArrayList<String>();
	public int size;
	
	public double[][] M;
	public double[] B;
	public int[] Q;
	
	protected static String prepareToken(String s) {
		return s.startsWith("\"")? s.substring(1, s.length()-1) : s; 
	}
	
	public void dumpInput(String file) throws Exception {
		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(file)));
		out.print("score");
		for(int i=1; i < names.size(); i++)
			out.append('\t').append(names.get(i));
		out.println();
		
		for(int i=0; i < B.length; i++) {
			out.print(B[i]);
			for(int j=1; j < M[i].length; j++)
				out.append('\t').append(Double.toString(M[i][j]));
			out.println();						
		}		
		out.close();
	}
	
	public String getName(int index) {
		return names.get(index);		
	}	

}
