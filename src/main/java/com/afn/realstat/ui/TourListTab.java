package com.afn.realstat.ui;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.data.geo.Point;

import com.afn.realstat.AdReviewTourList;
import com.afn.realstat.Address;
import com.afn.realstat.AddressRepository;
import com.afn.realstat.MyTour;
import com.afn.realstat.TourListEntry;
import com.afn.realstat.TourListRepository;
import com.afn.realstat.framework.AppContext;
import com.afn.realstat.util.GeoLocation;
import com.afn.realstat.util.MapDirection;
import com.google.maps.model.EncodedPolyline;
import com.vaadin.data.provider.DataProvider;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.server.Sizeable.Unit;
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
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.Upload;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.components.grid.MultiSelectionModel;
import com.vaadin.ui.renderers.HtmlRenderer;

public class TourListTab {

	private HorizontalLayout tourPage;
	private Grid<TourListEntry> tourListView = null;
	private MyTour myTour;
	private AfnGoogleMap map;
	private Component tourListSelector;
	private HorizontalLayout tourDisplayControl;
	private TourListRepository tleRepo;
	private AddressRepository adrRepo;
	private GoogleMapPolyline tourPolyLine = null;

	public TourListTab(TourListRepository tleRepo) {

		this.tleRepo = tleRepo;
		this.adrRepo = Address.getRepo();

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

		tourDisplayControl = new HorizontalLayout();
		tourDisplay.addComponent(tourDisplayControl);

		tourListSelector = getTourSelector();
		tourDisplayControl.addComponent(tourListSelector);

		Button routeTour = new Button("Sequence Tour", (ClickListener) event -> {
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
			tourListView.getDataProvider().refreshAll();
			tourPolyLine = GeoLocation.convert(polyline);
			map.addPolyline(tourPolyLine);

		});
		tourDisplayControl.addComponent(routeTour);

		@SuppressWarnings("serial")
		Button tourFile = new Button("Import tour pdf-file", new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				selectAndShowPropertiesForTour();
			}
		});
		tourDisplayControl.addComponent(tourFile);
		tourFile.setWidth(100, Unit.PERCENTAGE);
		
	

		// initialize tourList view
		tourListView = new Grid<TourListEntry>();
		tourListView.addStyleName("h3rows");
		// tourListView.setStyleGenerator(cellRef -> "tourlistcell");
		Grid.Column<TourListEntry, String> propInfo = tourListView.addColumn(TourListEntry::htmlString,
				new HtmlRenderer());
		tourListView.addColumn(TourListEntry::getStringSeq);
		// propInfo.setStyleGenerator(cellRef -> "tourlistcell");
		propInfo.setWidth(400.0);
		// Grid.Column<TourListEntry,String> sequence =
		// tourListView.addColumn(TourListEntry::sequence);

		tourListView.setSelectionMode(SelectionMode.MULTI);
		tourListView.setSizeFull();

		// tourListView.setCaption("Tour");

		MultiSelectionModel<TourListEntry> selectionModel = (MultiSelectionModel<TourListEntry>) tourListView
				.getSelectionModel();
		selectionModel.addMultiSelectionListener(event -> {
			Set<TourListEntry> added = event.getAddedSelection();
			for (TourListEntry tle : added) {
				myTour.selectEntry(tle);
				TourMarker tm = map.getMarker(tle);
				if (tm != null) {
					tm.includeInTour();
				}
			}

			Set<TourListEntry> removed = event.getRemovedSelection();
			for (TourListEntry tle : removed) {
				myTour.deselectEntry(tle);
				TourMarker tm = map.getMarker(tle);
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

				ListDataProvider<TourListEntry> dataProvider = DataProvider.ofCollection(myTour.getTourList());

				dataProvider.setSortOrder(TourListEntry::getPrice, SortDirection.ASCENDING);

				tourListView.setDataProvider(dataProvider);
				// TODO tourListView.setItems(myTour.getTourList());
				addMarkersForTour(myTour);
				
				Button printTour = new ShowPdfButton( myTour.getPdfFile() );
				tourDisplayControl.addComponent(printTour);
				printTour.setWidth(100, Unit.PERCENTAGE);
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

	protected void selectAndShowPropertiesForTour() {

		// create a window to upload a pdf-file
		Window popUp = new Window("Sub-window");
		VerticalLayout subContent = new VerticalLayout();
		popUp.setContent(subContent);

		// Put some components in it
		subContent.addComponent(new Label("Tour Upload and Date Selection"));

		FileUploader receiver = new FileUploader();
		receiver.setAfterUploadSucceeded(new AfterUploadSucceeded() {
			@Override
			public void afterUploadSucceeded(FileUploader fileUploader) {
				AdReviewTourList adReview = new AdReviewTourList(fileUploader.getFile());
				adReview.createTourList();
				System.out.println("TourList Done");
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
