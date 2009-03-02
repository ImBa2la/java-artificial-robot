package org.styskin.ca;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;

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
	
	public void atestTextRelevance() throws Exception {
		Criteria tr = CriteriaXMLParser.loadXML("cfg/tr/tr.xml");
		logger.info("Optimization started");
		Pair<Optimize, Optimize> ops = Optimize.getInput("cfg/tr/train.in", tr, 0.85);
		Optimize.saveInput("input.txt", tr, ops.getFirst().getF(), ops.getFirst().getBase());
		
//		Optimizer optimizer = new MultiOptimizer(tr);
		Optimizer optimizer = new SingleOptimizer(tr);
		Criteria trRes = optimizer.optimize(ops.getFirst().getBase(), ops.getFirst().getF());
		CriteriaXMLParser.saveXML(trRes, "tr.res.xml");
		
//		CacheCriteria trControl = new CacheCriteria(trRes, control.getBase(), control.getF());
		CacheCriteria trValidation = new CacheCriteria(trRes, ops.getFirst().getBase(), ops.getFirst().getF());
		CacheCriteria trCrossValidation = new CacheCriteria(trRes, ops.getSecond().getBase(), ops.getSecond().getF());
		logger.info("Validation: " + trValidation.check() + ", " + trValidation.checkAbs());
//		trControl.checkOut2("tr.check");
		logger.info("CrossValidation: " + trCrossValidation.check() + ", " + trCrossValidation.checkAbs());
	}
	
	public void testRanking() throws Exception {
		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("r.tmp")));		
		IntegralCriteria music = CriteriaXMLParser.loadXML("tr.res.xml");
		OptimizeInputFormat optimizeInputFormat = new OptimizeInputFormat(music);
		BufferedReader in = new BufferedReader(new FileReader("cfg/tr/train.in"));
		String line = null;
		optimizeInputFormat.init(in.readLine());
		while( (line = in.readLine()) != null) {
			Pair<Double, double[]> pair = optimizeInputFormat.parseLine(line);
			String url = line.substring(0, line.indexOf('\t'));
			out.printf("%s\t%s\n", url, CriteriaXMLParser.FORMAT.format(music.getValue(pair.getSecond())));
		}
		in.close();
		out.close();
	}

}
