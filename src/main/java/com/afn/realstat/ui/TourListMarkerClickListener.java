package com.afn.realstat.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import com.afn.realstat.MyTour;
import com.afn.realstat.TourListEntry;
import com.vaadin.tapio.googlemaps.GoogleMap;
import com.vaadin.tapio.googlemaps.client.events.MarkerClickListener;
import com.vaadin.tapio.googlemaps.client.overlays.GoogleMapInfoWindow;
import com.vaadin.tapio.googlemaps.client.overlays.GoogleMapMarker;
import com.vaadin.ui.Grid;
import com.vaadin.ui.UI;

/**
 * Listener that opens info window when a marker is clicked.
 */
public class TourListMarkerClickListener implements MarkerClickListener {

	private static final long serialVersionUID = 646386541641L;

	private final AfnGoogleMap map;
	private final TourMarker marker;
	private final Grid<TourListEntry> tourListView;

	public TourListMarkerClickListener(AfnGoogleMap map, TourMarker marker, Grid<TourListEntry> tourListView) {
		this.map = map;
		this.marker = marker;
		this.tourListView = tourListView;
		marker.setListener(this);
	}

	@Override
	public void markerClicked(GoogleMapMarker clickedMarker) {
		if (clickedMarker.equals(marker)) {

			// change the marker and add it to the selected list of MyTour
			boolean added = marker.toggleTour();

			// select or de-select it from the tourListView
			TourListEntry tle = marker.getTourListEntry();
			if (added) {
				tourListView.select(tle);
			} else {
				tourListView.deselect(tle);
			}
			// tourListView.markAsDirtyRecursive();
			tourListView.getDataProvider().refreshAll();
			
			
/*
			UI ui = tourListView.getUI();
			ui.accessSynchronously(new Runnable() {
				@Override
				public void run() {
					if (added) {
						tourListView.select(tle);
					} else {
						tourListView.deselect(tle);
					}
					tourListView.markAsDirtyRecursive();
					tourListView.getDataProvider().refreshAll();
				}
			});
*/
		}
	}

	public Grid<TourListEntry> getTourListView() {
		return tourListView;
	}
}