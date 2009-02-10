package org.styskin.ca;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.styskin.ca.functions.CacheCriteria;
import org.styskin.ca.functions.Criteria;
import org.styskin.ca.functions.IntegralCriteria;
import org.styskin.ca.functions.MultiOptimizer;
import org.styskin.ca.functions.Optimizer;
import org.styskin.ca.model.CriteriaXMLParser;
import org.styskin.ca.model.Pair;
import org.styskin.ca.model.CriteriaXMLParser.Optimize;
import org.styskin.ca.model.CriteriaXMLParser.OptimizeInputFormat;

import junit.framework.TestCase;

public class MusicTest extends TestCase {
	
	private static final Logger logger = Logger.getLogger(MusicTest.class);	
	
	protected void setUp() throws Exception {
		BasicConfigurator.configure();
	}
	
	public void atestMusic() throws Exception {
		Criteria music = CriteriaXMLParser.loadXML("cfg/music/music.xml");
		logger.info("Optimization started");
		Optimize control = Optimize.getInput("cfg/music/music.tsv", music);
		Pair<Optimize, Optimize> ops = Optimize.getInput("cfg/music/music.tsv", music, 0.9);
		Optimize.saveInput("input.txt", music, ops.getFirst().getF(), ops.getFirst().getBase());
		
		Optimizer optimizer = new MultiOptimizer(music);
		Criteria musicRes = optimizer.optimize(ops.getFirst().getBase(), ops.getFirst().getF());
		CriteriaXMLParser.saveXML(musicRes, "music.res.xml");
		
		CacheCriteria musicControl = new CacheCriteria(musicRes, control.getBase(), control.getF());
		CacheCriteria musicValidation = new CacheCriteria(musicRes, ops.getFirst().getBase(), ops.getFirst().getF());
		CacheCriteria musicCrossValidation = new CacheCriteria(musicRes, ops.getSecond().getBase(), ops.getSecond().getF());
		logger.info("Validation: " + musicValidation.check());
		musicControl.checkOut2("music.check");
		logger.info("CrossValidation: " + musicCrossValidation.check());
	}
	
	public void testRanking() throws Exception {
		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("music.out")));		
		IntegralCriteria music = CriteriaXMLParser.loadXML("cfg/music/music.res.xml");
		OptimizeInputFormat optimizeInputFormat = new OptimizeInputFormat(music);
		BufferedReader in = new BufferedReader(new FileReader("cfg/music/music.in"));
		String line = null;
		optimizeInputFormat.init(in.readLine());
		while( (line = in.readLine()) != null) {
			Pair<Double, double[]> pair = optimizeInputFormat.parseLine(line);			
			out.printf("%s\t%4.4f\n", line.substring(0, line.indexOf('\t')), music.getValue(pair.getSecond()));
		}
		in.close();
		out.close();
	}

}
