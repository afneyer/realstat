package com.afn.realstat.ui;

import com.vaadin.tapio.googlemaps.GoogleMap;
import com.vaadin.tapio.googlemaps.client.events.MarkerClickListener;
import com.vaadin.tapio.googlemaps.client.overlays.GoogleMapInfoWindow;
import com.vaadin.tapio.googlemaps.client.overlays.GoogleMapMarker;

/**
 * Listener that opens info window when a marker is clicked.
 */
public class TourListMarkerClickListener implements MarkerClickListener {

    private static final long serialVersionUID = 646386541641L;

    private final AfnGoogleMap map;
    private final MyTourListMarker marker;

    public TourListMarkerClickListener(AfnGoogleMap map,
            MyTourListMarker marker) {
        this.map = map;
        this.marker = marker;
    }

    @Override
    public void markerClicked(GoogleMapMarker clickedMarker) {
        if (clickedMarker.equals(marker)) {
            marker.toggleTour();
        }
    }

}