package org.styskin.ca.matrix;

import static java.lang.Math.abs;

import java.io.PrintStream;

import org.styskin.ca.model.Constants;

strictfp public class Matrix implements Constants {

	/** Dimensions of Matrix */
	private int N, M;

	/** Data of Matrix */
	private double[][] data;

	/** Matrix constructor. Create N*M matrix and fills it with 0's
	 *
	 * @param N - rows count
	 * @param M - column count
	 */
	public Matrix(int N, int M) {
		this.N = N;
		this.M = M;
		data = new double[this.N][this.M];
	}

	/** Inverse matrix */
	public Matrix inverse()
		throws Exception {
		if(M != N) {
			throw new Exception("Matrix must be quadrix");
		}

		Matrix inv = new Matrix(N,N);

		for(int i=0; i < inv.getN(); i++) {
			inv.setCell(i,i, 1);
		}

		for(int i=0; i< this.getN(); i++) {
			solve(i, inv);
		}

		for(int i=getN()-1; i>= 0; i--) {
			solveUp(i, inv);
		}

		return inv;
	}

	/** Multiplisity of matrix */
	public Matrix multiplisity(Matrix b)
		throws Exception {
		if(this.M != b.getN()) {
			throw new Exception("a.M != b.N ");
		}
		Matrix c = new Matrix(this.getN(), b.getM());

		for(int i=0; i<this.getN(); i++ ) {
			for(int j=0; j<b.getM(); j++) {
				double value = 0;
				for(int k=0; k < this.M; k++) {
					value += this.getCell(i, k) * b.getCell(k, j);
				}
				c.setCell(i, j, value);
			}
		}
		return c;
	}

	/** Transponate matrix */
	public Matrix transponate() {
		Matrix b = new Matrix( this.getM(),this.getN());
		for(int i=0; i<this.getN(); i++ ) {
			for(int j=0; j<this.getM(); j++ ) {
				b.setCell(j, i,this.getCell(i,j));
			}
		}
		return b;
	}

	public int getN() {
		return N;
	}

	public int getM() {
		return M;
	}

	public double getCell(int i, int j) {
		return data[i][j];
	}

	public void setCell(int i, int j, double value) {
		data[i][j] = value;
	}



	private void solve(int k, Matrix b)
		throws Exception {
		// Normalize

		double d = this.getCell(k, k);
		for(int i=0; i < this.N; i++) {
			if(i > k) {
				this.setCell(k, i,  this.getCell(k, i)/ d);
			}
			b.setCell(k, i,  b.getCell(k, i)/ d);
		}
		this.setCell(k, k, 1);

		for(int i=k+1; i < this.N; i++ ) {
			d = this.getCell(i, k);
			for(int j = 0; j < this.N; j++ ) {
				if(j > k) {
					this.setCell(i, j, this.getCell(i,j) - d * this.getCell(k, j));
				}
				b.setCell(i, j, b.getCell(i,j) - d * b.getCell(k, j));
			}
			this.setCell(i, k, 0);
		}
	}

	private void solveUp(int k, Matrix b)
		throws Exception {

		for(int i=k-1; i >= 0; i-- ) {
			double d = this.getCell(i, k);

			for(int j = 0; j < this.N; j++ ) {
				b.setCell(i, j, b.getCell(i,j) - d * b.getCell(k, j));
			}
			this.setCell(i, k, 0);
		}
	}

	public boolean equals(Matrix b ) {
		if((this.getN() != b.getN()) || (this.getM() != b.getM())) {
			return false;
		}
		for(int i=0; i < this.getN(); i++ ) {
			for(int j=0; j < this.getM(); j++ ) {
				if(abs(this.getCell(i, j) - b.getCell(i,j)) > EPS) {
					return false;
				}
			}
		}
		return true;
	}

	public void output(PrintStream out) {
		for(int i=0; i< getN(); i++) {
			for(int j=0; j < getM(); j++) {
				out.printf("%3.3f ", getCell(i,j));
			}
			out.println();
		}
	}

	public static void output(Matrix r) {
		for(int i=0; i<r.getN(); i++) {
			for(int j=0; j<r.getM(); j++) {
				System.out.printf("%2.2f ", r.getCell(i,j));
			}
			System.out.println();
		}
	}

	public double[] getRow(int k) {
		return data[k];
	}
}
