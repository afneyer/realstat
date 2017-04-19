package com.afn.realstat.ui;

import com.afn.realstat.TourListEntry;
import com.vaadin.tapio.googlemaps.GoogleMap;
import com.vaadin.tapio.googlemaps.client.events.MarkerClickListener;
import com.vaadin.tapio.googlemaps.client.overlays.GoogleMapInfoWindow;
import com.vaadin.tapio.googlemaps.client.overlays.GoogleMapMarker;
import com.vaadin.ui.Grid;

/**
 * Listener that opens info window when a marker is clicked.
 */
public class TourListMarkerClickListener implements MarkerClickListener {

    private static final long serialVersionUID = 646386541641L;

    private final AfnGoogleMap map;
    private final MyTourListMarker marker;
    private final Grid<TourListEntry> tourListView;

    public TourListMarkerClickListener(AfnGoogleMap map,
            MyTourListMarker marker, Grid<TourListEntry> tourListView) {
        this.map = map;
        this.marker = marker;
        this.tourListView = tourListView;
        marker.setListener(this);
    }

    @Override
    public void markerClicked(GoogleMapMarker clickedMarker) {
        if (clickedMarker.equals(marker)) {
            marker.toggleTour();
            tourListView.setItems(marker.getTour().getSelected());
        }
    }

    public Grid<TourListEntry> getTourListView() {
    	return tourListView;
    }
}