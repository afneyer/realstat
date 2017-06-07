package com.afn.realstat.ui;

import org.springframework.data.geo.Point;

import com.afn.realstat.MyTour;
import com.afn.realstat.MyTourStop;
import com.afn.realstat.util.Icon;
import com.vaadin.tapio.googlemaps.client.LatLon;
import com.vaadin.tapio.googlemaps.client.overlays.GoogleMapMarker;

@SuppressWarnings("serial")
public class TourMarker extends GoogleMapMarker {

	private MyTour myTour;
	private MyTourStop myTourStop;
	private AfnGoogleMap googleMap;
	private TourListMarkerClickListener clickListener;
	private boolean isInTour = false;
	private String blankInUrl;
	private String blankOutUrl;

	public TourMarker(MyTourStop mts, AfnGoogleMap map) {
		super();

		this.myTour = mts.getTour();
		this.myTourStop = mts;
		this.googleMap = map;

		Point location = mts.getLocation();
		// TODO re-factor conversion from point to LatLon
		LatLon pos = new LatLon(location.getY(), location.getX());
		this.setPosition(pos);

		String cap = mts.mapMarkerCaption();
		this.setCaption(cap);

		this.setDraggable(true);
		this.setAnimationEnabled(false);
		this.blankOutUrl = new Icon( Icon.MarkerButtonGreen).getIconUrl();
		this.blankInUrl = new Icon( Icon.MarkerButtonRed).getIconUrl();
		this.setIconUrl(blankOutUrl);

	}
	
	public String getOutIconUrl() {
		return blankOutUrl;
	}
	
	public String getInIconUrl() {
		String inIconUrl = blankInUrl;
		int seq = myTourStop.getSequence();
		String text = Integer.toString(seq);
		if ( seq != 0) {
			inIconUrl = new Icon( Icon.MarkerButtonGreen, text ).getIconUrl();
		}
		return inIconUrl;
	}

	public boolean isInTour() {
		return isInTour;
	}

	public boolean toggleTour() {
		if (isInTour) {
			excludeFromTour();
			return false;
		} else {
			includeInTour();
			return true;
		}
	}

	public void includeInTour() {
		// change icon
		if (!isInTour) {
			setIconUrl(getInIconUrl());
			isInTour = true;
			googleMap.refresh();
		}
	}

	public void excludeFromTour() {

		if (isInTour()) {
			// change icon
			setIconUrl(getOutIconUrl());
			isInTour = false;
			googleMap.refresh();
		}
	}

	public MyTour getTour() {
		return myTour;
	}

	public MyTourStop getMyTourStop() {
		return myTourStop;
	}

	public void setListener(TourListMarkerClickListener tourListMarkerClickListener) {
		clickListener = tourListMarkerClickListener;
	}
	
	public TourListMarkerClickListener getTourListMarkerClickListener() {
		return clickListener;
	}

}
