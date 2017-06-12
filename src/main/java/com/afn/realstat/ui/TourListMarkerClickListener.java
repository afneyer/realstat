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

		this.marker = marker;
		this.tourListView = tourListView;
		marker.setListener(this);
	}

	@Override
	public void markerClicked(GoogleMapMarker clickedMarker) {
		if (clickedMarker.equals(marker)) {

			// change the marker and add it to the selected list of MyTour
			marker.toggleTour();
			tourListView.getDataProvider().refreshAll();
		}
	}

	public Grid<MyTourStop> getTourListView() {
		return tourListView;
	}
}