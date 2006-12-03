/*
 *$Id$
 */
package org.styskin.ca.functions;

import java.util.HashMap;
import java.util.Map;

/**
 * @author astyskin
 *
 */
public class CacheCriteria {

	private Criteria root;

	private double[][] R;
	private Map<Criteria, Boolean> cached;
	private Map<Criteria, Integer> index;


	private double[] base;
	protected double[][] F;

	private CacheCriteria(Criteria criteria, double[][] F) {
		this.F = F;
		root = criteria;
		cached = new HashMap<Criteria, Boolean>();
		index = new HashMap<Criteria, Integer>();
		int size = buildIndex(root, 0);
		R = new double[size + 1][F.length];
		buildSingle(root, 0);
		clearCache();
		refreshCache();
	}

	// TODO throws Exception base.length != F.length
	public CacheCriteria(Criteria criteria, double[] base, double[][] F) {
		this(criteria, F);
		this.base = base;
	}


	public CacheCriteria(Criteria criteria, Criteria criteriaBase, double[][] F) {
		this(criteria, F);
		setBase(criteriaBase);
	}

	private int buildIndex(Criteria c, int p) {
		int size = p;
		index.put(c, size);
		cached.put(c, false);
		if(c instanceof ComplexCriteria) {
			for(Criteria child : c.getChildren()) {
				size = buildIndex(child, size + 1);
			}
		}
		return size;
	}

	private int buildSingle(Criteria c, int p) {
		if(c instanceof ComplexCriteria) {
			for(Criteria child : c.getChildren()) {
				p = buildSingle(child, p);
			}
		} else {
			int me = index.get(c);
			try {
				for(int i = 0; i < F.length; i++) {
					R[me][i] = c.getValue(F[i], p, p);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			p++;
		}
		return p;
	}


	public void clearCache() {
		for(Criteria c : cached.keySet()) {
			cached.put(c, false);
		}
	}

	public void turnOffCache(Criteria criteria) {
		cached.put(criteria, false);
		renewCache(root);
	}

	private boolean renewCache(Criteria criteria) {
		boolean on = cached.get(criteria);
		for(Criteria child : criteria.getChildren()) {
			on &= renewCache(child);
		}
		cached.put(criteria, on);
		return on;
	}

	public void refreshCache() {
		calcValue(root);
		for(Criteria c : cached.keySet()) {
			cached.put(c, true);
		}
	}

	private void setBase(Criteria c) {
		base = new double[F.length];
		// TODO rewrite try-catch block
		try {
			for(int i=0; i< F.length; i++) {
				base[i] = c.getValues(F[i]);
			}
		} catch(Exception e) {}
	}

	public double[] getValue() {
		calcValue(root);
		return R[0];
	}

	private void calcValue(Criteria c) {
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
					// TODO
					try {
						R[me][i] = cc.getOperator().getValue(P);
					} catch (Exception ex) {}
				}
			} else {
				cached.put(c, true);
			}
		}
	}

	public String arrayOut(double[] m) {
		StringBuffer sb = new StringBuffer();
		sb.append("{");
		for(double x : m) {
			sb.append(x).append(';');
		}
		sb.append('}');
		return sb.toString();
	}

	public void checkOut() {
		double[] Y = getValue();
		double d = 0;
		for(int i=0; i< base.length; i++) {
			System.out.printf("%s => (%4.4f ==> %4.4f)\n", arrayOut(F[i]), base[i], Y[i]) ;
			d += Math.abs(base[i]-Y[i]);
		}
		System.out.printf("Sum = %4.4f\n", d);
	}

	public double check() {
		double d = 0;
		double[] Y = getValue();
		for(int i=0; i< base.length; i++) {
			double t = Y[i] - base[i];
			d += t*t;
		}
		return Math.sqrt(d);
	}
/*	public double check() {
		double d = 0;
		double[] Y = getValue();
		for(int i=0; i< base.length; i++) {
			double t = Y[i] - base[i];
			if (Math.abs(t) > d) {
				d = Math.abs(t);
			}
		}
		return Math.sqrt(d);
	}*/

	public double[] getValue(Criteria criteria) {
		calcValue(criteria);
		return R[index.get(criteria)];
	}

	public double[][] getInput(ComplexCriteria cr) {
		int size = cr.getSize();
		int[] ind = new int[size];
		double[][] values = new double[F.length][size];
		int j = 0;
		for(Criteria child : cr.getChildren()) {
			ind[j++] = index.get(child);
			calcValue(child);
		}
		int me = index.get(cr);
		for(int i = 0; i < F.length; i++) {
			for(int l = 0; l < values[i].length; l++) {
				values[i][l] = R[ind[l]][i];
			}
			// TODO
			try {
				R[me][i] = cr.getOperator().getValue(values[i]);
			} catch (Exception ex) {}
		}
		return values;
	}
}
