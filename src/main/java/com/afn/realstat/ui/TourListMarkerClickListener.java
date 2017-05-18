package com.afn.realstat.ui;

import com.afn.realstat.MyTourStop;
import com.vaadin.tapio.googlemaps.client.events.MarkerClickListener;
import com.vaadin.tapio.googlemaps.client.overlays.GoogleMapMarker;
import com.vaadin.ui.Grid;

/**
 * Listener that opens info window when a marker is clicked.
 */
public class TourListMarkerClickListener implements MarkerClickListener {

	private static final long serialVersionUID = 646386541641L;

	// TODO remove
	// private final AfnGoogleMap map;
	private final TourMarker marker;
	private final Grid<MyTourStop> tourListView;

	public TourListMarkerClickListener(TourMarker marker, Grid<MyTourStop> tourListView) {
		// TODO remove
		// this.map = map;
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
			MyTourStop mts = marker.getMyTourStop();
			if (added) {
				tourListView.select(mts);
			} else {
				tourListView.deselect(mts);
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

	public Grid<MyTourStop> getTourListView() {
		return tourListView;
	}
}