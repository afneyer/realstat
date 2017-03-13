package com.afn.util;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.assertj.core.util.DateUtil;
import org.slf4j.LoggerFactory;
import org.springframework.data.geo.Point;

import com.google.gwt.maps.client.services.Geocoder;
import com.google.gwt.maps.client.services.GeocoderRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.GeocodingApiRequest;
import com.google.maps.PendingResult;
import com.google.maps.GeolocationApi.Response;
import com.google.maps.errors.ApiException;
import com.google.maps.model.AddressComponent;
import com.google.maps.model.AddressComponentType;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.Geometry;

import ch.qos.logback.classic.Logger;

/**
 * MapLocation is initialized with an address. The getters return the
 * information about the location.
 * 
 * @author Andreas Neyer
 *
 *         Copyright 2017 afndev. All rights reserved.
 *
 */
public class MapLocation {

	private static final Logger log = (Logger) LoggerFactory.getLogger("app");

	private final static String apiKey = "AIzaSyCSgBJHB0XMVHlGaMrTgL-YO2_pHhPtuKc";

	private static final Integer maxCallsPerDay = 2000;
	private static Integer numCallsToday = null;
	private static Integer maxCallsPerSecond = 50;
	private static Date lastCall = null;

	private Geometry geo = null;
	private GeocodingResult result = null;
	private GeocodingResult[] results = null;
	private GeocodingApiRequest gar = null;
	private Response resp = null;

	public MapLocation(String address) {

	

		/*
		 * Geocoder geocoder =Geocoder.newInstance(); GeocoderRequest
		 * geocoderRequest = GeocoderRequest.newInstance();
		 * geocoderRequest.setAddress(address); Geocoder geocoderResponse =
		 * geocoder.geocode(geocoderRequest, null);
		 */

		GeoApiContext context = new GeoApiContext().setApiKey(apiKey);
		context.setQueryRateLimit(maxCallsPerSecond);
		if (waitForApiReady()) {
			try {
				updateCallData();
				System.out.println("Calling geo-coder for: " + address);
				results = GeocodingApi.geocode(context, address).await();
				if (results != null) {
					geo = results[0].geometry;
				}
			} catch (Exception e) {
				log.error("Error in MapLocation: Cannot convert address =" + address + e);
				System.out.println(e);
				e.printStackTrace();
			}

		} else {
			log.error("Error executing Google Map API, mostly likely exceeded daily maximum");
		}

	}

	private void updateCallData() {	
		
		numCallsToday++;
		lastCall = new Date();
		int numCallsBeforeSave = maxCallsPerDay / 100;
		if (numCallsBeforeSave > 0 && numCallsToday % numCallsBeforeSave == 0) {
			// save to database
		}
	}

	private boolean waitForApiReady() {
		
		if (numCallsToday == null || lastCall == null) {
			numCallsToday = 0;
			lastCall = new Date();
		}
		
		if (numCallsToday > maxCallsPerDay) {
			return false;
		} 
		/*
		else {
			Date now = new Date();
			int maxMilli = 1000 / maxCallsPerSecond;
			long diffInMilli = DateUtil.timeDifference(now, lastCall);
			if (diffInMilli < maxMilli) {
				sleepInMilliseconds(maxMilli - diffInMilli);
			}
			return true;
		} */
		return true;
	}

	private void sleepInMilliseconds(long millis) {
		try {
			TimeUnit.MILLISECONDS.sleep(millis);
			System.out.println("Slept for " + millis + "waiting");
		} catch (InterruptedException e) {
			log.error("Error in sleepInMilliseconds in MapLocation");
		}
		;

	}

	public String getFormattedAddress() {
		String r = null;
		if (isValid()) {
			r = results[0].formattedAddress;
		}
		return r;
	}

	public Double getLng() {
		if (isValid()) {
			return geo.location.lng;
		}
		return 0.0;
	}

	public Double getLat() {
		if (isValid()) {
			return geo.location.lat;
		}
		return 0.0;
	}

	public Point getLocation() {
		if (isValid()) {
			return new Point(getLng(), getLat());
		}
		return null;
	}

	public String getStreetNbr() {
		String r = null;
		if (isValid()) {
			r = getAddressComponentType(AddressComponentType.STREET_NUMBER);
		}
		return r;
	}

	public String getStreet() {
		String r = null;
		if (isValid()) {
			r = getAddressComponentType(AddressComponentType.ROUTE);
		}
		return r;
	}

	public String getCity() {
		String r = null;
		if (isValid()) {
			r = getAddressComponentType(AddressComponentType.LOCALITY);
		}
		return r;
	}

	public String getCounty() {
		String r = null;
		if (isValid()) {
			r = getAddressComponentType(AddressComponentType.ADMINISTRATIVE_AREA_LEVEL_2);
		}
		return r;
	}

	public String getState() {
		String r = null;
		if (isValid()) {
			r = getAddressComponentType(AddressComponentType.ADMINISTRATIVE_AREA_LEVEL_1);
		}
		return r;
	}

	public String getCountry() {
		String r = null;
		if (isValid()) {
			r = getAddressComponentType(AddressComponentType.COUNTRY);
		}
		return r;
	}

	public String getZip() {
		String r = null;
		if (isValid()) {
			r = getAddressComponentType(AddressComponentType.POSTAL_CODE);
		}
		return r;
	}

	public String getZip4() {
		String r = null;
		if (isValid()) {
			r = getAddressComponentType(AddressComponentType.POSTAL_CODE_SUFFIX);
		}
		return r;
	}

	public Boolean getPartialMatch() {
		Boolean r = true;
		if (isValid()) {
			r = results[0].partialMatch;
		}
		return r;
	}

	private String getAddressComponentType(AddressComponentType type) {
		String result = null;
		AddressComponent[] cmpList = results[0].addressComponents;

		for (int i = 0; i < cmpList.length; i++) {
			AddressComponent adrCmpnt = cmpList[i];
			com.google.maps.model.AddressComponentType[] types = adrCmpnt.types;
			for (int j = 0; j < types.length; j++) {
				if (types[j] == type) {
					return cmpList[i].shortName;
				}
			}
		}

		return result;
	}

	public Boolean isValid() {
		if (results == null) {
			return false;
		}
		if (results.length == 0)
			return false;
		if (geo == null)
			return false;
		return true;
	}
}
