package com.afn.realstat.util;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.slf4j.LoggerFactory;
import org.springframework.data.geo.Point;

import com.afn.realstat.AfnDateUtil;
import com.afn.realstat.AppParamManager;
import com.afn.realstat.framework.SpringApplicationContext;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
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

	private static AppParamManager apmMgr = (AppParamManager) SpringApplicationContext.getBean("appParamManager");

	private static Integer maxCallsPerDay = new Integer(apmMgr.getVal("maxCallsPerDay", "MAP"));
	private static Integer maxCallsPerSecond = new Integer(apmMgr.getVal("maxCallsPerSecond", "MAP"));
	private static Integer numCallsToday = null;
	private static Date lastCall = null;

	private Geometry geo = null;
	private GeocodingResult[] results = null;

	public MapLocation(String address) {

		GeoApiContext context = new GeoApiContext().setApiKey(apiKey);
		context.setQueryRateLimit(maxCallsPerSecond);

		if (!apiLimitReached()) {
			try {
				updateCallData();
				System.out.println("Call number " + numCallsToday + " today to geo-coder for address: " + address);
				results = GeocodingApi.geocode(context, address).await();
				if (results != null && results.length != 0) {
					geo = results[0].geometry;
				}
			} catch (Exception e) {
				log.error("Error in MapLocation: Cannot convert address =" + address + "Exception = " + e);
			}

		} else {
			log.error("Error executing Google Map API, mostly likely exceeded daily maximum");
		}

	}

	private void updateCallData() {

		// if last call is yesterday, set it today and reset the number of calls
		// made today to zero
		Date lastCallDate = apmMgr.getDateVal("lastCall", "MAP");
		if (lastCallDate == null) {
			// lastCallDate = AfnDateUtil.dateYesterday();
		}

		numCallsToday = new Integer(apmMgr.getVal("callsToday", "MAP"));

		Date today = AfnDateUtil.dateToday();
		if (lastCallDate.before(today)) {
			lastCall = today;
			numCallsToday = 0;
		}

		// update call parameters
		numCallsToday++;
		apmMgr.setVal("callsToday", "MAP", Integer.toString(numCallsToday));
		apmMgr.setVal("lastCall", "MAP", lastCall);
		System.out.println("numCallsToday = " + numCallsToday);
		System.out.println("lastCall = " + lastCall);
	}

	public static boolean apiLimitReached() {

		if (numCallsToday == null || lastCall == null) {
			numCallsToday = 0;
			lastCall = new Date();
		}

		if (numCallsToday >= maxCallsPerDay) {
			return true;
		}
		return false;
	}

	public static Integer apiLimit() {
		return maxCallsPerDay;
	}

	// TODO Move to Timer function
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
