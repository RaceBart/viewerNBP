package pl.com.viewerNBP.ui;


import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.DateField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@SpringUI
public class AppUi extends UI {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1992537788056549902L;

	@Autowired
	CurrenciesModelRepo modelRepo;

	private VerticalLayout root;
	private Button addBt = new Button("Add", VaadinIcons.PLUS);
	private Button clearBt = new Button("Clear", VaadinIcons.CLOSE);
	private Button drawBt = new Button("Draw", VaadinIcons.PENCIL);
	private Button databaseValueSettBt = new Button("DB Settings", VaadinIcons.COG_O);
	private Button predictBt = new Button("Predict", VaadinIcons.CLOCK);
	private ComboBox<String> currencyChoose = new ComboBox<>();
	private ListSelect<String> sample = new ListSelect<>();
	private DateField startDate = new DateField("Choose start date");
	private DateField endDate = new DateField("Choose start date");
//	private List<String> selectedCurrencies = new LinkedList();
	private List<List<CurrenciesModel>> dataToDraw = new LinkedList<List<CurrenciesModel>>();
	private Comparator<CurrenciesModel> dataComparator = new Comparator<CurrenciesModel>() {
		public int compare(CurrenciesModel o1, CurrenciesModel o2) {
			return o1.getCurrency_date().compareTo(o2.getCurrency_date());
		}
	};

	private NbpApiSender nbpSender = new NbpApiSender();
	private LocalDate localDateMin;
	private LocalDate localDateMax;
	private LocalDate selectedLocalDateMin;
	private LocalDate selectedLocalDateMax;

	private List<String> currenciesList = new LinkedList();

	@Override
	protected void init(VaadinRequest request) {
		// *** sprawdzic czy dobre dane doszly
		currenciesList = nbpSender.getCurrenciesList();
	
		//getDataRange();
		
		root = new VerticalLayout();
		addButtonsLayout();
		
		setupButtonsBehaviour();
		setupDatapicker();
		setContent(root);

	}

	private void getDataRange() {
//		if(!currenciesList.isEmpty()) {
//			// *** niepobierac pierwszego
//		List<CurrenciesModel> repoAll = modelRepo.findByCurrencyname(currenciesList.get(0));
//		CurrenciesModel dbDateMax = Collections.max(repoAll, dataComparator);
//		localDateMax = dbDateMax.getCurrency_date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
//		selectedLocalDateMax = localDateMax;
//		CurrenciesModel dbDateMin = Collections.min(repoAll, dataComparator);
//		localDateMin = dbDateMin.getCurrency_date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
//		selectedLocalDateMin = localDateMin;
//		}		
	}

	private void addButtonsLayout() {
		HorizontalLayout dataChooseLayout = new HorizontalLayout();
		
		currencyChoose.setItems(nbpSender.getCurrenciesList());
		currencyChoose.setPlaceholder("Choose Currency");
		currencyChoose.setEmptySelectionAllowed(false);
		sample.setItems(nbpSender.getCurrenciesList());
		sample.setRows(4);
		startDate.setDateFormat("yyyy-MM-dd");
		startDate.setValue(localDateMin);
		endDate.setDateFormat("yyyy-MM-dd");
		endDate.setValue(localDateMax);
		endDate.setValue(LocalDate.now());
		drawBt.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		clearBt.setStyleName(ValoTheme.BUTTON_DANGER);
		dataChooseLayout.addComponents(sample, drawBt,clearBt,startDate, endDate,databaseValueSettBt, predictBt);
		dataChooseLayout.setComponentAlignment(drawBt, Alignment.BOTTOM_LEFT);
		dataChooseLayout.setComponentAlignment(clearBt, Alignment.BOTTOM_LEFT);
		dataChooseLayout.setComponentAlignment(startDate, Alignment.BOTTOM_LEFT);
		dataChooseLayout.setComponentAlignment(endDate, Alignment.BOTTOM_LEFT);
		dataChooseLayout.setComponentAlignment(databaseValueSettBt, Alignment.BOTTOM_LEFT);
		dataChooseLayout.setComponentAlignment(predictBt, Alignment.BOTTOM_LEFT);
		root.addComponent(dataChooseLayout);
	}

	private void setupButtonsBehaviour() {
//		LocalDate localDateTemp = null;

		addBt.addClickListener(c -> {
//			if (!selectedCurrencies.contains(currencyChoose.getValue())) {
//				selectedCurrencies.add(currencyChoose.getValue());
//			} else {
//				Notification.show("Choose new currency");
//			}
		});

		clearBt.addClickListener(c -> {
			root.removeAllComponents();
			addButtonsLayout();

		});

		drawBt.addClickListener(c -> {
			List<List<CurrenciesModel>> subList = new LinkedList();
			
			if (!sample.isEmpty()) {
				dataToDraw.clear();
				sample.getValue().stream().forEach(s -> {
					dataToDraw.add(modelRepo.findByCurrencyname(s));
				});

				dataToDraw.stream().forEach(d -> {
					Collections.sort(d, dataComparator);
				});
				
				dataToDraw.stream().forEach(d -> {
					List<CurrenciesModel> tempList = new LinkedList<>();
					d.stream().forEach(cur->{
						LocalDate localDateTemp = cur.getCurrency_date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
						if((!localDateTemp.isBefore(startDate.getValue())&&(!localDateTemp.isAfter(endDate.getValue())))) {
							tempList.add(cur);
						}
					});
					subList.add(tempList);
				});
				Notification.show(String.valueOf(subList.get(0).size()));
				Chart chart = new Chart();
				Component chartComp = chart.chartLine(subList);
				chartComp.setSizeFull();
				root.addComponent(chartComp);		

			} else {
				Notification.show("Choose some currency to show");
			}
		});

		databaseValueSettBt.addClickListener(s -> {
			SettingsWindow settingsWindow = new SettingsWindow("Database Value Settings", modelRepo);
			settingsWindow.setHeight(40, Unit.PERCENTAGE);
			settingsWindow.setWidth(30, Unit.PERCENTAGE);
			settingsWindow.center();
			settingsWindow.setDraggable(true);
			settingsWindow.setModal(true);
			UI.getCurrent().addWindow(settingsWindow);
		});

		predictBt.addClickListener(c -> {
			modelRepo.deleteAll();
			Notification.show(String.valueOf(checkDb()));
		});

	}
	
	private void setupDatapicker() {
		startDate.addValueChangeListener(l->{
			if(startDate.getValue().isBefore(localDateMin)) {
				Notification.show("Date is out of range in database");
				startDate.setValue(localDateMin);
			}else {
				selectedLocalDateMin = startDate.getValue();
			}
			
		});
		
		endDate.addValueChangeListener(l->{
			if(endDate.getValue().isAfter(localDateMax)) {
				Notification.show("Date is out of range in database");
				endDate.setValue(localDateMax);
			}else {
				selectedLocalDateMax = endDate.getValue();
			}
			
		});


	}
	
	private Boolean checkDb() {
		List<CurrenciesModel> testVal = modelRepo.findOneByCurrencyname("euro");
		if(testVal!=null) {
			return true;
		}else {
			return false;
		}
		
		
	}

	private void setButtonsActionsEnable(Boolean state) {
		if(state == true) {
			startDate.setEnabled(true);
			endDate.setEnabled(true);
			clearBt.setEnabled(true);
			drawBt.setEnabled(true);
			sample.setEnabled(true);
		}else{
			startDate.setEnabled(false);
			endDate.setEnabled(false);
			clearBt.setEnabled(false);
			drawBt.setEnabled(false);
			sample.setEnabled(false);
		}
	}


}
