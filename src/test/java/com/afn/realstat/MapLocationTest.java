package com.afn.realstat;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.afn.util.MapLocation;

public class MapLocationTest {

	@Test
	public void testGeoCodingApi() {

		MapLocation mapLoc = new MapLocation("112 Indian Road, Piedmont, CA 94610");
		assertEquals("112 Indian Rd, Piedmont, CA 94610, USA", mapLoc.getFormattedAddress());
		assertEquals(37.816977, mapLoc.getLat(), 0.0000001);
		assertEquals(-122.220519, mapLoc.getLng(), 0.0000001);

	}
}
