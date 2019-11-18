package FinalProject_29;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

import FinalProject_29.SUT;

public class TestSUT {

	@Test
	public void allBranchCoverageMinimumTestCaseForReturnAverageTest1() {
		int var1 = 4;
		int var2 = 10;
		
		double average = SUT.Math(var1,var2);
		assertEquals(7, average, 0.1);
	}

	@Test
	public void allBranchCoverageMinimumTestCaseForReturnAverageTest2() {
		int var1 = 1;
		int var2 = 10;
		
		double average = SUT.Math(var1,var2);
		assertEquals(5, average, 0.1);
	}
}
