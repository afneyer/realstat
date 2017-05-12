package com.afn.realstat.ui;

import org.springframework.data.geo.Point;

import com.afn.realstat.MyTour;
import com.afn.realstat.TourListEntry;
import com.vaadin.tapio.googlemaps.client.LatLon;
import com.vaadin.tapio.googlemaps.client.overlays.GoogleMapMarker;

@SuppressWarnings("serial")
public class TourMarker extends GoogleMapMarker {
	
	private final String inIconUrl = "VAADIN/house-32Red.ico";
	private final String outIconUrl = "VAADIN/star32.png";
	private MyTour myTour;
	private TourListEntry tourListEntry;
	private AfnGoogleMap googleMap;
	private TourListMarkerClickListener clickListener;

	public TourMarker() {
		// TODO Auto-generated constructor stub
	}

	public TourMarker(TourListEntry tle, MyTour mt, AfnGoogleMap map) {
		super();
		
		this.myTour = mt;
		this.tourListEntry = tle;
		this.googleMap = map;
		
		Point location = tle.getLocation();
		LatLon pos = new LatLon(location.getY(),location.getX());
		this.setPosition(pos);
		
		String cap = tle.mapMarkerCaption();
		this.setCaption(cap);
		
		this.setDraggable(true);
		this.setAnimationEnabled(false);
		this.setIconUrl(outIconUrl);
		
	}
	
	public boolean isInTour() {
		if (getIconUrl().equals(inIconUrl)) {
			return true;
		} 
		return false;
	}
	
	public boolean toggleTour() {
		if (isInTour()) {
			excludeFromTour();
			return false;
		} else {
			includeInTour();
			return true;
		}
	}
	
	public void includeInTour() {
		// change icon
		if (!isInTour()) {
			setIconUrl(inIconUrl);
			googleMap.refresh();
		}
	}
	
	public void excludeFromTour() {
		
		if (isInTour()) {
		// change icon
			setIconUrl(outIconUrl);
			googleMap.refresh();
		}
	}
	
	
	// todo remove
	public void refresh() {
		TourMarker newMarker = this.getCopy();
		googleMap.removeMarkerClickListener(clickListener);
		googleMap.removeMarker(this);
		googleMap.addMarker(newMarker);
		googleMap.addMarkerClickListener(new TourListMarkerClickListener(googleMap, newMarker, clickListener.getTourListView()));
	}
	
	// todo remove
	private TourMarker getCopy() {
		TourMarker tlm = new TourMarker();
		tlm.googleMap = googleMap;
		tlm.myTour = myTour;
		tlm.tourListEntry = tourListEntry;
		tlm.setAnimationEnabled(this.isAnimationEnabled());
		tlm.setCaption(this.getCaption());
		tlm.setDraggable(this.isDraggable());
		tlm.setIconUrl(this.getIconUrl());
		tlm.setOptimized(this.isOptimized());
		tlm.setPosition(this.getPosition());
		return tlm;
	}
	
	public MyTour getTour() {
		return myTour;
	}
	
	public TourListEntry getTourListEntry() {
		return tourListEntry;
	}

	public void setListener(TourListMarkerClickListener tourListMarkerClickListener) {
		clickListener = tourListMarkerClickListener;
	}

}
