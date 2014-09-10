import static org.junit.Assert.*;
import static java.lang.StrictMath.*;

import org.junit.Test;


public class FeaturesTest {

	@Test
	public void testSolve() {
		assertEquals("Not angle", Features.canonicalAngle(Math.atan2(0 - 100, 0 - 100)), Features.canonicalAngle(Features.solve(0, 0, 100, 100, 0, 0, 1, 0)), Features.EPS);
		assertEquals("Not angle", Features.canonicalAngle(PI*(-90)/180), Features.canonicalAngle(Features.solve(0, 0, 100, 100, 1, 0, 1, 0)), Features.EPS);
		assertEquals("Not angle", Features.canonicalAngle(PI*(-180)/180), Features.canonicalAngle(Features.solve(0, 0, 100, 100, 0, 1, 1, 0)), Features.EPS);
		
		double a = Features.solve(0, 300, 100, 100, 10, 2, 10, 0);
		double t = Features.dT(0, 300, 500, 100, 1, 0.2, 5, a);
		System.out.printf("%f %f\n", 180*Features.canonicalAngle(a) / PI, t);

	}

}
