package org.styskin.util;

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
	

	
	public String getName(int index) {
		return names.get(index);		
	}	

}
