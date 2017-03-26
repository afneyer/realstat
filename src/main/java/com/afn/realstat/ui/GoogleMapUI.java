package com.afn.realstat.ui;

import java.util.List;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Point;
import org.springframework.stereotype.Component;

import com.afn.realstat.Address;
import com.afn.realstat.AddressManager;
import com.afn.realstat.Agent;
import com.afn.realstat.AgentRepository;
import com.afn.realstat.PropertyTransactionService;
import com.afn.realstat.QAgent;
import com.afn.realstat.RealProperty;
import com.afn.realstat.util.MapLocation;
import com.querydsl.core.types.dsl.StringPath;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Widgetset;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.tapio.googlemaps.GoogleMap;
import com.vaadin.tapio.googlemaps.client.GoogleMapControl;
import com.vaadin.tapio.googlemaps.client.LatLon;
import com.vaadin.tapio.googlemaps.client.events.InfoWindowClosedListener;
import com.vaadin.tapio.googlemaps.client.events.MapClickListener;
import com.vaadin.tapio.googlemaps.client.events.MapMoveListener;
import com.vaadin.tapio.googlemaps.client.events.MarkerDragListener;
import com.vaadin.tapio.googlemaps.client.layers.GoogleMapKmlLayer;
import com.vaadin.tapio.googlemaps.client.overlays.GoogleMapInfoWindow;
import com.vaadin.tapio.googlemaps.client.overlays.GoogleMapMarker;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * Google Maps UI for displaying properties on maps
 */
@SpringUI(path = "/maps")
@Theme("valo")
@SuppressWarnings("serial")
@Widgetset("AppWidgetset")
@Component
public class GoogleMapUI extends UI {

	@Autowired
	private PropertyTransactionService ptService;

	@Autowired
	private AgentRepository agtRepo;

	@Autowired
	private AddressManager adrMgr;
	private GoogleMap googleMap;
	

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
		tabs.addTab(new Label("An another tab"), "The other tab");

		HorizontalLayout mapContent = new HorizontalLayout();
		mapContent.setHeight(100, Unit.PERCENTAGE);
		mapContent.setWidth(100, Unit.PERCENTAGE);
		page.addComponent(mapContent);

		googleMap = new GoogleMap(null, null, null);

		googleMap.setCenter(new LatLon(37.83, -122.226));
		googleMap.setZoom(13);
		googleMap.setHeight(100, Unit.PERCENTAGE);
		googleMap.setWidth(100, Unit.PERCENTAGE);

		mapContent.addComponent(googleMap);
		mapContent.setExpandRatio(googleMap, 3.0f);
		page.setExpandRatio(mapContent, 4.0f);

		Panel console = new Panel();
		final CssLayout consoleLayout = new CssLayout();
		console.setContent(consoleLayout);
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
				consoleLayout.addComponent(consoleEntry, 0);
			}
		});

		googleMap.addMapClickListener(new MapClickListener() {
			@Override
			public void mapClicked(LatLon position) {
				Label consoleEntry = new Label("Map click to (" + position.getLat() + ", " + position.getLon() + ")");
				consoleLayout.addComponent(consoleEntry, 0);
			}
		});

		googleMap.addInfoWindowClosedListener(new InfoWindowClosedListener() {

			@Override
			public void infoWindowClosed(GoogleMapInfoWindow window) {
				Label consoleEntry = new Label("InfoWindow \"" + window.getContent() + "\" closed");
				consoleLayout.addComponent(consoleEntry, 0);
			}
		});

		Button showAgentDeals = new Button("Show Transactions for Agent", new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {

				StringPath strPath = QAgent.agent.agentName;
				
				Function<Agent, Boolean> addMarkers = e -> {
					addMarkersForAgentDeals(e);
					return true;
				};
				
				Lov<Agent> lov = new Lov<Agent>(Agent.class, agtRepo, strPath, addMarkers);
				Window popUp = lov.getPopUpWindowLov();
				UI.getCurrent().addWindow(lov.getPopUpWindowLov());

			}

		});
		buttonLayoutRow1.addComponent(showAgentDeals);
		showAgentDeals.setWidth(100, Unit.PERCENTAGE);

		Button limitCenterButton = new Button("Limit center between (60.619324, 22.712753), (60.373484, 21.945083)",
				new Button.ClickListener() {
					@Override
					public void buttonClick(ClickEvent event) {
						googleMap.setCenterBoundLimits(new LatLon(60.619324, 22.712753),
								new LatLon(60.373484, 21.945083));
						event.getButton().setEnabled(false);
					}
				});
		buttonLayoutRow1.addComponent(limitCenterButton);

		Button limitVisibleAreaButton = new Button(
				"Limit visible area between (60.494439, 22.397835), (60.373484, 21.945083)",
				new Button.ClickListener() {
					@Override
					public void buttonClick(ClickEvent event) {
						googleMap.setVisibleAreaBoundLimits(new LatLon(60.494439, 22.397835),
								new LatLon(60.420632, 22.138626));
						event.getButton().setEnabled(false);
					}
				});
		buttonLayoutRow1.addComponent(limitVisibleAreaButton);

		Button zoomToBoundsButton = new Button("Zoom to bounds", new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				googleMap.fitToBounds(new LatLon(60.45685853323144, 22.320034754486073),
						new LatLon(60.4482979242303, 22.27887893936156));

			}
		});
		buttonLayoutRow1.addComponent(zoomToBoundsButton);

		Button clearMarkersButton = new Button("Remove all markers", new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent clickEvent) {
				googleMap.clearMarkers();
			}
		});
		buttonLayoutRow1.addComponent(clearMarkersButton);
	}
	
	// Make this null safe TODO

	protected void addMarkersForAgentDeals(Agent agt) {
		List<RealProperty> rpList = ptService.getRealPropertiesForAgent(agt.getLicense());
		
		// TODO change to the following
		// List<RealProperty> rpList = ptService.getRealPropertiesForAgent(agt);
		for (RealProperty rp : rpList) {
			Address adr = rp.getPropertyAdr();
			adrMgr.getLocation(adr);
			if (adr != null) {
				Point loc = adr.getLocation();
				if (loc != null) {
					GoogleMapMarker mrkr = new GoogleMapMarker("test", new LatLon(loc.getY(), loc.getX()),
							false);
					googleMap.addMarker(mrkr);
					googleMap.markAsDirty();
				}
			}
		}
		// }
		System.out.println("Done with Marking");
		
	}

}