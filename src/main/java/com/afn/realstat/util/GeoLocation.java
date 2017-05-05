package com.afn.realstat.util;

import java.util.ArrayList;
import java.util.List;

import com.google.maps.model.EncodedPolyline;
import com.google.maps.model.LatLng;
import com.vaadin.tapio.googlemaps.client.LatLon;
import com.vaadin.tapio.googlemaps.client.overlays.GoogleMapPolyline;

public class GeoLocation {

	public static GoogleMapPolyline convert(EncodedPolyline modelPolyline) {
		List<LatLng> latLngPoly = modelPolyline.decodePath();
		List<LatLon> latLonPoly = new ArrayList<LatLon>();
		for (LatLng latLng : latLngPoly) {
			LatLon latLon = GeoLocation.convert(latLng);
			latLonPoly.add(latLon);
		}
		GoogleMapPolyline gmPoly = new GoogleMapPolyline(latLonPoly);
		return gmPoly;
	}

	public static LatLon convert(LatLng latLng) {
		LatLon latLon = new LatLon(latLng.lat, latLng.lng);
		return latLon;
	}
	
	public static LatLng convert(LatLon latLon) {
		LatLng latLng = new LatLng(latLon.getLat(), latLon.getLon());
		return latLng;
	}
}