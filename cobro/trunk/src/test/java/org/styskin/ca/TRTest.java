package org.styskin.ca;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.styskin.ca.functions.CacheCriteria;
import org.styskin.ca.functions.Criteria;
import org.styskin.ca.functions.IntegralCriteria;
import org.styskin.ca.functions.Optimizer;
import org.styskin.ca.functions.SingleOptimizer;
import org.styskin.ca.model.CriteriaXMLParser;
import org.styskin.ca.model.Pair;
import org.styskin.ca.model.CriteriaXMLParser.Optimize;
import org.styskin.ca.model.CriteriaXMLParser.OptimizeInputFormat;

public class TRTest extends TestCase {
	
	private static final Logger logger = Logger.getLogger(TRTest.class);	
	
	protected void setUp() throws Exception {
		BasicConfigurator.configure();
	}
	
	public void testTextRelevance() throws Exception {
		Criteria tr = CriteriaXMLParser.loadXML("cfg/tr/tr.xml");
		logger.info("Optimization started");
		Pair<Optimize, Optimize> ops = Optimize.getInput("cfg/tr/train.in", tr, 0.2);
		Optimize.saveInput("input.txt", tr, ops.getFirst().getF(), ops.getFirst().getBase());
		
//		Optimizer optimizer = new MultiOptimizer(tr);
		Optimizer optimizer = new SingleOptimizer(tr);
		Criteria trRes = optimizer.optimize(ops.getFirst().getBase(), ops.getFirst().getF());
		CriteriaXMLParser.saveXML(trRes, "tr.res.xml");
		
//		CacheCriteria trControl = new CacheCriteria(trRes, control.getBase(), control.getF());
		CacheCriteria trValidation = new CacheCriteria(trRes, ops.getFirst().getBase(), ops.getFirst().getF());
		CacheCriteria trCrossValidation = new CacheCriteria(trRes, ops.getSecond().getBase(), ops.getSecond().getF());
		logger.info("Validation: " + trValidation.check() + ", " + trValidation.checkAbs() + ", " + trValidation.checkCorrelation());
//		trControl.checkOut2("tr.check");
		logger.info("CrossValidation: " + trCrossValidation.check() + ", " + trCrossValidation.checkAbs() + ", " + trCrossValidation.checkCorrelation());
	}
	
	public void testRanking() throws Exception {
		Map<String, Integer> req = new HashMap<String, Integer>();
		{
			String s;
			int i=0;
			BufferedReader in = new BufferedReader(new FileReader("cfg/tr/requests.txt"));
			while( (s = in.readLine()) != null) {
				req.put(s.trim(), i);
				++ i;
			}
			in.close();
		}
		
		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("ranking_cor2.txt")));		
		IntegralCriteria music = CriteriaXMLParser.loadXML("cfg/tr/tr.xml");
		OptimizeInputFormat optimizeInputFormat = new OptimizeInputFormat(music);
		BufferedReader in = new BufferedReader(new FileReader("cfg/tr/train.in"));
		String line = null;
		optimizeInputFormat.init(in.readLine());
		String pq = "";
		List<Pair<String, Double>> list = new ArrayList<Pair<String,Double>>();
		while( (line = in.readLine()) != null) {
			Pair<Double, double[]> pair = optimizeInputFormat.parseLine(line);
			double v = music.getValue(pair.getSecond());
			String url = line.substring(0, line.indexOf('\t'));
			String q = url.substring(0, url.lastIndexOf(' '));
			String u = url.substring(url.lastIndexOf(' ') + 1);
			if(!q.equals(pq)) {
				Collections.sort(list, new Comparator<Pair<String, Double>>() {
					public int compare(Pair<String, Double> o1, Pair<String, Double> o2) {
						return -o1.getSecond().compareTo(o2.getSecond());
					}
				});
				for(int i=0; i < Math.min(10, list.size()); ++i)
					out.printf("%s\t%d\t%s\t%f\n", pq, i, list.get(i).getFirst(), list.get(i).getSecond());
				list = new ArrayList<Pair<String,Double>>();
				pq = q;
			}
			list.add(new Pair<String, Double>(u, v));
//			System.out.printf("%s\t%s\n", url, CriteriaXMLParser.FORMAT.format(v));
		}
		Collections.sort(list, new Comparator<Pair<String, Double>>() {
			public int compare(Pair<String, Double> o1, Pair<String, Double> o2) {
				return -o1.getSecond().compareTo(o2.getSecond());
			}
		});
		for(int i=0; i < list.size(); ++i)
			out.printf("%s\t%d\t%s\t%f\n", pq, i, list.get(i).getFirst(), list.get(i).getSecond());

		in.close();
		out.close();
	}

}
