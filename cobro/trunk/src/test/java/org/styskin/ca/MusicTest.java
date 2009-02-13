package org.styskin.ca;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.SortedMap;
import java.util.StringTokenizer;
import java.util.TreeMap;

import junit.framework.TestCase;

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

public class MusicTest extends TestCase {
	
	private static final Logger logger = Logger.getLogger(MusicTest.class);	
	
	protected void setUp() throws Exception {
		BasicConfigurator.configure();
	}
	
	public void testMusic() throws Exception {
		Criteria music = CriteriaXMLParser.loadXML("cfg/music/music.res.xml");
		logger.info("Optimization started");
//		Optimize control = Optimize.getInput("cfg/music/music.tsv", music);
		Pair<Optimize, Optimize> ops = Optimize.getInput("cfg/music/html.base", music, 0.85);
		Optimize.saveInput("input.txt", music, ops.getFirst().getF(), ops.getFirst().getBase());
		
		Optimizer optimizer = new MultiOptimizer(music);
		Criteria musicRes = optimizer.optimize(ops.getFirst().getBase(), ops.getFirst().getF());
		CriteriaXMLParser.saveXML(musicRes, "music.res.xml");
		
//		CacheCriteria musicControl = new CacheCriteria(musicRes, control.getBase(), control.getF());
		CacheCriteria musicValidation = new CacheCriteria(musicRes, ops.getFirst().getBase(), ops.getFirst().getF());
		CacheCriteria musicCrossValidation = new CacheCriteria(musicRes, ops.getSecond().getBase(), ops.getSecond().getF());
		logger.info("Validation: " + musicValidation.check() + ", " + musicValidation.checkAbs());
//		musicControl.checkOut2("music.check");
		logger.info("CrossValidation: " + musicCrossValidation.check() + ", " + musicCrossValidation.checkAbs());
	}
	
	public void atestRanking() throws Exception {
		SortedMap<String, Double> fixed = new TreeMap<String, Double>();
		{
			BufferedReader in = new BufferedReader(new FileReader("cfg/music/fixed.r.out"));
			String line;
			while ((line = in.readLine()) != null) {
				StringTokenizer st = new StringTokenizer(line);
				fixed.put(st.nextToken(), CriteriaXMLParser.FORMAT.parse(st.nextToken()).doubleValue());
			}
			in.close();
		}
		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("r.out")));		
		IntegralCriteria music = CriteriaXMLParser.loadXML("music.res.xml");
		OptimizeInputFormat optimizeInputFormat = new OptimizeInputFormat(music);
		BufferedReader in = new BufferedReader(new FileReader("cfg/music/music.in"));
		String line = null;
		optimizeInputFormat.init(in.readLine());
		outer: while( (line = in.readLine()) != null) {
			Pair<Double, double[]> pair = optimizeInputFormat.parseLine(line);
			String url = line.substring(0, line.indexOf('\t'));
			while(fixed.size() > 0 && fixed.firstKey().compareTo(url) <= 0) {
				out.printf("%s\t%s\n", fixed.firstKey(), CriteriaXMLParser.FORMAT.format(fixed.get(fixed.firstKey())));
				if(fixed.firstKey().equals(url)) {
					fixed.remove(fixed.firstKey());
					continue outer;					
				}
				fixed.remove(fixed.firstKey());
			}
			out.printf("%s\t%s\n", url, CriteriaXMLParser.FORMAT.format(music.getValue(pair.getSecond())));
		}
		in.close();
		out.close();
	}

}
