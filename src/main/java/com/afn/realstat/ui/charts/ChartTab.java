package com.afn.realstat.ui.charts;

import com.afn.realstat.AdReviewTourList;
import com.afn.realstat.ui.FileUploader;
import com.afn.realstat.ui.MyTourView;
import com.afn.realstat.ui.ShowPdfButton;
import com.afn.realstat.ui.TourListView;
import com.afn.realstat.ui.TourMap;
import com.byteowls.vaadin.chartjs.ChartJs;
import com.afn.realstat.ui.FileUploader.AfterUploadSucceeded;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.HasComponents;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.Upload;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class ChartTab extends HorizontalLayout {

	private VerticalLayout chartButtons;
	private HorizontalLayout thisTab;
	private ChartJs chart = null;

	public ChartTab() {

		// initialize tourPage
		setMargin(false);
		setSizeFull();

		chartButtons = new VerticalLayout();
		chartButtons.setHeight(100, Unit.PERCENTAGE);
		// chartButtons.setMargin(false);

		chartButtons.addComponent(getActiveListingsButton());
		chartButtons.setWidth(300,Unit.PIXELS);
		addComponent(chartButtons);
		
		chart = new ChartJs();
		chart.setCaption("RealStat Analytics Chart");
		chart.setSizeFull();
		addComponent(chart);
		thisTab = this;
	}

	private Button getActiveListingsButton() {
		@SuppressWarnings("serial")
		Button button = new Button("Active Listings", new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				thisTab.removeComponent(chart);
				chart = new ActiveListingsChart();
				thisTab.addComponent(chart);
			}
		});
		return button;
	}

}
