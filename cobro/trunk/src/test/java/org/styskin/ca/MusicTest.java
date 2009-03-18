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
import org.styskin.ca.functions.MultiOptimizer;
import org.styskin.ca.functions.Optimizer;
import org.styskin.ca.functions.SingleOptimizer;
import org.styskin.ca.model.CriteriaXMLParser;
import org.styskin.ca.model.Pair;
import org.styskin.ca.model.CriteriaXMLParser.Optimize;
import org.styskin.ca.model.CriteriaXMLParser.OptimizeInputFormat;

public class MusicTest extends TestCase {
	
	private static final Logger logger = Logger.getLogger(MusicTest.class);	
	
	protected void setUp() throws Exception {
		BasicConfigurator.configure();
	}
	
	public void testMusic() throws Exception {
		Criteria music = CriteriaXMLParser.loadXML("cfg/music/music.res.xml");
		logger.info("Optimization started");
//		Optimize control = Optimize.getInput("cfg/music/music.tsv", music);
		Pair<Optimize, Optimize> ops = Optimize.getInput("cfg/music/html.base", music, 0.50);
		Optimize.saveInput("input.in", music, ops.getFirst().getF(), ops.getFirst().getBase());		
		Optimize.saveInput("input.test", music, ops.getSecond().getF(), ops.getSecond().getBase());		
//		Optimizer optimizer = new MultiOptimizer(music);
		Optimizer optimizer = new SingleOptimizer(music);
		Criteria musicRes = optimizer.optimize(ops.getFirst());
		CriteriaXMLParser.saveXML(musicRes, "music.res.xml");
		
//		CacheCriteria musicControl = new CacheCriteria(musicRes, control.getBase(), control.getF());
		CacheCriteria musicValidation = new CacheCriteria(musicRes, ops.getFirst());
		CacheCriteria musicCrossValidation = new CacheCriteria(musicRes, ops.getSecond());
		logger.info("Validation: " + musicValidation.check() + ", " + musicValidation.checkAbs());
//		musicControl.checkOut2("music.check");
		logger.info("CrossValidation: " + musicCrossValidation.check() + ", " + musicCrossValidation.checkAbs());
	}
	
	public void atestRanking() throws Exception {
		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("r.tmp")));		
		IntegralCriteria music = CriteriaXMLParser.loadXML("cfg/music/music.res.xml");
		OptimizeInputFormat optimizeInputFormat = new OptimizeInputFormat(music);
//		BufferedReader in = new BufferedReader(new FileReader("cfg/music/html.base"));
		BufferedReader in = new BufferedReader(new FileReader("cfg/music/music.in"));
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
