package com.afn.realstat.ui.charts;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.byteowls.vaadin.chartjs.ChartJs;
import com.byteowls.vaadin.chartjs.config.BarChartConfig;
import com.byteowls.vaadin.chartjs.config.ChartConfig;
import com.byteowls.vaadin.chartjs.config.LineChartConfig;
import com.byteowls.vaadin.chartjs.data.BarDataset;
import com.byteowls.vaadin.chartjs.data.Data;
import com.byteowls.vaadin.chartjs.data.Dataset;
import com.byteowls.vaadin.chartjs.data.LineDataset;
import com.byteowls.vaadin.chartjs.options.Position;

public class ActiveListingsChart extends ChartJs {

	/**
	 * Shows number of active listings on the market over a time period
	 */
	private static final long serialVersionUID = 7998526718865061845L;

	ActiveListingsChart() {
		super();
		ChartConfig chartConfig = getConfig();
		this.configure(chartConfig);
	}
	
	public LineChartConfig getConfig() { 
	   LineChartConfig config = new LineChartConfig();
	
	
	
	
	   config.data().addDataset(new LineDataset().type().label("Dataset"));
	   /*
	config.data().labels("January", "February", "March", "April", "May", "June", "July")
			.addDataset(new BarDataset().type().label("Dataset 1").backgroundColor("rgba(151,187,205,0.5)")
					.borderColor("white").borderWidth(2))
			.addDataset(new LineDataset().type().label("Dataset 2").backgroundColor("rgba(151,187,205,0.5)")
					.borderColor("white").borderWidth(2))
			.addDataset(new BarDataset().type().label("Dataset 3").backgroundColor("rgba(220,220,220,0.5)")).and();
			*/

	config.options().responsive(true).title().display(true).position(Position.LEFT)
			.text("Chart.js Active Listings on Market").and().done();

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
	
	return config;
	
	}	
	
}