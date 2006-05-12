package org.styskin.ca.functions;

public abstract class Criteria {

	protected double[][] F;
	protected double[] base; // F->y

	public void setMatrix() {
		double[] CASES = {0.2, 0.5, 0.8};
		int VAR_NUMBER = this.getTotalSize();
		int CASE_NUMBER = (int) Math.round(Math.pow(CASES.length, VAR_NUMBER));

		F = new double[CASE_NUMBER][VAR_NUMBER];
		for(int i=0; i < CASE_NUMBER; i++) {
			int mod = CASE_NUMBER/3;
			for(int j=0; j < VAR_NUMBER; j++) {
				F[i][j] = CASES[(i / mod) %3];
				mod /= 3;
			}
		}
	}

	public double getValues(double... X) throws Exception {
//		setValues(X, 0, X.length - 1);
		return getValue(X, 0, X.length - 1);
	}

	public void setBase(Criteria c) {
		base = new double[F.length];
		// TODO rewrite try-catch block
		try {
			for(int i=0; i< F.length; i++) {
				base[i] = c.getValues(F[i]);
			}
		} catch(Exception e) {}
	}

	public double check() {
		double d = 0;
		try {
			for(int i=0; i< F.length; i++) {
				double t = getValues(F[i]) - base[i];
				d += t*t;
			}
		} catch(Exception e) {}
		return Math.sqrt(d);
	}

	abstract protected double getValue(double[] X, int start, int end) throws Exception;

	abstract protected int getSize();

	abstract public int getTotalSize();
}
