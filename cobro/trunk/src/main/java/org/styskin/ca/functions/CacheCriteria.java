/*
 *$Id$
 */
package org.styskin.ca.functions;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.IdentityHashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.styskin.ca.model.CriteriaXMLParser.Optimize;

public class CacheCriteria {
	
	private static final Logger logger = Logger.getLogger(CacheCriteria.class); 

	private Criteria root;

	private double[][] R;
	private Map<Criteria, Boolean> cached;
	private Map<Criteria, Integer> index;
	private Map<Criteria, Criteria> parentRelation;
	
	private double[] base;
	protected double[][] F;

	private Checker checker;
	private Checker absChecker;
	private Checker correlationChecker;
	
	private CacheCriteria(Criteria criteria, double[][] F) throws Exception {
		this.F = F;
		root = criteria;
		// XXX: use IdentityHashMap for storing intermidiate results
		cached = new IdentityHashMap<Criteria, Boolean>();
		index = new IdentityHashMap<Criteria, Integer>();
		parentRelation = new IdentityHashMap<Criteria, Criteria>();
		int size = buildIndex(root, 0);
		R = new double[size + 1][F.length];
		buildSingle(root, 0);
		clearCache();
		refreshCache();
	}

	// TODO throws Exception base.length != F.length
	public CacheCriteria(Criteria criteria, Optimize op) throws Exception {
		this(criteria, op.getF());
		this.base = op.getBase();
		initChecker(op);
	}

	private void initChecker(Optimize op) {
/*		int[] req = new int[base.length];
		String cq = null;
		int qi = -1;
		for(int i=0; i < req.length; i++) {
			String l = op.getLines()[i];
			l = l.substring(0, l.indexOf('\t'));
			l = l.substring(0, l.lastIndexOf(' '));
			if(!l.equals(cq)) {
				++ qi;
				cq = l;
			}
			req[i] = qi;
		}*/		
//		checker = new CheckLeastSquare(base);
		checker = new CheckSampleCorrelation(base);
		absChecker = new CheckAbs(base);
		correlationChecker = new CheckSampleCorrelation(base);
//		correlationChecker = new CheckPfound(base, req);
	}
	
	private int buildIndex(Criteria c, int p) {
		int size = p;
		index.put(c, size);
		cached.put(c, Boolean.FALSE);
		if(c instanceof ComplexCriteria) {
			for(Criteria child : c.getChildren()) {
				size = buildIndex(child, size + 1);
				parentRelation.put(child, c);
			}
		}
		return size;
	}

	private int buildSingle(Criteria c, int p) throws Exception {
		if(c instanceof ComplexCriteria) {
			for(Criteria child : c.getChildren()) {
				p = buildSingle(child, p);
			}
		} else {
			int me = index.get(c);
			for(int i = 0; i < F.length; ++i) {
				R[me][i] = c.getValue(F[i], p, p);
			}
			++ p;
		}
		return p;
	}


	public void clearCache() {
		for(Criteria c : cached.keySet()) {
			cached.put(c, Boolean.FALSE);
		}
	}

	public void turnOffCache(Criteria criteria) {
		Criteria parent = criteria;
		while(parent != null) {
			cached.put(parent, Boolean.FALSE);
			parent = parentRelation.get(parent);
		}
	}

	public void refreshCache() throws Exception {
		calcValue(root);
		for(Map.Entry<Criteria, Boolean> en : cached.entrySet())
			en.setValue(Boolean.TRUE);		
	}

//	private void setBase(Criteria c) throws Exception {
//		base = new double[F.length];
//		for(int i=0; i< F.length; i++) {
//			base[i] = c.getValues(F[i]);
//		}
//	}

	public double[] getValue() throws Exception {
		calcValue(root);
		return R[0];
	}

	private void calcValue(Criteria c) throws Exception {
		if(!cached.get(c)) {
			if (c instanceof ComplexCriteria) {
				ComplexCriteria cc = (ComplexCriteria) c;
				int size = cc.getSize();
				int[] ind = new int[size];
				int j = 0;
				for(Criteria child : cc.getChildren()) {
					ind[j++] = index.get(child);
					calcValue(child);
				}
				double[] P = new double[size];
				int me = index.get(c);
				for(int i = 0; i < F.length; i++) {
					for(int l = 0; l < P.length; l++) {
						P[l] = R[ind[l]][i];
					}
					R[me][i] = cc.getOperator().getValue(P);
				}
			}
		}
	}

	public void checkOut() throws Exception {
		double[] Y = getValue();
		double d = 0;
		for(int i=0; i< base.length; i++) {
			logger.info(base[i] + " " + Y[i]);
			d += Math.abs(base[i]-Y[i]);
		}
		logger.info("Sum = " + d);
	}
	
	public void checkOut2(String path) throws Exception {
		double[] Y = getValue();
		outputValues(path, Y);
	}

	public static void outputValues(String path, double[] Y) throws IOException {
		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(path)));			
		for(int i=0; i< Y.length; i++) {
			out.printf("%4.4f\n", Y[i]) ;
		}
		out.close();
	}
	
	
	public double check() throws Exception {
		double[] x = getValue();
		return checker.check(x);
	}

	//XXX: hack
	public double checkAbs() throws Exception {
		double[] x = getValue();
		return absChecker.check(x);
	}
	
	public double checkCorrelation() throws Exception {
		double[] x = getValue();
		return correlationChecker.check(x);
	}


	public double[] getValue(Criteria criteria) throws Exception {
		calcValue(criteria);
		return R[index.get(criteria)];
	}

}
