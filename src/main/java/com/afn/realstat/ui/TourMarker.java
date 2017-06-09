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
		mts.setMarker(this);
		this.googleMap = map;

		Point location = mts.getLocation();
		// TODO re-factor conversion from point to LatLon
		LatLon pos = new LatLon(location.getY(), location.getX());
		this.setPosition(pos);

		String cap = mts.mapMarkerCaption();
		this.setCaption(cap);

		this.setDraggable(true);
		this.setAnimationEnabled(false);
		this.blankOutUrl = new Icon( Icon.markerButtonGreen).getIconUrl();
		this.blankInUrl = new Icon( Icon.markerButtonRed).getIconUrl();
		this.setIconUrl(blankOutUrl);

	}
	/* TOTO remove
	public String getOutIconUrl() {
		return blankOutUrl;
	}
	
	public String getInIconUrl() {
		String inIconUrl = blankInUrl;
		int seq = myTourStop.getSequence();
		String text = Integer.toString(seq);
		if ( seq != 0) {
			inIconUrl = new Icon( Icon.markerButtonGreen, text ).getIconUrl();
		}
		return inIconUrl;
	}
	*/
	
	@Override
	public String getIconUrl() {
		String iconUrl = null;
	
		if (isInTour) {
			iconUrl = blankInUrl;
			int seq = myTourStop.getSequence();
			String text = Integer.toString(seq);
			if ( seq != 0) {
				iconUrl = new Icon( Icon.markerButtonRed, text ).getIconUrl();
			}
		} else {
			iconUrl = blankOutUrl;
		}
		return iconUrl;
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
			isInTour = true;
			googleMap.refresh();
		}
	}

	public void excludeFromTour() {

		if (isInTour()) {
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
