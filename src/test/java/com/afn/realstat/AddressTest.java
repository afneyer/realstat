package com.afn.realstat;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class AddressTest {

	@Test
	public void testCleanAddress() {

		Address origAddr;
		String targetStr;

		// Test 1
		origAddr = new Address("2140 Arrowhead street", "Oakland", "94611");
		targetStr = "2140||ARROWHEAD|||OAKLAND|94611";
		assertEquals(targetStr, origAddr.getCleanAddress());

		origAddr = new Address("2140 Arrowhead", "", "OAKLAND", "94611");
		assertEquals(targetStr, origAddr.getCleanAddress());

		// Test 2
		// Address='1 KELTON CT', Unit='7H', City='OAKLAND',Zip='94611'
		origAddr = new Address("1 KELTON CT", "#7H", "OAKLAND", "94611");
		targetStr = "1||KELTON||7H|OAKLAND|94611";
		assertEquals(targetStr, origAddr.getCleanAddress());

		origAddr = new Address("1 Kelton Ct. Apt. 7H", "Oakland", "94611");
		assertEquals(targetStr, origAddr.getCleanAddress());

		// Test 3
		// Address='155 PEARL ST', Unit='304', City='OAKLAND',Zip='94611'
		origAddr = new Address("155 PEARL ST", "304", "OAKLAND", "94611");
		targetStr = "155||PEARL||304|OAKLAND|94611";
		assertEquals(targetStr, origAddr.getCleanAddress());

		origAddr = new Address("155 Pearl St Apt 304", "Oakland", "94611");
		assertEquals(targetStr, origAddr.getCleanAddress());

		// Test 4
		// Address='2005 PLEASANT VALLEY', Unit='308',
		// City='OAKLAND',Zip='94611'
		origAddr = new Address("2005 PLEASANT VALLEY", "308", "OAKLAND", "94611");
		targetStr = "2005||PLEASANT VALLEY||308|OAKLAND|94611";
		assertEquals(targetStr, origAddr.getCleanAddress());

		origAddr = new Address("2005 Pleasant Valley Ave. #308", "OAKLAND", "94611");
		targetStr = "2005||PLEASANT VALLEY||308|OAKLAND|94611";
		assertEquals(targetStr, origAddr.getCleanAddress());

		// Test 5
		origAddr = new Address("150 Pearl St Apt 315", "OAKLAND", "94611");
		targetStr = "150||PEARL||315|OAKLAND|94611";
		assertEquals(targetStr, origAddr.getCleanAddress());
		
		// Test 6
		origAddr = new Address("150 Pearl St Apt 315", "OAKLAND", "94611-4321");
		targetStr = "150||PEARL||315|OAKLAND|94611";
		assertEquals(targetStr, origAddr.getCleanAddress());

	}

	@Test
	public void testHasUnitInfo() {
		Address adr;
		adr = new Address("1 KELTON CT", "#7H", "OAKLAND", "94611");
		assertTrue(adr.hasUnitInfo());

		adr = new Address("2140 Arrowhead street", "Oakland", "94611");
		assertFalse(adr.hasUnitInfo());
	}

	@Test
	public void testIsInSameBuilding() {

		Address adr1;
		Address adr2;

		adr1 = new Address("1 KELTON CT", "#7H", "OAKLAND", "94611");
		adr2 = new Address("1 KELTON CT", "8Q", "OAKLAND", "94611");
		assertTrue(adr1.isInSameBuilding(adr2));

		adr2 = new Address("2140 Arrowhead street", "Oakland", "94611");
		assertFalse(adr1.isInSameBuilding(adr2));
	}

	@Test
	public void testSpecialCases() {
		Address origAddr = new Address("2005 Pleasant Valley #308", "OAKLAND", "94611");
		String targetStr = "2005||PLEASANT VALLEY||308|OAKLAND|94611";
		assertEquals(targetStr, origAddr.getCleanAddress());
	}

}
