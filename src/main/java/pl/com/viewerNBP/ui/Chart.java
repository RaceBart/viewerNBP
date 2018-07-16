package pl.com.viewerNBP.ui;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.byteowls.vaadin.chartjs.ChartJs;
import com.byteowls.vaadin.chartjs.config.LineChartConfig;
import com.byteowls.vaadin.chartjs.data.Dataset;
import com.byteowls.vaadin.chartjs.data.LineDataset;
import com.byteowls.vaadin.chartjs.options.InteractionMode;
import com.byteowls.vaadin.chartjs.options.Position;
import com.byteowls.vaadin.chartjs.options.scale.Axis;
import com.byteowls.vaadin.chartjs.options.scale.CategoryScale;
import com.byteowls.vaadin.chartjs.options.scale.LinearScale;
import com.byteowls.vaadin.chartjs.utils.ColorUtils;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.Component;

public class Chart {
	public Component chartLine(List<List<CurrenciesModel>> objectsToDraw) {
		LineDataset dataset1 = new LineDataset();
		Map<String,LineDataset> mapDataset = new LinkedHashMap();
	    List<List<Double>> dataToDraw = new LinkedList();
	    List<LineDataset> datasetToDraw = new LinkedList();
		List<Double> myData1 = new ArrayList<>();
		List<String> labels = new LinkedList();

		 
		 objectsToDraw.get(0).stream().forEach(l->{
				labels.add(l.getCurrency_date().toString().substring(0,10));
		 });
		 
		 objectsToDraw.stream().forEach(o->{
			 LineDataset tempDataset = new LineDataset();
			 List<Double> tempList = new LinkedList<>();
			 o.stream().forEach(d->{
				 tempList.add(d.getCurrency_value());
			 });
			 dataToDraw.add(tempList);
			 tempDataset.dataAsList(tempList);
			 datasetToDraw.add(tempDataset);
			 mapDataset.put(o.get(0).getCurrency_name(), tempDataset);
		 });

		
		dataset1.dataAsList(myData1);
		
		LineChartConfig lineConfig = new LineChartConfig();
		lineConfig.data().labelsAsList(labels);
		
		for (String entry : mapDataset.keySet()) {
			mapDataset.get(entry);
			lineConfig.data().addDataset(mapDataset.get(entry).label(entry).fill(false).lineTension(0));
		}

		lineConfig.data()
		.and().options().responsive(true)
		
		.title().display(true).text("Currency price chart").padding(20).and().tooltips()
		.mode(InteractionMode.INDEX).intersect(true).and().hover().mode(InteractionMode.NEAREST).intersect(true)
		.and().scales()
		.add(Axis.X, new CategoryScale().display(true).scaleLabel().display(true).labelString("Date") 
				.and().position(Position.BOTTOM))
		.add(Axis.Y, new LinearScale().display(true).scaleLabel().display(true).labelString("Price").and()
				.position(Position.LEFT))
		.and().done();

		for (Dataset<?, ?> ds : lineConfig.data().getDatasets()) {
			LineDataset lds = (LineDataset) ds;
			String color = ColorUtils.randomColor(0.5);
			lds.borderColor(color).backgroundColor(color);
		}
		ChartJs chart = new ChartJs(lineConfig);
		chart.setJsLoggingEnabled(true);
		chart.setWidth(80, Unit.PERCENTAGE);
		return chart;
	}
}