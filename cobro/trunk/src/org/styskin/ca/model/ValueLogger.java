/*
 *$Id: Optimizer.java 62 2007-05-09 16:19:07Z gutalin $
 */
package org.styskin.ca.model;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.log4j.Logger;

public abstract class ValueLogger {
	
	private static final Logger logger = Logger.getLogger(ValueLogger.class);
	
	private static final long START = System.currentTimeMillis();
	
	private static class Entry {
		private long time = System.currentTimeMillis() - START;
		private double value;
		
		public Entry(double value) {
			this.value = value;			
		}		
	}
	
	private static final Map<Integer, List<Entry>> log = new TreeMap<Integer, List<Entry>>(); 
	
	private ValueLogger() {}
	
	public static class ValueLoggerInner extends ValueLogger {
		
		private Integer index;
		
		private ValueLoggerInner(Integer index) {
			this.index = index;
			log.put(this.index, new ArrayList<Entry>());
		}
		
		public void log(double value) {
			log.get(index).add(new Entry(value));
		}		
	}
	
	public abstract void log(double value);
	
	private static Integer index = 0;
	
	public static synchronized ValueLogger getValueLogger() {
		return new ValueLoggerInner(index ++);
	}
	
	public static void output(Writer writer) throws IOException {
		PrintWriter out = new PrintWriter(new BufferedWriter(writer));
		Set<Integer> keys = log.keySet();
		for(int i=0; i < 100; i++) {
			for(Integer index : keys) {
				if(i < log.get(index).size()) {
					out.printf("%d\t%f\t", log.get(index).get(i).time, log.get(index).get(i).value);
				} else {
					out.printf("\t\t");
				}
			}
			out.println();
		}		
		out.flush();
	}
		
}
