package com.afn.realstat.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.vaadin.tapio.googlemaps.GoogleMap;
import com.vaadin.tapio.googlemaps.client.LatLon;
import com.vaadin.tapio.googlemaps.client.events.MapClickListener;
import com.vaadin.tapio.googlemaps.client.overlays.GoogleMapMarker;
import com.vaadin.ui.TextField;

@SuppressWarnings("serial")
public class AfnGoogleMap extends GoogleMap {

	private GoogleMapMarker dummyMarker;
	private final String emptyIcon = "VAADIN/emptyIcon.jpg";

	public AfnGoogleMap(String apiKey, String clientId, String language) {
		super(apiKey, clientId, language);

		dummyMarker = new GoogleMapMarker("Dummy Marker", new LatLon(0, 0), false, null);
		dummyMarker.setIconUrl(emptyIcon);
	}

	public AfnGoogleMap() {
		this(null,null,null);
	}

	public List<TourMarker> getTourMarkers() {
		List<TourMarker> markerList = new ArrayList<TourMarker>();
		
		Iterator<GoogleMapMarker> markerIter = getMarkers().iterator();
		
		GoogleMapMarker m;
		while (markerIter.hasNext() ) {
			m = markerIter.next();
			if ( TourMarker.class.isInstance(m)) {
				markerList.add((TourMarker) m);
			}
		}
		
		return markerList;
	}

	/**
	 * Refreshes the Map
	 * 
	 * For lack of better option adds or removes a dummy marker which forces a
	 * refresh. Used the refresh the client after performing actions on a map
	 * marker (e.g. change icon) that does not push to the client.
	 * 
	 * Probably not the best solutions. Would expect that VAADIN refreshes
	 * automatically or has a function to force a refresh.
	 * 
	 */
	protected void refresh() {

		// refresh function
		if (hasMarker(dummyMarker)) {
			removeMarker(dummyMarker);
		} else {
			addMarker(dummyMarker);
		}

	}

	/**
	 * Set the scale (zoom) and the boundaries of the map so that all markers
	 * are shown on the map
	 * 
	 * @param map
	 */
	public void centerOnTourMarkers() {

		Collection<TourMarker> markers = getTourMarkers();
		
		Collection<GoogleMapMarker> gMarkers = Collections.unmodifiableCollection(markers);

		this.centerOnMarkers(gMarkers);

	}

	
	/**
	 * Centers map on markers excluding the dummy marker
	 */
	public void centerOnMarkers() {
		centerOnMarkers(this.getMarkers());
	}
	
	/**
	 * Centers map on a collection of markers
	 * Excludes dummy marker
	 */
	private void centerOnMarkers(Collection<GoogleMapMarker> markers) {

		double latMin = 90.0;
		double latMax = -90.0;
		double lonMin = 180.0;
		double lonMax = -180.0;
		Iterator<GoogleMapMarker> iter = markers.iterator();

		GoogleMapMarker m;
		while (iter.hasNext()) {
			m = iter.next();
			
			// Skip dummyMarker
			if (m == dummyMarker) {
				break;
			}

			double lat = m.getPosition().getLat();
			latMin = Math.min(latMin, lat);
			latMax = Math.max(latMax, lat);

			double lon = m.getPosition().getLon();
			lonMin = Math.min(lonMin, lon);
			lonMax = Math.max(lonMax, lon);

		}

		if (markers.size() != 0) {
			LatLon boundsNE = new LatLon(latMax, lonMax);
			LatLon boundsSW = new LatLon(latMin, lonMin);
			fitToBounds(boundsNE, boundsSW);
		}

	}

}
