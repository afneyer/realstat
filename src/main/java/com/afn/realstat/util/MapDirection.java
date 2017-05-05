package com.afn.realstat.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.joda.time.Instant;
import org.slf4j.LoggerFactory;

import com.afn.realstat.Address;
import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.EncodedPolyline;
import com.google.maps.model.LatLng;

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
public class MapDirection {

	private static final Logger log = (Logger) LoggerFactory.getLogger("app");

	private Address start;
	private Address end;
	private List<Address> route;
	private GoogleMapApi mapApi = null;

	public MapDirection(Address start, Address end, List<Address> routeList) {

		this.mapApi = new GoogleMapApi();
		this.start = start;
		this.end = end;
		this.route = routeList;

	}

	public EncodedPolyline route(Date startTime) {

		Instant routeStart = new Instant(startTime);

		EncodedPolyline polyLine = null;

		LatLng startPoint = start.getLatLng();
		LatLng endPoint = end.getLatLng();

		int wpSize = route.size();
		LatLng[] wayPoints = new LatLng[wpSize];
		for (int i = 0; i < wpSize; i++) {
			wayPoints[i] = route.get(i).getLatLng();
		}

		DirectionsApiRequest request = null;
		if (routeStart.isAfter(new Instant())) {
			request = DirectionsApi.newRequest(mapApi.getContext()).origin(startPoint).destination(endPoint)
					.departureTime(routeStart).optimizeWaypoints(true).waypoints(wayPoints);
		} else {
			request = DirectionsApi.newRequest(mapApi.getContext()).origin(startPoint).destination(endPoint)
					.optimizeWaypoints(true).waypoints(wayPoints);
		}
		DirectionsResult result = null;
		try {
			result = request.await();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException(e);
		}

		DirectionsRoute[] directionsRouteList = result.routes;
		for (int i = 0; i < directionsRouteList.length; i++) {
			DirectionsRoute directionsRoute = directionsRouteList[i];
			polyLine = directionsRoute.overviewPolyline;
		
		}
		return polyLine;
	}
}