package com.afn.realstat.ui;

import org.springframework.data.geo.Point;

import com.afn.realstat.MyTour;
import com.afn.realstat.MyTourStop;
import com.afn.realstat.util.Icon;
import com.vaadin.tapio.googlemaps.client.LatLon;
import com.vaadin.tapio.googlemaps.client.overlays.GoogleMapMarker;

@SuppressWarnings("serial")
public class TourMarker extends GoogleMapMarker {

	private MyTourStop myTourStop;
	private AfnGoogleMap googleMap;
	private TourListMarkerClickListener clickListener;

	private Icon blankInIcon;
	private Icon blankOutIcon;

	public TourMarker(MyTourStop mts, AfnGoogleMap map) {
		super();

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
		this.blankInIcon = new Icon( Icon.markerButtonRed);
		this.blankOutIcon = new Icon( Icon.markerButtonGreen);
		this.setDraggable(false);

	}
	
	@Override
	public String getIconUrl() {
		
		Icon icon = null;
		if (isInTour()) {
			icon = blankInIcon;
			int seq = myTourStop.getSequence();
			String text = Integer.toString(seq);
			if ( seq != 0) {
				icon = new Icon(blankInIcon);
				icon.setText(text);
			}
		} else {
			icon = blankOutIcon;
		}
		return icon.getIconUrl();
	}

	public boolean isInTour() {
		return myTourStop.isSelected();
	}

	public void toggleTour() {
		if (isInTour()) {
			myTourStop.deselect();
		} else {
			myTourStop.select();
		}
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
