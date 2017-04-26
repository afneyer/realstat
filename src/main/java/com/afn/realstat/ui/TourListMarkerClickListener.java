package com.afn.realstat.ui;

import java.util.ArrayList;
import java.util.List;

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
    private final TourMarker marker;
    private final Grid<TourListEntry> tourListView;

    public TourListMarkerClickListener(AfnGoogleMap map,
            TourMarker marker, Grid<TourListEntry> tourListView) {
        this.map = map;
        this.marker = marker;
        this.tourListView = tourListView;
        marker.setListener(this);
    }

    @Override
    public void markerClicked(GoogleMapMarker clickedMarker) {
        if (clickedMarker.equals(marker)) {
            marker.toggleTour();
            // select all the items that are part of the TourListView
            // TODO remove: tourListView.setItems(marker.getTour().getSelected());
            List<TourListEntry> selected = new ArrayList<TourListEntry>(marker.getTour().getSelected());
            for ( TourListEntry tle : selected) {
            	tourListView.select(tle);
            }
            tourListView.markAsDirtyRecursive();
            tourListView.getDataProvider().refreshAll();
            marker.refresh();
        }
    }

    public Grid<TourListEntry> getTourListView() {
    	return tourListView;
    }
}