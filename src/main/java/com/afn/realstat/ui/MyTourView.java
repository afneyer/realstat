package com.afn.realstat.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.data.geo.Point;

import com.afn.realstat.MyTour;
import com.afn.realstat.MyTourStop;
import com.vaadin.data.provider.DataProvider;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.shared.data.sort.SortDirection;
import com.vaadin.tapio.googlemaps.client.LatLon;
import com.vaadin.tapio.googlemaps.client.overlays.GoogleMapPolyline;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.Notification;
import com.vaadin.ui.components.grid.MultiSelectionModel;
import com.vaadin.ui.renderers.HtmlRenderer;

public class MyTourView {

	private Grid<MyTourStop> tourListView = null;
	private MyTour myTour;
	private AfnGoogleMap map;
	private GoogleMapPolyline tourPolyLine = null;

	public MyTourView( MyTour myTour ) {
		this.myTour = myTour;
		this.map = getMapView();
	}

	public Grid<MyTourStop> getListView() {
		
		tourListView = new Grid<MyTourStop>();
		tourListView.addStyleName("h3rows");
		Grid.Column<MyTourStop, String> colPropertyInfo = tourListView.addColumn(MyTourStop::htmlString, new HtmlRenderer());
		colPropertyInfo.setWidth(350.0);
		Grid.Column<MyTourStop, String> colStringSeq = tourListView.addColumn(MyTourStop::getStringSeq);
		colStringSeq.setWidth(50);
		
		tourListView.setSelectionMode(SelectionMode.MULTI);
		tourListView.setHeight(100, Unit.PERCENTAGE);

		MultiSelectionModel<MyTourStop> selectionModel = (MultiSelectionModel<MyTourStop>) tourListView
				.getSelectionModel();
		selectionModel.addMultiSelectionListener(event -> {
			Set<MyTourStop> added = event.getAddedSelection();
			for (MyTourStop mts : added) {
				myTour.selectEntry(mts);
				TourMarker tm = map.getMarker(mts);
				if (tm != null) {
					tm.includeInTour();
				}
			}

			Set<MyTourStop> removed = event.getRemovedSelection();
			for (MyTourStop mts : removed) {
				myTour.deselectEntry(mts);
				TourMarker tm = map.getMarker(mts);
				if (tm != null) {
					tm.excludeFromTour();
				}
			}
		});

		tourListView.addItemClickListener(event -> Notification.show("Value: " + event.getItem()));
		return tourListView;
	} 
	
	public AfnGoogleMap getMapView() {
		map = new AfnGoogleMap();
		map.setCenter(new LatLon(37.83, -122.226));
		map.setZoom(13);
		return map;
	}

	public CheckBox getShowSelectedCheckBox() {
		String label = "Show Selected Only";
		CheckBox checkBox = new CheckBox(label);

		checkBox.addValueChangeListener(event -> {
			Boolean checked = event.getValue();

			if (myTour != null) {
				if (checked) {
					showSelected();
				} else {
					showAll();

				}
			}
		});

		return checkBox;
	}
		
	public void showAll() {
		
		ArrayList<MyTourStop> selected = new ArrayList<MyTourStop>(myTour.getSelected());
		ListDataProvider<MyTourStop> dataProvider = DataProvider.ofCollection(myTour.getTourList());
		dataProvider.setSortOrder(MyTourStop::getPrice, SortDirection.ASCENDING);
		tourListView.setDataProvider(dataProvider);
		
		for ( MyTourStop mts : selected ) {
			tourListView.select(mts);
		}		
		map.refresh();
		map.removeAllMarkers();
		this.addMarkersForTour();
		
	}
	
	public void showSelected() {
		
		ArrayList<MyTourStop> selected = new ArrayList<MyTourStop>(myTour.getSelected());
		ListDataProvider<MyTourStop> dataProvider = DataProvider.ofCollection(myTour.getSelected());
		dataProvider.setSortOrder(MyTourStop::getSequence, SortDirection.ASCENDING);
		tourListView.setDataProvider(dataProvider);
		
		for ( MyTourStop mts : selected ) {
			tourListView.select(mts);
		}
		map.refresh();
		hideExcludedMarkers();
	}

	public void addMarkersForTour() {

		List<MyTourStop> mtsList = myTour.getTourList();
		for (MyTourStop mts : mtsList) {
			addMarkerForTourListEntry(mts);
		}
		map.centerOnTourMarkers();
		
	}

	private TourMarker addMarkerForTourListEntry(MyTourStop mts) {
		TourMarker mrkr = null;
		Point loc = mts.getLocation();
		if (loc != null) {

			mrkr = new TourMarker(mts, map);

			map.addMarker(mrkr);

			map.addMarkerClickListener(new TourListMarkerClickListener(mrkr, tourListView));
		}
		return mrkr;
	}

	private void hideExcludedMarkers() {
		List<MyTourStop> mtsList = myTour.getTourList();
		for (MyTourStop mts : mtsList) {
			TourMarker tm = mts.getTourMarker();
			if (!tm.isInTour()) {
				map.removeMarker(mts.getTourMarker());
			}
		}
		map.centerOnTourMarkers();
	}
	
	public void setTour( MyTour tour ) {
		myTour = tour;
	}

}
