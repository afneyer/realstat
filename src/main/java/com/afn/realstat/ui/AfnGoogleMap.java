package com.afn.realstat.ui;

import java.util.Collection;

import com.vaadin.tapio.googlemaps.GoogleMap;
import com.vaadin.tapio.googlemaps.client.LatLon;
import com.vaadin.tapio.googlemaps.client.events.MapClickListener;
import com.vaadin.ui.TextField;

@SuppressWarnings("serial")
public class AfnGoogleMap extends GoogleMap {

	private MapClickListener dummyListener;

	public AfnGoogleMap(String apiKey, String clientId, String language) {
		super(apiKey, clientId, language);
		
		dummyListener = new MapClickListener() {

			@Override
			public void mapClicked(LatLon position) {
				// do nothing
			}
		};
	}

	protected void refresh() {
	
		// Collection<MapClickListener> listeners = (Collection<MapClickListener>) inMap
		// 		.getListeners(MapClickListener.class);
		this.addComponent( new TextField("test"));
		this.addMapClickListener(dummyListener);
	}
}
