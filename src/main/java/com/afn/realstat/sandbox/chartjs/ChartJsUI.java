package com.afn.realstat.sandbox.chartjs;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.byteowls.vaadin.chartjs.ChartJs;
import com.byteowls.vaadin.chartjs.config.BarChartConfig;
import com.byteowls.vaadin.chartjs.data.BarDataset;
import com.byteowls.vaadin.chartjs.data.Dataset;
import com.byteowls.vaadin.chartjs.data.LineDataset;
import com.byteowls.vaadin.chartjs.options.Position;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Widgetset;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
@SpringUI(path = "/sandbox/chart")
@Theme("realstatTheme")
@Widgetset("AppWidgetset")
public class ChartJsUI extends UI {

	@Autowired
	public ChartJsUI() {
	}

	@Override
	protected void init(VaadinRequest request) {
		// build layout
		ChartJs chart = getChartJs();
		chart.setSizeFull();
		VerticalLayout mainLayout = new VerticalLayout(chart);
		setContent(mainLayout);

		// Configure layouts and components
		mainLayout.setMargin(true);
		mainLayout.setSpacing(true);
	}

	ChartJs getChartJs() {
		BarChartConfig config = new BarChartConfig();
		config.data().labels("January", "February", "March", "April", "May", "June", "July")
				.addDataset(new BarDataset().type().label("Dataset 1").backgroundColor("rgba(151,187,205,0.5)")
						.borderColor("white").borderWidth(2))
				.addDataset(new LineDataset().type().label("Dataset 2").backgroundColor("rgba(151,187,205,0.5)")
						.borderColor("white").borderWidth(2))
				.addDataset(new BarDataset().type().label("Dataset 3").backgroundColor("rgba(220,220,220,0.5)")).and();

		config.options().responsive(true).title().display(true).position(Position.LEFT)
				.text("Chart.js Combo Bar Line Chart").and().done();

		List<String> labels = config.data().getLabels();
		for (Dataset<?, ?> ds : config.data().getDatasets()) {
			List<Double> data = new ArrayList<>();
			for (int i = 0; i < labels.size(); i++) {
				data.add((double) (Math.random() > 0.5 ? 1.0 : -1.0) * Math.round(Math.random() * 100));
			}

			if (ds instanceof BarDataset) {
				BarDataset bds = (BarDataset) ds;
				bds.dataAsList(data);
			}

			if (ds instanceof LineDataset) {
				LineDataset lds = (LineDataset) ds;
				lds.dataAsList(data);
			}
		}

		ChartJs chart = new ChartJs(config);
		chart.setJsLoggingEnabled(true);
		return chart;

	}

}
