package com.afn.realstat;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.afn.util.MapLocation;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class MapLocationTest {

	@Test
	public void testGeoCodingApi() {

		MapLocation mapLoc = new MapLocation("112 Indian Road, Piedmont, CA 94610");
		assertEquals("112 Indian Rd, Piedmont, CA 94610, USA", mapLoc.getFormattedAddress());
		assertEquals(37.816977, mapLoc.getLat(), 0.0000001);
		assertEquals(-122.220519, mapLoc.getLng(), 0.0000001);
		
		assertEquals("112",mapLoc.getStreetNbr());
		assertEquals("Indian Rd", mapLoc.getStreet());
		assertEquals("Piedmont", mapLoc.getCity());
		assertEquals("Alameda County",mapLoc.getCounty());
		assertEquals("CA",mapLoc.getState());
		assertEquals("US",mapLoc.getCountry());
		assertEquals("94610",mapLoc.getZip());
		assertEquals("1201",mapLoc.getZip4());
	}
	
	
}
