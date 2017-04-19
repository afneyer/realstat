package com.afn.realstat.ui;

import org.springframework.data.geo.Point;

import com.afn.realstat.MyTour;
import com.afn.realstat.TourListEntry;
import com.vaadin.tapio.googlemaps.client.LatLon;
import com.vaadin.tapio.googlemaps.client.overlays.GoogleMapMarker;

@SuppressWarnings("serial")
public class MyTourListMarker extends GoogleMapMarker {
	
	private final String inIconUrl = "VAADIN/house-32Red.ico";
	private final String outIconUrl = "VAADIN/star32.png";
	private MyTour myTour;
	private TourListEntry tourListEntry;
	private AfnGoogleMap googleMap;
	private TourListMarkerClickListener clickListener;

	public MyTourListMarker() {
		// TODO Auto-generated constructor stub
	}

	public MyTourListMarker(TourListEntry tle, MyTour mt, AfnGoogleMap map) {
		super();
		
		this.myTour = mt;
		this.tourListEntry = tle;
		this.googleMap = map;
		
		Point location = tle.getLocation();
		LatLon pos = new LatLon(location.getY(),location.getX());
		this.setPosition(pos);
		
		String cap = tle.getPrice() + "\n" + tle.getDescription();
		this.setCaption(cap);
		
		this.setDraggable(true);
		this.setAnimationEnabled(false);
		this.setIconUrl(outIconUrl);
		
	}
	
	public void toggleTour() {
		if (myTour.isSelected(tourListEntry)) {
			excludeFromTour();
		} else {
			includeInTour();
		}
	}
	
	public void includeInTour() {
		// change icon
		setIconUrl(inIconUrl);
		// add/remove from tour
		myTour.selectEntry(tourListEntry);
		refresh();
	}
	
	public void excludeFromTour() {
		// change icon
		setIconUrl(outIconUrl);
		// add/remove from tour
		myTour.deselectEntry(tourListEntry);
		refresh();
	}
	
	public void refresh() {
		MyTourListMarker newMarker = this.getCopy();
		googleMap.removeMarkerClickListener(clickListener);
		googleMap.removeMarker(this);
		googleMap.addMarker(newMarker);
		googleMap.addMarkerClickListener(new TourListMarkerClickListener(googleMap, newMarker, clickListener.getTourListView()));
	}
	
	private MyTourListMarker getCopy() {
		MyTourListMarker tlm = new MyTourListMarker();
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

	public void setListener(TourListMarkerClickListener tourListMarkerClickListener) {
		clickListener = tourListMarkerClickListener;
	}

}
