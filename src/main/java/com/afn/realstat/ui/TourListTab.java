package com.afn.realstat.ui;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.shared.data.sort.SortDirection;
import com.vaadin.tapio.googlemaps.client.overlays.GoogleMapPolyline;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Upload;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class TourListTab {

	private HorizontalLayout tourPage;
	private Grid<MyTourStop> tourListView = null;
	private MyTour myTour;
	private MyTourView myTourView;
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
		tourDisplay.setHeight(100, Unit.PERCENTAGE);
		tourDisplay.setWidth(460, Unit.PIXELS);
		tourDisplay.setMargin(false);

		tourPage.addComponent(tourDisplay);

		tourDisplayControl = new HorizontalLayout();
		tourDisplayControl.setMargin(false);
		tourDisplay.addComponent(tourDisplayControl);

		tourListSelector = getTourSelector();
		tourDisplayControl.addComponent(tourListSelector);

		VerticalLayout tourControls = new VerticalLayout();
		tourControls.setMargin(false);
		tourDisplayControl.addComponent(tourControls);

		HorizontalLayout tourControlButtons = new HorizontalLayout();
		tourControlButtons.setMargin(false);
		tourControls.addComponent(tourControlButtons);

		HorizontalLayout tourOptions = new HorizontalLayout();
		tourOptions.setMargin(false);
		tourControls.addComponent(tourOptions);

		CheckBox showRouted = getShowSelectedCheckBox();
		tourOptions.addComponent(showRouted);

		routeTourButton = getRouteTourButton();
		tourControlButtons.addComponent(routeTourButton);

		printTourButton = getPrintPdfButton();
		tourControlButtons.addComponent(printTourButton);

		Button importTourFile = getImportTourFileButton();

		tourControlButtons.addComponent(importTourFile);
		importTourFile.setDescription("testDescription");

		// initialize tourList view
		myTour = new MyTour(new Date());
		myTourView = new MyTourView(myTour);
		tourListView = myTourView.createListView();
		tourListView.setHeight(100, Unit.PERCENTAGE);
		tourListView.setWidth(460, Unit.PIXELS);

		tourDisplay.addComponent(tourListView);
		tourDisplay.setExpandRatio(tourListView, 1.0f);

		// initialize tourMapView
		map = myTourView.getMapView();
		map.setSizeFull();

		tourPage.addComponent(map);
		tourPage.setExpandRatio(map, 1.0f);
		// tourPage.setExpandRatio(tourDisplay, 1.0f);
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
				if (tourPolyLine != null) {
					map.removePolyline(tourPolyLine);
					if (myTour != null) {
						myTour.clearSequence();
					}
				}
				myTour = new MyTour(date);
				
				// replace the tour on the view with the new one
				myTourView.setTour(myTour);
			
				// TODO move to view
				ListDataProvider<MyTourStop> dataProvider = DataProvider.ofCollection(myTour.getTourList());
				dataProvider.setSortOrder(MyTourStop::getPrice, SortDirection.ASCENDING);
				tourListView.setDataProvider(dataProvider);

				myTourView.addMarkersForTour();
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

			tourPolyLine = GeoLocation.convert(polyline);
			tourPolyLine.setStrokeColor("#C7320D");
			tourPolyLine.setStrokeWeight(4);
			map.addPolyline(tourPolyLine);
		});

		routeTour.setDescription("Click to route tour");
		routeTour.setEnabled(false);
		return routeTour;
	}

	private Button getImportTourFileButton() {
		@SuppressWarnings("serial")
		Button tourFile = new Button("Import", new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				importAdReviewPdf();
			}
		});
		return tourFile;
	}

	private ShowPdfButton getPrintPdfButton() {
		ShowPdfButton showPdf = new ShowPdfButton();
		showPdf.setCaption("Print");
		showPdf.setDescription("Click to print the selected tour");
		showPdf.setEnabled(false);
		return showPdf;
	};

	private CheckBox getShowSelectedCheckBox() {
		String label = "Show Selected Only";
		CheckBox checkBox = new CheckBox(label);

		checkBox.addValueChangeListener(event -> {
			Boolean checked = event.getValue();

			if (myTour != null) {
				if (checked) {
					myTourView.showSelected();
				} else {
					myTourView.showAll();

				}
			}
		});
		return checkBox;
	}


	/* TODO remove

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
	
	*/

	protected void importAdReviewPdf() {

		// create a window to upload a pdf-file
		Window popUp = new Window("Import AdReview");
		VerticalLayout subContent = new VerticalLayout();
		popUp.setContent(subContent);

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
