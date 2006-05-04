package org.styskin.ca.test;

import org.styskin.ca.matrix.Matrix;
import static org.styskin.ca.matrix.Matrix.output;

import junit.framework.TestCase;

public class MatrixTest extends TestCase {

	Matrix E = new Matrix(3,3);
	Matrix a = null;

	protected void setUp() throws Exception {
		super.setUp();

		E.setCell(0,0, 1);
		E.setCell(1,1, 1);
		E.setCell(2,2, 1);

		a = new Matrix(3, 3);

		a.setCell(0,0, 2);
		a.setCell(0,1, -1);
		a.setCell(1,0, 1);
		a.setCell(1,1, 1);
		a.setCell(2,0, 19);
		a.setCell(0,2, 18);
		a.setCell(2,1, -4);
		a.setCell(1,2, 8);
		a.setCell(2,2, 99);
	}


	public void testInverse1()
		throws Exception {

		Matrix b;
		output(a.inverse());

		b = (a.inverse()).multiplisity(a);
		output(b);
		assertTrue(a.equals(b));

		b = (a).multiplisity(a.inverse());
		output(b);
		assertTrue(a.equals(b));

	}

}
