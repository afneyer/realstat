package com.afn.realstat;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;
import com.vaadin.tapio.googlemaps.demo.GoogleMapsDemoUI;

public class GeoCodingApiTest {

	@Test
	public void testGeoCodingApi() {
		
		GeoApiContext context = new GeoApiContext().setApiKey(GoogleMapsDemoUI.getGoogleApiKey());
		GeocodingResult[] results = null;
		try {
			results = GeocodingApi.geocode(context,
			    "112 Indian Road, Piedmont, CA 94610").await();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(results[0].formattedAddress);
		assertEquals("112 Indian Rd, Piedmont, CA 94610, USA",results[0].formattedAddress);
		LatLng latlng = results[0].geometry.location;
		assertEquals( 37.816977, latlng.lat,0.0000001);
		assertEquals(-122.2220519, latlng.lng,0.0000001);
	}
}
