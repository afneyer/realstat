package com.afn.realstat.ui;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

// import org.hsqldb.lib.Iterator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Point;

import com.afn.realstat.AdReviewTourList;
import com.afn.realstat.Address;
import com.afn.realstat.AddressManager;
import com.afn.realstat.AddressRepository;
import com.afn.realstat.Agent;
import com.afn.realstat.AgentRepository;
import com.afn.realstat.MyTour;
import com.afn.realstat.PropertyTransactionService;
import com.afn.realstat.QAgent;
import com.afn.realstat.RealProperty;
import com.afn.realstat.TourListEntry;
import com.afn.realstat.TourListRepository;
import com.querydsl.core.types.dsl.StringPath;
import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Widgetset;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.tapio.googlemaps.client.LatLon;
import com.vaadin.tapio.googlemaps.client.events.InfoWindowClosedListener;
import com.vaadin.tapio.googlemaps.client.events.MapClickListener;
import com.vaadin.tapio.googlemaps.client.events.MapMoveListener;
import com.vaadin.tapio.googlemaps.client.overlays.GoogleMapInfoWindow;
import com.vaadin.tapio.googlemaps.client.overlays.GoogleMapMarker;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.UI;
import com.vaadin.ui.Upload;
import com.vaadin.ui.Upload.SucceededEvent;
import com.vaadin.ui.Upload.SucceededListener;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.renderers.HtmlRenderer;

/**
 * Google Maps UI for displaying properties on maps
 */
@SpringUI(path = "/maps")
@Theme("realstatTheme")
@SuppressWarnings("serial")
@Widgetset("AppWidgetset")
@SpringComponent
@Push
public class GoogleMapUI extends UI {

	@Autowired
	private PropertyTransactionService ptService;

	@Autowired
	private AgentRepository agtRepo;

	@Autowired
	private AddressManager adrMgr;

	@Autowired
	private AddressRepository adrRepo;

	private AfnGoogleMap googleMap;
	
	// private Grid<TourListEntry> tourListView;

	@Autowired
	protected TourListRepository tleRepo;

	protected CssLayout consoleLayout;
	protected MapClickListener dummyListener;

	@Autowired
	public GoogleMapUI(PropertyTransactionService ptService) {
		this.ptService = ptService;
	}

	public GoogleMapUI() {
	}

	@Override
	protected void init(VaadinRequest request) {
		CssLayout rootLayout = new CssLayout();
		rootLayout.setSizeFull();
		setContent(rootLayout);

		TabSheet tabs = new TabSheet();
		tabs.setSizeFull();
		rootLayout.addComponent(tabs);

		VerticalLayout page = new VerticalLayout();
		page.setSizeFull();
		tabs.addTab(page, "Map");
		
		TourListTab tourListTab = new TourListTab(tleRepo);
		com.vaadin.ui.Component tourListComp = tourListTab.getTourListPage();
		tabs.addTab(tourListComp, "Tour");

		HorizontalLayout mapContent = new HorizontalLayout();
		mapContent.setHeight(100, Unit.PERCENTAGE);
		mapContent.setWidth(100, Unit.PERCENTAGE);
		page.addComponent(mapContent);
		
		// tourListView = new Grid<TourListEntry>();
		// tourListView.addColumn(TourListEntry::htmlString, new HtmlRenderer());
		// tourListView.setSelectionMode(SelectionMode.MULTI);
		// tourListView.setCaption("Tour");
		// tourListView.setStyleGenerator(cellRef -> "heigher");
		// mapContent.addComponent(tourListView);

		googleMap = new AfnGoogleMap(null, null, null);

		googleMap.setCenter(new LatLon(37.83, -122.226));
		googleMap.setZoom(13);
		googleMap.setSizeFull();

		mapContent.addComponent(googleMap);
		mapContent.setExpandRatio(googleMap, 3.0f);
		page.setExpandRatio(mapContent, 4.0f);
		
		

		Panel console = new Panel();
		final CssLayout cnlt = new CssLayout();
		consoleLayout = cnlt;
		console.setContent(cnlt);

		page.addComponent(console);
		page.setExpandRatio(console, 1.0f);

		VerticalLayout buttonLayoutRow1 = new VerticalLayout();
		mapContent.addComponent(buttonLayoutRow1);
		mapContent.setExpandRatio(buttonLayoutRow1, 1.0f);

		googleMap.addMapMoveListener(new MapMoveListener() {
			@Override
			public void mapMoved(int zoomLevel, LatLon center, LatLon boundsNE, LatLon boundsSW) {
				Label consoleEntry = new Label("Map moved to (" + center.getLat() + ", " + center.getLon() + "), zoom "
						+ zoomLevel + ", boundsNE: (" + boundsNE.getLat() + ", " + boundsNE.getLon() + "), boundsSW: ("
						+ boundsSW.getLat() + ", " + boundsSW.getLon() + ")");
				cnlt.addComponent(consoleEntry, 0);
			}
		});

		googleMap.addMapClickListener(new MapClickListener() {
			@Override
			public void mapClicked(LatLon position) {
				Label consoleEntry = new Label("Map click to (" + position.getLat() + ", " + position.getLon() + ")");
				cnlt.addComponent(consoleEntry, 0);
			}
		});

		googleMap.addInfoWindowClosedListener(new InfoWindowClosedListener() {

			@Override
			public void infoWindowClosed(GoogleMapInfoWindow window) {
				Label consoleEntry = new Label("InfoWindow \"" + window.getContent() + "\" closed");
				cnlt.addComponent(consoleEntry, 0);
			}
		});

		Button showAgentDeals = new Button("Show transactions for agent", new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {

				StringPath strPath = QAgent.agent.agentName;

				Function<Agent, Boolean> addMarkers = e -> {
					addMarkersForAgentDeals(e);
					return true;
				};

				Lov<Agent> lov = new Lov<Agent>(Agent.class, agtRepo, strPath, addMarkers);
				Window popUp = lov.getPopUpWindowLov();
				UI.getCurrent().addWindow(popUp);

			}

		});
		buttonLayoutRow1.addComponent(showAgentDeals);
		showAgentDeals.setWidth(100, Unit.PERCENTAGE);

		Button tourFile = new Button("Import tour pdf-file", new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				selectAndShowPropertiesForTour();
			}
		});
		buttonLayoutRow1.addComponent(tourFile);
		tourFile.setWidth(100, Unit.PERCENTAGE);

		Button tour = new Button("Show properties on tour", new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				tourDateSelector();
			}
		});
		buttonLayoutRow1.addComponent(tour);
		tour.setWidth(100, Unit.PERCENTAGE);

		Button clearMarkersButton = new Button("Remove all markers", new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent clickEvent) {
				googleMap.clearMarkers();
			}
		});
		buttonLayoutRow1.addComponent(clearMarkersButton);
		clearMarkersButton.setWidth(100, Unit.PERCENTAGE);
	}

	protected void selectAndShowPropertiesForTour() {
		// create a window to upload a pdf-file and select a tour date
		Window popUp = new Window("Sub-window");
		VerticalLayout subContent = new VerticalLayout();
		popUp.setContent(subContent);

		// Put some components in it
		subContent.addComponent(new Label("Tour Upload and Date Selection"));
		FileUpLoader receiver = new FileUpLoader(adrRepo, tleRepo);

		Upload upload = new Upload("Select AdReview Tour Pdf-File", receiver);
		subContent.addComponent(upload);

		// Center it in the browser window
		popUp.center();

		// Open it in the UI
		addWindow(popUp);

	}

	protected void tourDateSelector() {
		// create a window to upload a pdf-file and select a tour date
		Window popUp = new Window("Sub-window");
		VerticalLayout subContent = new VerticalLayout();
		popUp.setContent(subContent);

		

		// Center it in the browser window
		popUp.center();

		// Open it in the UI
		addWindow(popUp);
		// Put some components in it
		// subContent.addComponent(new Label("Select Tour Date"));

		// Create the selection component for tour dates
		List<Date> tlDates = tleRepo.findAllDisctintDatesNewestFirst();

		ListSelect<Date> select = new ListSelect<Date>("Select tour date");
		
		subContent.addComponent(select);

		select.setItemCaptionGenerator(d -> {
			SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
	        String ds = df.format(d);
	        return ds;
		} );

		// Add some items
		select.setItems(tlDates);

		// Show 6 items and a scroll-bar if there are more
		select.setRows(6);

		select.addValueChangeListener(event -> {
			Set<Date> dates = event.getValue();
			Date date = dates.iterator().next();
			if (date != null) {
				MyTour myTour = new MyTour(date, tleRepo);
				// tourListView.setItems(myTour.getTourList());
				addMarkersForTour(myTour);
				popUp.close();
			}

		});

	}

	private void addMarkersForTour(MyTour myTour) {

		List<TourListEntry> tleList = tleRepo.findByTourDate(myTour.getTourDate());
		for (TourListEntry tle : tleList) {
			addMarkerForTourListEntry(tle, myTour);
		}
		googleMap.centerOnTourMarkers();
		System.out.println("Done with Marking");
	}

	private TourMarker addMarkerForTourListEntry(TourListEntry tle, MyTour myTour) {
			TourMarker mrkr = null;
			Point loc = tle.getLocation();
			if (loc != null) {
				 
				mrkr = new TourMarker(tle, myTour, googleMap);

				googleMap.addMarker(mrkr);

				// googleMap.addMarkerClickListener(new TourListMarkerClickListener(googleMap, mrkr, tourListView));
			}
		return mrkr;
	}

	// Make this null safe TODO

	protected void addMarkersForAgentDeals(Agent agt) {
		List<RealProperty> rpList = ptService.getRealPropertiesForAgent(agt.getLicense());

		// TODO change to the following
		// List<RealProperty> rpList = ptService.getRealPropertiesForAgent(agt);
		for (RealProperty rp : rpList) {
			Address adr = rp.getPropertyAdr();
			GoogleMapMarker mrkr = addMarkerForAddress(adr);
			String text = "Testing Info Window";
			new GoogleMapInfoWindow(text, mrkr);
		}
		// }

		googleMap.centerOnMarkers();
		System.out.println("Done with Marking");

	}

	protected void addMarkersForAddresses(List<Address> list) {

		// TODO change to the following
		// List<RealProperty> rpList = ptService.getRealPropertiesForAgent(agt);
		for (Address adr : list) {
			addMarkerForAddress(adr);
		}
		// }

		googleMap.centerOnMarkers();
		System.out.println("Done with Marking");

	}

	protected void addMarkersForTourList(List<TourListEntry> list) {

		// TODO change to the following
		// List<RealProperty> rpList = ptService.getRealPropertiesForAgent(agt);
		for (TourListEntry te : list) {
			Address adr = te.getPropertyAdr();
			addMarkerForAddress(adr);
		}
		// }

		googleMap.centerOnTourMarkers();
		System.out.println("Done with Marking");

	}

	private GoogleMapMarker addMarkerForAddress(Address adr) {
		Point loc = adrMgr.getLocation(adr);
		if (adr != null) {
			if (loc != null) {
				GoogleMapMarker mrkr = new GoogleMapMarker("testCaption", new LatLon(loc.getY(), loc.getX()), false);
				googleMap.addMarker(mrkr);
				googleMap.markAsDirty();
				return mrkr;
			}
		}
		return null;
	}

	
	

}