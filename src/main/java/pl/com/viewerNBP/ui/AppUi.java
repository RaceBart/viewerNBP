package pl.com.viewerNBP.ui;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.DateField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@SpringUI
public class AppUi extends UI {

	@Autowired
	CurrenciesModelRepo modelRepo;

	private VerticalLayout root;
	private VerticalLayout currencyList;
	private Button addBt = new Button("Add", VaadinIcons.PLUS);
	private Button clearBt = new Button("Clear", VaadinIcons.CLOSE);
	private Button drawBt = new Button("Draw", VaadinIcons.PENCIL);
	private Button databaseValueSettBt = new Button("DB Settings", VaadinIcons.COG_O);
	private Button predictBt = new Button("Predict", VaadinIcons.CLOCK);
	private ComboBox<String> currencyChoose = new ComboBox<>();
	private ListSelect<String> sample = new ListSelect<>("Select an option");
	private DateField startDate = new DateField();
	private DateField endDate = new DateField();
	private List<String> selectedCurrencies = new LinkedList();
	private List<String> downloadedCurrencies = new LinkedList();
	private List<List<CurrenciesModel>> dataToDraw = new LinkedList();
	private Comparator<CurrenciesModel> dataComparator = new Comparator<CurrenciesModel>() {
		public int compare(CurrenciesModel o1, CurrenciesModel o2) {
			return o1.getCurrency_date().compareTo(o2.getCurrency_date());
		}
	};

	private NbpApiSender nbpSender = new NbpApiSender();
	private Chart chart;

	@Override
	protected void init(VaadinRequest request) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			// Date date = sdf.parse("2018-07-01");
			List<CurrenciesModel> sampleList = new LinkedList();
			sampleList.add(new CurrenciesModel(sdf.parse("2018-07-01"), "test", 1.1));
			sampleList.add(new CurrenciesModel(sdf.parse("2018-07-02"), "test", 2.1));
			sampleList.add(new CurrenciesModel(sdf.parse("2018-07-03"), "test", 1.1));
			dataToDraw.add(sampleList);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		root = new VerticalLayout();
		setContent(root);
		addButtonsLayout();
		setupButtonsBehaviour();
		addChartLayout();
		getVaules();
		drawChart();

	}

	private void addChartLayout() {
		// List<CurrenciesModel> cur1 = modelRepo.findByCurrencyname("euro");
		//
		// Collections.sort(cur1, new Comparator<CurrenciesModel>() {
		// public int compare(CurrenciesModel o1, CurrenciesModel o2) {
		// return o1.getCurrency_date().compareTo(o2.getCurrency_date());
		// }
		// });

		// chart = new Chart();
		// Component chartComp = chart.chartLine(cur1);
		// chartComp.setSizeFull();
		// HorizontalLayout chartLayout = new HorizontalLayout();
		//
		// chartLayout.addComponentsAndExpand(chartComp);
		// chartLayout.setSizeFull();
		// root.addComponent(chartLayout);

	}

	private void addButtonsLayout() {
		HorizontalLayout dataChooseLayout = new HorizontalLayout();
		currencyChoose.setItems(nbpSender.getCurrenciesList());
		currencyChoose.setPlaceholder("Choose Currency");
		currencyChoose.setEmptySelectionAllowed(false);
		sample.setItems(nbpSender.getCurrenciesList());
		sample.setRows(3);
		startDate.setDateFormat("yyyy-MM-dd");
		endDate.setDateFormat("yyyy-MM-dd");
		endDate.setValue(LocalDate.now());
		dataChooseLayout.addComponents(sample, addBt, clearBt, drawBt, currencyChoose, startDate, endDate,
				databaseValueSettBt, predictBt);
		root.addComponent(dataChooseLayout);
	}

	private void setupButtonsBehaviour() {

		addBt.addClickListener(c -> {
			if (!selectedCurrencies.contains(currencyChoose.getValue())) {
				selectedCurrencies.add(currencyChoose.getValue());
				// selectedCurrenciesCombo.setItems(selectedCurrencies);
			} else {
				Notification.show("Choose new currency");
			}
		});

		clearBt.addClickListener(c -> {
			Notification.show(nbpSender.getBaseURL());
		});

		drawBt.addClickListener(c -> {
			// addChartLayout();
			// selectedCurrenciesCombo.setItems(selectedCurrencies);

			if (!selectedCurrencies.isEmpty()) {
				dataToDraw.clear();
				selectedCurrencies.stream().forEach(s -> {
					dataToDraw.add(modelRepo.findByCurrencyname(s));
					downloadedCurrencies.add(s);
				});

				dataToDraw.stream().forEach(d -> {
					Collections.sort(d, dataComparator);
				});
//				HorizontalLayout chartLayout = new HorizontalLayout();
				chart = new Chart();
				Component chartComp = chart.chartLine(dataToDraw);
				chartComp.setSizeFull();
//				chartLayout.addComponentsAndExpand(chartComp);
//				chartLayout.setSizeFull();
				root.addComponent(chartComp);

				Notification.show(
						String.valueOf(selectedCurrencies.size()) + "  " + String.valueOf(downloadedCurrencies.size()));

			} else {
				Notification.show("Choose some currency to show");
			}
		});

		databaseValueSettBt.addClickListener(s -> {
			SettingsWindow settingsWindow = new SettingsWindow("Database Value Settings", modelRepo);
			settingsWindow.setHeight(20, Unit.PERCENTAGE);
			settingsWindow.setWidth(30, Unit.PERCENTAGE);
			settingsWindow.center();
			settingsWindow.setDraggable(true);
			settingsWindow.setModal(true);
			UI.getCurrent().addWindow(settingsWindow);
		});

		predictBt.addClickListener(c -> {

		});

	}

	private void drawChart() {
		// TODO Auto-generated method stub

	}

	private void getVaules() {
		// TODO Auto-generated method stub

	}

}
