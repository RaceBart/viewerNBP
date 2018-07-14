package pl.com.viewerNBP.ui;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
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
	public Component wykresLin(List<CurrenciesModel> cur1) {
		LineDataset dataset1 = new LineDataset();

		List<Double> myData1 = new ArrayList<>();
		List<String> labels = new LinkedList();

		cur1.stream().forEach(f->{
			myData1.add(f.getCurrency_value());
			labels.add(f.getCurrency_date().toString().substring(0,10));
		});
		
		dataset1.dataAsList(myData1);
		
		LineChartConfig lineConfig = new LineChartConfig();
		
		lineConfig.data().labelsAsList(labels)				
		.addDataset(dataset1.label(cur1.get(0).getCurrency_name()).fill(false).lineTension(0))
		.and().options().responsive(true)
		.title().display(true).text("tytuł wykresu liniowego").padding(20).and().tooltips()
		.mode(InteractionMode.INDEX).intersect(true).and().hover().mode(InteractionMode.NEAREST).intersect(true)
		.and().scales()
		.add(Axis.X, new CategoryScale().display(true).scaleLabel().display(true).labelString("Data") 
				.and().position(Position.BOTTOM))
		.add(Axis.Y, new LinearScale().display(true).scaleLabel().display(true).labelString("Wartość").and()
				.position(Position.LEFT))
		.and().done();




//		lineConfig.data().labelsAsList(labels);
//		lineConfig.data().labelsAsList(labels)
				// serie nie wypełnione kolorem
//				.addDataset(dataset1.label("Dane 1").fill(false).lineTension(0))
//				.addDataset(dataset2.label("Dane 2").fill(false).lineTension(0))
				// seria domyślnie ukryta, pojawi się po kliknięciu jej w legendzie
//				.addDataset(dataset3.label("Dane 3").hidden(true).lineTension(0)).and().options().responsive(true)
//				.title().display(true).text("tytuł wykresu liniowego").padding(20).and().tooltips()
//				.mode(InteractionMode.INDEX).intersect(true).and().hover().mode(InteractionMode.NEAREST).intersect(true)
//				.and().scales()
//				.add(Axis.X, new CategoryScale().display(true).scaleLabel().display(true).labelString("Dzień tygodnia") // możemy
//																														// zatytułować
//																														// oś
//						.and().position(Position.BOTTOM))
//				.add(Axis.Y, new LinearScale().display(true).scaleLabel().display(true).labelString("Wartość").and()
//						.position(Position.LEFT))
//				.and().done();
		// pętla która losowo generuje nam kolory, żeby nie wprowadzać ichoddzielnie dla
		// każdej serii danych
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