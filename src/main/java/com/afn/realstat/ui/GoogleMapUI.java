package com.afn.realstat.ui;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.geo.Point;
import org.springframework.stereotype.Component;
import org.vaadin.viritin.LazyList;
import org.vaadin.viritin.fields.LazyComboBox;
import org.vaadin.viritin.fields.MValueChangeEvent;
import org.vaadin.viritin.fields.MValueChangeListener;

import com.afn.realstat.Address;
import com.afn.realstat.AddressManager;
import com.afn.realstat.Agent;
import com.afn.realstat.AgentRepository;
import com.afn.realstat.PropertyTransactionService;
import com.afn.realstat.QAgent;
import com.afn.realstat.RealProperty;
import com.afn.realstat.util.MapLocation;
import com.querydsl.core.types.Predicate;
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
	/*
	 * private GoogleMapMarker kakolaMarker = new
	 * GoogleMapMarker("DRAGGABLE: Kakolan vankila", new LatLon(60.44291,
	 * 22.242415), true, null); private GoogleMapInfoWindow kakolaInfoWindow =
	 * new GoogleMapInfoWindow("Kakola used to be a provincial prison.",
	 * kakolaMarker); private GoogleMapMarker maariaMarker = new
	 * GoogleMapMarker("Maaria", new LatLon(60.536403, 22.344648), false);
	 * private GoogleMapInfoWindow maariaWindow = new
	 * GoogleMapInfoWindow("Maaria is a district of Turku", maariaMarker);;
	 * private Button componentToMaariaInfoWindowButton;
	 */

	private final String center = "5541 Maxwellton Rd. Piedmont, CA 94611";

	/*
	 * @WebServlet(value = "/maps", asyncSupported = true)
	 * 
	 * @VaadinServletConfiguration(productionMode = false, ui =
	 * GoogleMapUI.class, widgetset =
	 * "com.vaadin.tapio.googlemaps.demo.DemoWidgetset") public static class
	 * Servlet extends VaadinServlet { }
	 */

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

		MapLocation cntr = new MapLocation(center);
		googleMap.setCenter(new LatLon(cntr.getLat(), cntr.getLng()));
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

		// kakolaMarker.setAnimationEnabled(false);
		// googleMap.addMarker(kakolaMarker);
		// googleMap.addMarker("DRAGGABLE: Paavo Nurmi Stadion", new
		// LatLon(60.442423, 22.26044), true,
		// "VAADIN/1377279006_stadium.png");
		googleMap.addMarker("NOT DRAGGABLE: Iso-Heikkilä", new LatLon(60.450403, 22.230399), false, null);
		// googleMap.addMarker(maariaMarker);
		// googleMap.setMinZoom(4);
		// googleMap.setMaxZoom(16);

		// kakolaInfoWindow.setWidth("400px");
		// kakolaInfoWindow.setHeight("500px");

		/*
		 * HorizontalLayout buttonLayoutRow2 = new HorizontalLayout();
		 * buttonLayoutRow2.setHeight("26px");
		 * mapContent.addComponent(buttonLayoutRow2);
		 */

		// OpenInfoWindowOnMarkerClickListener infoWindowOpener = new
		// OpenInfoWindowOnMarkerClickListener(googleMap,
		// kakolaMarker, kakolaInfoWindow);

		// googleMap.addMarkerClickListener(infoWindowOpener);

		/*
		 * googleMap.addMarkerClickListener(new MarkerClickListener() {
		 * 
		 * @Override public void markerClicked(GoogleMapMarker clickedMarker) {
		 * Label consoleEntry = new Label( "Marker \"" +
		 * clickedMarker.getCaption() + "\" at (" +
		 * clickedMarker.getPosition().getLat() + ", " +
		 * clickedMarker.getPosition().getLon() + ") clicked.");
		 * consoleLayout.addComponent(consoleEntry, 0); } });
		 */

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

		googleMap.addMarkerDragListener(new MarkerDragListener() {
			@Override
			public void markerDragged(GoogleMapMarker draggedMarker, LatLon oldPosition) {
				Label consoleEntry = new Label("Marker \"" + draggedMarker.getCaption() + "\" dragged from ("
						+ oldPosition.getLat() + ", " + oldPosition.getLon() + ") to ("
						+ draggedMarker.getPosition().getLat() + ", " + draggedMarker.getPosition().getLon() + ")");
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

		Button showAgentDealsTmp = new Button("Show deals for agent Tmp", new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {

				UI.getCurrent().addWindow(createAgentSelector());

			}

		});
		buttonLayoutRow1.addComponent(showAgentDealsTmp);
		showAgentDealsTmp.setWidth(100, Unit.PERCENTAGE);

		Button showAgentDeals = new Button("Show deals for agent", new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {

				// create markers for all properties of an agent
				// TODO create finder for agent
				// TODO create finder for agent
				// List<Agent> agt = repo.findByLicense("792768");
				// if (agt != null) {
				List<RealProperty> rpList = ptService.getRealPropertiesForAgent("792768");
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

		});
		buttonLayoutRow1.addComponent(showAgentDeals);

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

		// Button addMarkerToMaariaButton = new Button("Open Maaria Window", new
		// Button.ClickListener() {
		// @Override
		// public void buttonClick(ClickEvent clickEvent) {
		// googleMap.openInfoWindow(maariaWindow);
		// }
		// });
		// buttonLayoutRow1.addComponent(addMarkerToMaariaButton);

		/*
		 * componentToMaariaInfoWindowButton = new
		 * Button("Add component to Maaria window", new Button.ClickListener() {
		 * 
		 * @Override public void buttonClick(ClickEvent clickEvent) {
		 * googleMap.setInfoWindowContents(maariaWindow, new
		 * Button("Maaria button does something", new Button.ClickListener() {
		 * 
		 * @Override public void buttonClick(ClickEvent event) {
		 * Notification.show("hello there!"); } })); } });
		 * buttonLayoutRow1.addComponent(componentToMaariaInfoWindowButton);
		 */
		/*
		 * Button addPolyOverlayButton = new
		 * Button("Add overlay over Luonnonmaa", new Button.ClickListener() {
		 * 
		 * @Override public void buttonClick(ClickEvent event) {
		 * ArrayList<LatLon> points = new ArrayList<LatLon>(); points.add(new
		 * LatLon(60.484715, 21.923706)); points.add(new LatLon(60.446636,
		 * 21.941387)); points.add(new LatLon(60.422496, 21.99546));
		 * points.add(new LatLon(60.427326, 22.06464)); points.add(new
		 * LatLon(60.446467, 22.064297));
		 * 
		 * GoogleMapPolygon overlay = new GoogleMapPolygon(points, "#ae1f1f",
		 * 0.8, "#194915", 0.5, 3); googleMap.addPolygonOverlay(overlay);
		 * event.getButton().setEnabled(false); } });
		 * buttonLayoutRow2.addComponent(addPolyOverlayButton);
		 */

		/*
		 * Button addPolyLineButton = new
		 * Button("Draw line from Turku to Raisio", new Button.ClickListener() {
		 * 
		 * @Override public void buttonClick(ClickEvent event) {
		 * ArrayList<LatLon> points = new ArrayList<LatLon>(); points.add(new
		 * LatLon(60.448118, 22.253738)); points.add(new LatLon(60.455144,
		 * 22.24198)); points.add(new LatLon(60.460222, 22.211939));
		 * points.add(new LatLon(60.488224, 22.174602)); points.add(new
		 * LatLon(60.486025, 22.169195));
		 * 
		 * GoogleMapPolyline overlay = new GoogleMapPolyline(points, "#d31717",
		 * 0.8, 10); googleMap.addPolyline(overlay);
		 * event.getButton().setEnabled(false); } });
		 */
		/*
		 * buttonLayoutRow2.addComponent(addPolyLineButton); Button
		 * addPolyLineButton2 = new Button("Draw line from Turku to Raisio2",
		 * new Button.ClickListener() {
		 * 
		 * @Override public void buttonClick(ClickEvent event) {
		 * ArrayList<LatLon> points2 = new ArrayList<LatLon>(); points2.add(new
		 * LatLon(60.448118, 22.253738)); points2.add(new LatLon(60.486025,
		 * 22.169195)); GoogleMapPolyline overlay2 = new
		 * GoogleMapPolyline(points2, "#d31717", 0.8, 10);
		 * googleMap.addPolyline(overlay2); event.getButton().setEnabled(false);
		 * } });
		 */
		/*
		 * buttonLayoutRow2.addComponent(addPolyLineButton2); Button
		 * changeToTerrainButton = new Button("Change to terrain map", new
		 * Button.ClickListener() {
		 * 
		 * @Override public void buttonClick(ClickEvent event) {
		 * googleMap.setMapType(GoogleMap.MapType.Terrain);
		 * event.getButton().setEnabled(false); } });
		 * buttonLayoutRow2.addComponent(changeToTerrainButton);
		 */
		Button changeControls = new Button("Remove street view control", new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				googleMap.removeControl(GoogleMapControl.StreetView);
				event.getButton().setEnabled(false);
			}
		});
		buttonLayoutRow1.addComponent(changeControls);
		/*
		 * Button addInfoWindowButton = new
		 * Button("Add InfoWindow to Kakola marker", new Button.ClickListener()
		 * {
		 * 
		 * @Override public void buttonClick(ClickEvent event) {
		 * googleMap.openInfoWindow(kakolaInfoWindow); } });
		 * buttonLayoutRow2.addComponent(addInfoWindowButton);
		 */
		/*
		 * Button moveMarkerButton = new Button("Move kakola marker", new
		 * Button.ClickListener() {
		 * 
		 * @Override public void buttonClick(ClickEvent event) {
		 * kakolaMarker.setPosition(new LatLon(60.3, 22.242415));
		 * googleMap.addMarker(kakolaMarker); } });
		 * buttonLayoutRow2.addComponent(moveMarkerButton);
		 */
		Button addKmlLayerButton = new Button("Add KML layer", new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				googleMap.addKmlLayer(new GoogleMapKmlLayer(
						"http://maps.google.it/maps/" + "ms?authuser=0&ie=UTF8&hl=it&oe=UTF8&msa=0&"
								+ "output=kml&msid=212897908682884215672.0004ecbac547d2d635ff5"));
			}
		});
		buttonLayoutRow1.addComponent(addKmlLayerButton);

		Button clearMarkersButton = new Button("Remove all markers", new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent clickEvent) {
				googleMap.clearMarkers();
			}
		});
		buttonLayoutRow1.addComponent(clearMarkersButton);

		Button trafficLayerButton = new Button("Toggle Traffic Layer", new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent clickEvent) {
				googleMap.setTrafficLayerVisible(!googleMap.isTrafficLayerVisible());
			}
		});
		buttonLayoutRow1.addComponent(trafficLayerButton);

	}

	protected Window createAgentSelector() {
		// create a new pup-up window
		VerticalLayout popupContent = new VerticalLayout();

		/*
		 * ComboBox lov = new ComboBox(); // List<Agent> agentList =
		 * repo.findAll();
		 * 
		 * cityContainer = JPAContainerFactory.make(City.class,
		 * BazookaCalendarApplication.PERSISTENCE_UNIT); countryContainer =
		 * JPAContainerFactory.make(Country.class,
		 * BazookaCalendarApplication.PERSISTENCE_UNIT); setCaption("Country");
		 * 
		 * city.setContainerDataSource(cityContainer);
		 * city.setItemCaptionPropertyId("city"); city.setImmediate(true);
		 * 
		 * 
		 * 
		 * countryContainer.setApplyFiltersImmediately(false);
		 * 
		 * country.setContainerDataSource(countryContainer);
		 * country.setItemCaptionPropertyId("country");
		 * 
		 * Container cont = LazyQueryContainer.make(Agent.class); //
		 * BazookaCalendarApplication.PERSISTENCE_UNIT); // Container cont = new
		 * BeanItemContainer<Agent>(Agent.class, null); // gets the data from my
		 * mysql database
		 * 
		 * lov.setContainerDataSource(cont); //puts the data from the List
		 * <Korjaustiedot> into the container
		 * 
		 * lov .setItemCaptionPropertyId("lastName"); //chooses the data from
		 * the column "registernumber"
		 * 
		 * // lov.addValueChangeListener(event -> // Java 8 // .addComponent(new
		 * Label("Selected " + // event.getProperty().getValue())));
		 * 
		 * // lov.setItemCaptionPropertyId(fieldName); //
		 * field.setCaption(fieldLabel); lov.setNewItemsAllowed(false);
		 * lov.setNullSelectionAllowed(false); lov.setTextInputAllowed(false);
		 * // lov.setWidth(STANDARD_COMBO_WIDTH); lov.setImmediate(true);
		 */

		LazyComboBox<Agent> lov = new LazyComboBox<Agent>(Agent.class,

				new LazyComboBox.FilterablePagingProvider<Agent>() {

					@Override
					public List<Agent> findEntities(int firstRow, String filter) {
						QAgent qa = QAgent.agent;
						Predicate predicate = qa.agentName.containsIgnoreCase(filter);
						int batchSize = LazyList.DEFAULT_PAGE_SIZE;
						int pageNumber = firstRow / batchSize;
						PageRequest pr = new PageRequest(pageNumber, batchSize);
						Page<Agent> page = agtRepo.findAll(predicate, pr);
						List<Agent> list = page.getContent();
						return list;
					}
				}, new LazyComboBox.FilterableCountProvider() {

					@Override
					public int size(String filter) {
						QAgent qa = QAgent.agent;
						Predicate predicate = qa.agentName.startsWith(filter);

						long count = agtRepo.count(predicate);
						return (int) count;
					}

				});

		MValueChangeListener<Agent> listener = new MValueChangeListener<Agent>() {
			@Override
			public void valueChange(MValueChangeEvent<Agent> event) {
				Agent agt = (Agent) event.getProperty().getValue();
				System.out.println("Selected value = " + agt);
			}
		};
		lov.addMValueChangeListener(listener);

		popupContent.addComponent(lov);
		popupContent.addComponent(new Button("Button"));

		// The component itself
		Window popup = new Window("Agent Selector", popupContent);
		popup.setModal(true);
		return popup;

	}

}