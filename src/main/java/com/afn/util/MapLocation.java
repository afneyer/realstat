package com.afn.util;

import org.slf4j.LoggerFactory;
import org.springframework.data.geo.Point;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
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

	private final static String apiKey = "AIzaSyBCyOA84WMzCHjkrEdFxtrH-pGYcFGSywE";

	private Geometry geo = null;
	private String formattedAddress = null;

	public MapLocation(String address) {
		GeoApiContext context = new GeoApiContext().setApiKey(apiKey);
		GeocodingResult[] results = null;
		try {
			results = GeocodingApi.geocode(context, address).await();
		} catch (Exception e) {
			log.error("Error in MapLocation: Cannot convert address =" + "address");
			e.printStackTrace();
		}
		formattedAddress = results[0].formattedAddress;
		geo = results[0].geometry;

	}

	public String getFormattedAddress() {
		return formattedAddress;
	}

	public Double getLng() {
		return geo.location.lng;
	}

	public Double getLat() {
		return geo.location.lat;
	}
	
	public Point getLocation() {
		return new Point(getLng(),getLat());
	}
}
