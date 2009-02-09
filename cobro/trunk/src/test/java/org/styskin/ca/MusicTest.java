package org.styskin.ca;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.styskin.ca.functions.CacheCriteria;
import org.styskin.ca.functions.Criteria;
import org.styskin.ca.functions.MultiOptimizer;
import org.styskin.ca.functions.Optimizer;
import org.styskin.ca.model.CriteriaXMLParser;
import org.styskin.ca.model.Pair;
import org.styskin.ca.model.CriteriaXMLParser.Optimize;

import junit.framework.TestCase;

public class MusicTest extends TestCase {
	
	private static final Logger logger = Logger.getLogger(MusicTest.class);	
	
	private Criteria music;
	
	protected void setUp() throws Exception {
		BasicConfigurator.configure();
		music = CriteriaXMLParser.loadXML("cfg/music/music.s.xml");
	}
	
	public void testMusic() throws Exception {
		logger.info("Optimization started");		
		Pair<Optimize, Optimize> ops = Optimize.getInput("cfg/music/music.tsv", music, 0.85);
//		Optimize.saveInput("input.txt", music, ops.getFirst().getF(), ops.getFirst().getBase());
		
		Optimizer optimizer = new MultiOptimizer(music);
		Criteria musicRes = optimizer.optimize(ops.getFirst().getBase(), ops.getFirst().getF());
		CriteriaXMLParser.saveXML(musicRes, "music.s.res.xml");
		
		CacheCriteria musicValidation = new CacheCriteria(musicRes, ops.getFirst().getBase(), ops.getFirst().getF());
		CacheCriteria musicCrossValidation = new CacheCriteria(musicRes, ops.getSecond().getBase(), ops.getSecond().getF());
		logger.info("Validation: " + musicValidation.check());
		logger.info("CrossValidation: " + musicCrossValidation.check());
	}

}
