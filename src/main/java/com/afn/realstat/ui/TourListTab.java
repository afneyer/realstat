package com.afn.realstat.ui;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.data.geo.Point;

import com.afn.realstat.MyTour;
import com.afn.realstat.TourListEntry;
import com.afn.realstat.TourListRepository;
import com.vaadin.data.provider.DataProvider;
import com.vaadin.data.provider.DataProviderWrapper;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.shared.data.sort.SortDirection;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.tapio.googlemaps.client.LatLon;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.renderers.HtmlRenderer;

public class TourListTab {

	private HorizontalLayout tourPage;
	private Grid<TourListEntry> tourListView = new Grid<TourListEntry>();
	private MyTour myTour;
	private AfnGoogleMap map;
	private Component tourListSelector;
	private TourListRepository tleRepo;

	public TourListTab(TourListRepository tleRepo) {

		this.tleRepo = tleRepo;

		// initialize tourPage
		tourPage = new HorizontalLayout();
		tourPage.setMargin(false);
		tourPage.setSizeFull();

		// HorizontalLayout tourControls = new HorizontalLayout();
		// tourControls.setMargin(false);
		
		VerticalLayout tourDisplay = new VerticalLayout();
		tourDisplay.setSizeFull();
		tourDisplay.setMargin(false);

		tourPage.addComponent(tourDisplay);
		tourPage.setExpandRatio(tourDisplay, 1.0f);

		tourListSelector = getTourSelector();
		tourDisplay.addComponent(tourListSelector);
		// TODO possibly add upload button here

		// initialize tourList view
		tourListView = new Grid<TourListEntry>();
		tourListView.addColumn(TourListEntry::htmlString, new HtmlRenderer());
		tourListView.setSelectionMode(SelectionMode.MULTI);
		tourListView.setCaption("Tour");

		tourDisplay.addComponent(tourListView);
		tourDisplay.setExpandRatio(tourListView, 1.0f);

		// initialize tourMapView
		map = new AfnGoogleMap();
		map.setCenter(new LatLon(37.83, -122.226));
		map.setZoom(13);
		map.setSizeFull();

		tourPage.addComponent(map);
		
		tourPage.setExpandRatio(map, 1.0f);
		tourPage.setExpandRatio(tourDisplay, 1.0f);
		// initialize tourList view
	}

	private Component getTourSelector() {

		// Create the selection component for tour dates
		List<Date> tlDates = tleRepo.findAllDisctintDatesNewestFirst();

		ComboBox<Date> select = new ComboBox<>("Select Date");
		select.setItems(tlDates);

		// ListSelect<Date> select = new ListSelect<Date>("Select tour date");

		select.setItemCaptionGenerator(d -> {
			SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
			String ds = df.format(d);
			return ds;
		});

		// Add some items
		select.setItems(tlDates);

		// Show 6 items and a scroll-bar if there are more
		// select.setRows(6);

		select.addSelectionListener(event -> {
			Date date = event.getValue();
			if (date != null) {
				myTour = new MyTour(date, tleRepo);
				
				ListDataProvider<TourListEntry> dataProvider =
						  DataProvider.ofCollection(myTour.getTourList());

				dataProvider.setSortOrder(TourListEntry::getPrice,
				  SortDirection.ASCENDING);

				tourListView.setDataProvider(dataProvider);
				// TODO tourListView.setItems(myTour.getTourList());
				addMarkersForTour(myTour);
			}
		});
		return select;
	}

	private void addMarkersForTour(MyTour myTour) {

		List<TourListEntry> tleList = tleRepo.findByTourDate(myTour.getTourDate());
		for (TourListEntry tle : tleList) {
			addMarkerForTourListEntry(tle, myTour);
		}
		map.centerOnTourMarkers();
		System.out.println("Done with Marking");
	}

	private TourMarker addMarkerForTourListEntry(TourListEntry tle, MyTour myTour) {
		TourMarker mrkr = null;
		Point loc = tle.getLocation();
		if (loc != null) {

			mrkr = new TourMarker(tle, myTour, map);

			map.addMarker(mrkr);

			map.addMarkerClickListener(new TourListMarkerClickListener(map, mrkr, tourListView));
		}
		return mrkr;
	}

	public Component getTourListPage() {
		return tourPage;
	}

}
