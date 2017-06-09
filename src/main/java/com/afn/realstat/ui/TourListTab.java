package com.afn.realstat.ui;

import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.data.geo.Point;

import com.afn.realstat.AdReviewTourList;
import com.afn.realstat.Address;
import com.afn.realstat.MyTour;
import com.afn.realstat.MyTourStop;
import com.afn.realstat.TourListEntry;
import com.afn.realstat.TourListRepository;
import com.afn.realstat.ui.FileUploader.AfterUploadSucceeded;
import com.afn.realstat.util.GeoLocation;
import com.afn.realstat.util.MapDirection;
import com.google.maps.model.EncodedPolyline;
import com.vaadin.data.provider.DataProvider;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.shared.data.sort.SortDirection;
import com.vaadin.tapio.googlemaps.client.LatLon;
import com.vaadin.tapio.googlemaps.client.overlays.GoogleMapPolyline;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Upload;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.components.grid.MultiSelectionModel;
import com.vaadin.ui.renderers.HtmlRenderer;

public class TourListTab {

	private HorizontalLayout tourPage;
	private Grid<MyTourStop> tourListView = null;
	private MyTour myTour;
	private AfnGoogleMap map;
	private Component tourListSelector;
	private HorizontalLayout tourDisplayControl;

	private Button routeTourButton = null;
	private ShowPdfButton printTourButton = null;

	private GoogleMapPolyline tourPolyLine = null;

	public TourListTab() {

		// initialize tourPage
		tourPage = new HorizontalLayout();
		tourPage.setMargin(false);
		tourPage.setSizeFull();

		VerticalLayout tourDisplay = new VerticalLayout();
		tourDisplay.setSizeFull();
		tourDisplay.setMargin(false);

		tourPage.addComponent(tourDisplay);
		tourPage.setExpandRatio(tourDisplay, 1.0f);

		tourDisplayControl = new HorizontalLayout();
		tourDisplay.addComponent(tourDisplayControl);

		tourListSelector = getTourSelector();
		tourDisplayControl.addComponent(tourListSelector);

		routeTourButton = getRouteTourButton();
		tourDisplayControl.addComponent(routeTourButton);

		printTourButton = getPrintPdfButton();
		tourDisplayControl.addComponent(printTourButton);

		@SuppressWarnings("serial")
		Button tourFile = new Button("Import", new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				selectAndShowPropertiesForTour();
			}
		});
		tourDisplayControl.addComponent(tourFile);
		// tourFile.setWidth(100, Unit.PERCENTAGE);
		tourFile.setDescription("testDescription");

		// initialize tourList view
		tourListView = new Grid<MyTourStop>();
		tourListView.addStyleName("h3rows");
		Grid.Column<MyTourStop, String> propInfo = tourListView.addColumn(MyTourStop::htmlString, new HtmlRenderer());
		tourListView.addColumn(MyTourStop::getStringSeq);
		propInfo.setWidth(400.0);

		tourListView.setSelectionMode(SelectionMode.MULTI);
		tourListView.setSizeFull();

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
		TourListRepository tleRepo = TourListEntry.getRepo();
		List<Date> tlDates = tleRepo.findAllDisctintDatesNewestFirst();

		ComboBox<Date> select = new ComboBox<>("Select Date");
		select.setItems(tlDates);

		select.setItemCaptionGenerator(d -> {
			SimpleDateFormat df = new SimpleDateFormat("MMM dd, yyyy");
			String ds = df.format(d);
			return ds;
		});

		// Add the dates
		select.setItems(tlDates);

		select.addSelectionListener(event -> {
			Date date = event.getValue();
			if (date != null) {
				
				map.removeAllMarkers();
				myTour = new MyTour(date);

				ListDataProvider<MyTourStop> dataProvider = DataProvider.ofCollection(myTour.getTourList());

				dataProvider.setSortOrder(MyTourStop::getPrice, SortDirection.ASCENDING);

				tourListView.setDataProvider(dataProvider);
				addMarkersForTour(myTour);

				printTourButton.setPdfFileGetter(myTour::getPdfFile);
				printTourButton.setEnabled(true);
				routeTourButton.setEnabled(true);

			}
		});
		return select;
	}

	private Button getRouteTourButton() {
		Button routeTour = new Button("Route", (ClickListener) event -> {
			if (tourPolyLine != null) {
				map.removePolyline(tourPolyLine);
				myTour.clearSequence();
			}
			Address start = new Address("4395 Piedmont Ave. #309", "Oakland", "94611");
			Address end = start;

			// move this into myTour;
			List<Address> routeList = myTour.getSelectedAddresses();
			MapDirection dir = new MapDirection(start, end, routeList);
			EncodedPolyline polyline = dir.route(myTour.getTourDate());
			int seq[] = dir.getWaypointSequence();
			myTour.setSequence(seq);
	        map.refresh();
			// todo not needed?
			// tourListView.getDataProvider().refreshAll();
			tourPolyLine = GeoLocation.convert(polyline);
			tourPolyLine.setStrokeColor("#C7320D");
			tourPolyLine.setStrokeWeight(5);
			map.addPolyline(tourPolyLine);
		});

		routeTour.setDescription("Click to route tour");
		routeTour.setEnabled(false);
		return routeTour;
	}

	private ShowPdfButton getPrintPdfButton() {
		ShowPdfButton showPdf = new ShowPdfButton();
		showPdf.setCaption("Print");
		showPdf.setDescription("Click to print the selected tour");
		showPdf.setEnabled(false);
		return showPdf;
	};

	private void addMarkersForTour(MyTour myTour) {

		List<MyTourStop> mtsList = myTour.getTourList();
		for (MyTourStop mts : mtsList) {
			addMarkerForTourListEntry(mts);
		}
		map.centerOnTourMarkers();
		System.out.println("Done with Marking");
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

	protected void selectAndShowPropertiesForTour() {

		// create a window to upload a pdf-file
		Window popUp = new Window("Import AdReview");
		VerticalLayout subContent = new VerticalLayout();
		popUp.setContent(subContent);

		// Put some components in it
		// subContent.addComponent(new Label("Tour Upload and Date Selection"));

		FileUploader receiver = new FileUploader();
		receiver.setAfterUploadSucceeded(new AfterUploadSucceeded() {
			@Override
			public void afterUploadSucceeded(FileUploader fileUploader) {
				AdReviewTourList adReview = new AdReviewTourList(fileUploader.getFile());
				adReview.createTourList();
				Notification.show("Upload Complete",
						"AdReview has been uploaded, please select the date for your tour.",
						Notification.Type.HUMANIZED_MESSAGE);
				popUp.close();
			}
		}

		);
		Upload upload = new Upload("Select AdReview Tour Pdf-File", receiver);
		upload.addSucceededListener(receiver);
		subContent.addComponent(upload);

		// Center it in the browser window
		popUp.center();

		tourPage.getUI().addWindow(popUp);

	}

	public Component getTourListPage() {
		return tourPage;
	}

}
