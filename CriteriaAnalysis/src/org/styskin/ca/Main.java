package org.styskin.ca;

import java.io.BufferedReader;
import java.io.FileReader;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.styskin.ca.functions.CacheCriteria;
import org.styskin.ca.functions.ComplexCriteria;
import org.styskin.ca.functions.Criteria;
import org.styskin.ca.functions.Optimizer;
import org.styskin.ca.model.CriteriaXMLParser;

public class Main {

	static {
		BasicConfigurator.configure();
	}

	static Logger logger = Logger.getLogger(Main.class);

	public void testCriteria() throws Exception {
		Criteria criteria0 = CriteriaXMLParser.loadXML("cfg/big0.xml");
		Criteria criteria1 = CriteriaXMLParser.loadXML("cfg/big1.xml");
//		Criteria criteria2 = CriteriaXMLParser.loadXML("cfg/test2.xml");

/*		double[][] F = Optimizer.getMatrix(criteria0.getTotalSize(), 300);

		PrintWriter outF = new PrintWriter("F.txt");
		for(int i = 0; i < F.length; i++) {
			for(int j = 0; j < F[i].length; j++) {
				outF.printf("%f ", F[i][j]);
			}
			outF.println();
		}
		outF.close();*/


/*		double[][] F = new double[300][criteria0.getTotalSize()];
		Scanner inF = new Scanner(new File("F.txt"));
		for(int i = 0; i < F.length; i++) {
			for(int j = 0; j < F[i].length; j++) {
				F[i][j] = inF.nextDouble();
			}
		}
		inF.close();*/


/*		logger.debug(cr1.check());
		logger.debug(criteria2);

		Optimizer optimizer = new Optimizer();
		optimizer.optimize(criteria2, criteria, F);

		cr.clearCache();
		logger.debug("" + cr.check() + criteria2);*/
		double[][] F = Optimizer.getMatrix(criteria0.getTotalSize(), 300);
		Optimizer optimizer = new Optimizer();
		optimizer.optimize(criteria1, criteria0, F);
		System.out.printf("%s\n%s",criteria1, criteria0);
		
/*		Optimizer optimizer = new Optimizer();
		System.setOut(new PrintStream(new FileOutputStream("out5_1.txt")));
		optimizer.optimize(criteria1, criteria0, F);
		System.out.close();
		System.setOut(new PrintStream(new FileOutputStream("out5_2.txt")));
		optimizer.optimize(criteria2, criteria0, F);
		System.out.close();*/
		
/*		List<Double> w = new ArrayList<Double>();
		w.add(0.3);
		w.add(0.7);
		
		ComplexHOperator op = new ExponentalHOperator(w);
		op.initialize(0.3, 0.4);
		
		System.out.printf("%4.4f\n", op.getValue(new double[] {0.6, 0.6}));*/
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
/*		Criteria criteria = CriteriaXMLParser.loadXML("cfg/criteria3.xml");
		double[][] F = {{1,1},{2,2},{3,3}};

		CacheCriteria cr = new CacheCriteria(criteria, criteria, F);
		for(double x : cr.getValue()) {
			logger.info(x);
		}*/
//		(new Main()).testCriteria();
		(new Main()).testFlats();		
	}
	
	static class Optimize {
		double[][] F;
		double[] base;
		
		private static NumberFormat FORMAT = NumberFormat.getNumberInstance();
		
		private Optimize() {
			
		}
		
		private static int getCriteriaSize(Criteria cr, Map<String, Integer> map, int begin) throws Exception {
			if(cr instanceof ComplexCriteria) {
				for(Criteria c : cr.getChildren()) {
					begin = getCriteriaSize(c, map, begin);
				}								
			} else {
				if(!map.containsKey(cr.getName())) {
					map.put(cr.getName(), begin);
				} else {
					throw new Exception("duplicate");
				}
				begin++;
			}
			return begin;			
		}
		
		private static Map<String, Integer> getCriteriaMap(Criteria cr) throws Exception {
			Map<String, Integer> map = new HashMap<String, Integer>();
			getCriteriaSize(cr, map, 0);
			return map;			
		}
		
		
		// TODO: Exception on data absence
		public static Optimize getInput(String file, Criteria cr) {
			Optimize o = new Optimize();
			try {
				Map<String, Integer> map = getCriteriaMap(cr);
				map.put("price", -1);
				List<Integer> in_map = new ArrayList<Integer>();
				
				List<List<Double>> input = new ArrayList<List<Double>>();				
				BufferedReader in = new BufferedReader(new FileReader(file));
				StringTokenizer st;
				
				st = new StringTokenizer(in.readLine());
				while(st.hasMoreTokens()) {
					String key = st.nextToken(); 
					if(map.containsKey(key)) {
						in_map.add(map.get(key));						
					}
				}				
				String s = null;
				while((s = in.readLine()) != null) {
					st = new StringTokenizer(s);
					List<Double> d = new ArrayList<Double>();
					input.add(d);
					while(st.hasMoreTokens()) {
						d.add(FORMAT.parse(st.nextToken()).doubleValue());
					}
					if(d.size() != in_map.size()) {
						throw new Exception("Incorrect parameters number");						
					}
				}
				o.F = new double[input.size()][map.size()-1];
				o.base = new double[input.size()];				
				for(int j=0; j < input.size(); j++) {
					List<Double> d = input.get(j);
					for(int i=0; i < d.size(); i++) {
						if(in_map.get(i) < 0) {
							o.base[j] = d.get(i);
						} else {
							o.F[j][in_map.get(i)] = d.get(i);
						}
					}
				}				
			} catch(Exception ex) {
				ex.printStackTrace();
			}			
			return o;
		}		
	}

	public void testFlats() throws Exception {
		Criteria cr = CriteriaXMLParser.loadXML("cfg/kv.xml");
		Optimize op = Optimize.getInput("cfg/input.txt", cr);
		Optimizer optimizer = new Optimizer();
		optimizer.optimize(cr, op.base, op.F);
	}

	public void test() throws Exception {
		Criteria criteria0 = CriteriaXMLParser.loadXML("cfg/test0.xml");
		Criteria criteria1 = CriteriaXMLParser.loadXML("cfg/test1.xml");
		Criteria criteria2 = CriteriaXMLParser.loadXML("cfg/test2.xml");

/*		double[][] F = new double[300][criteria0.getTotalSize()];
		Scanner inF = new Scanner(new File("F.txt"));
		for(int i = 0; i < F.length; i++) {
			for(int j = 0; j < F[i].length; j++) {
				F[i][j] = inF.nextDouble();
			}
		}
		inF.close();*/
		double[][] F = Optimizer.getMatrix(criteria0.getTotalSize(), 300);
		CacheCriteria cache1 = new CacheCriteria(criteria1, criteria0, F);
		CacheCriteria cache2 = new CacheCriteria(criteria2, criteria0, F);

		System.out.printf("1 = %4.4f;\n2 = %4.4f\n", cache1.check(), cache2.check());
		cache1.checkOut();
		System.out.println("----------------------");
		cache2.checkOut();
	}

}
