package pl.com.viewerNBP.ui;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
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

	private static final long serialVersionUID = 1992537788056549902L;

	@Autowired
	CurrenciesModelRepo modelRepo;

	@PersistenceContext
	private EntityManager em;

	private VerticalLayout root;
	private Button clearBt = new Button("Clear", VaadinIcons.CLOSE);
	private Button drawBt = new Button("Draw", VaadinIcons.PENCIL);
	private Button databaseValueSettBt = new Button("Db downloader", VaadinIcons.COG_O);
	private Button predictBt = new Button("Erase db", VaadinIcons.CLOCK);
	private ListSelect<String> sample = new ListSelect<>();
	private DateField startDate = new DateField("Choose start date");
	private DateField endDate = new DateField("Choose start date");
	private List<List<CurrenciesModel>> dataToDraw = new LinkedList<List<CurrenciesModel>>();
	private Comparator<CurrenciesModel> dataComparator = new Comparator<CurrenciesModel>() {
		public int compare(CurrenciesModel o1, CurrenciesModel o2) {
			return o1.getCurrency_date().compareTo(o2.getCurrency_date());
		}
	};

	private LocalDate localDateMin;
	private LocalDate localDateMax;
	private LocalDate selectedLocalDateMin;
	private LocalDate selectedLocalDateMax;
	private List<String> currenciesList = new LinkedList();

	@Override
	protected void init(VaadinRequest request) {

		setButtonsActionsEnable(checkDb());
		getDataRange();

		root = new VerticalLayout();
		addButtonsLayout();
		setupButtonsBehaviour();
		setupDatapicker();
		setContent(root);

	}

	private void getDataRange() {
		if (!currenciesList.isEmpty()) {
			List<CurrenciesModel> repoAll = modelRepo.findByCurrencyname(currenciesList.get(0));
			CurrenciesModel dbDateMax = Collections.max(repoAll, dataComparator);
			localDateMax = dbDateMax.getCurrency_date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			selectedLocalDateMax = localDateMax;
			CurrenciesModel dbDateMin = Collections.min(repoAll, dataComparator);
			localDateMin = dbDateMin.getCurrency_date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			selectedLocalDateMin = localDateMin;
		}
	}

	private void addButtonsLayout() {
		HorizontalLayout dataChooseLayout = new HorizontalLayout();

		if (!currenciesList.isEmpty()) {
			sample.clear();
			sample.setItems(currenciesList);
		}
		sample.setRows(4);
		startDate.setDateFormat("yyyy-MM-dd");
		startDate.setValue(localDateMin);
		endDate.setDateFormat("yyyy-MM-dd");
		endDate.setValue(localDateMax);
		drawBt.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		clearBt.setStyleName(ValoTheme.BUTTON_DANGER);
		dataChooseLayout.addComponents(sample, drawBt, clearBt, startDate, endDate, databaseValueSettBt, predictBt);
		dataChooseLayout.setComponentAlignment(drawBt, Alignment.BOTTOM_LEFT);
		dataChooseLayout.setComponentAlignment(clearBt, Alignment.BOTTOM_LEFT);
		dataChooseLayout.setComponentAlignment(startDate, Alignment.BOTTOM_LEFT);
		dataChooseLayout.setComponentAlignment(endDate, Alignment.BOTTOM_LEFT);
		dataChooseLayout.setComponentAlignment(databaseValueSettBt, Alignment.BOTTOM_LEFT);
		dataChooseLayout.setComponentAlignment(predictBt, Alignment.BOTTOM_LEFT);
		root.addComponent(dataChooseLayout);
	}

	private void setupButtonsBehaviour() {

		clearBt.addClickListener(c -> {
			refreshLayout();

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
					d.stream().forEach(cur -> {
						LocalDate localDateTemp = cur.getCurrency_date().toInstant().atZone(ZoneId.systemDefault())
								.toLocalDate();
						if ((!localDateTemp.isBefore(startDate.getValue())
								&& (!localDateTemp.isAfter(endDate.getValue())))) {
							tempList.add(cur);
						}
					});
					subList.add(tempList);
				});
				Chart chart = new Chart();
				Component chartComp = chart.chartLine(subList);
				chartComp.setSizeFull();
				addTrend(subList);
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
			settingsWindow.addCloseListener(cl -> {
				setButtonsActionsEnable(checkDb());
				refreshLayout();
			});
			UI.getCurrent().addWindow(settingsWindow);
		});

		predictBt.addClickListener(c -> {
//			Query q1 = em.createQuery("SELECT c FROM CurrenciesModel c WHERE c.currencyname = '"+sample.getValue()+"' AND c.currencydate BETWEEN :"+startDate.getValue().toString()+" AND :"+endDate.getValue());
//			List<CurrenciesModel> temp = q1.getResultList();
//			Notification.show(q1.getSingleResult().toString());
//			
			modelRepo.deleteAll();
			setButtonsActionsEnable(checkDb());
			refreshLayout();
		});

	}

	private void refreshLayout() {
		root.removeAllComponents();
		getDataRange();
		setButtonsActionsEnable(checkDb());
		addButtonsLayout();
	}

	private void setupDatapicker() {
		startDate.addValueChangeListener(l -> {
			if (startDate.getValue().isBefore(localDateMin)) {
				Notification.show("Date is out of range in database");
				startDate.setValue(localDateMin);
			} else {
				selectedLocalDateMin = startDate.getValue();
			}

		});

		endDate.addValueChangeListener(l -> {
			if (endDate.getValue().isAfter(localDateMax)) {
				Notification.show("Date is out of range in database");
				endDate.setValue(localDateMax);
			} else {
				selectedLocalDateMax = endDate.getValue();
			}

		});

	}

	private Boolean checkDb() {
		List<CurrenciesModel> testVal = modelRepo.findByCurrencyname("euro");
		if (!testVal.isEmpty()) {
			currenciesList.clear();
			Date sampleDate = testVal.get(0).getCurrency_date();
			List<CurrenciesModel> curForDateList = modelRepo.findByCurrencydate(sampleDate);
			curForDateList.stream().forEach(f -> {
				currenciesList.add(f.getCurrency_name());
			});
			return true;
		} else {
			return false;
		}

	}

	private void setButtonsActionsEnable(Boolean state) {
		if (state == true) {
			startDate.setEnabled(true);
			endDate.setEnabled(true);
			clearBt.setEnabled(true);
			drawBt.setEnabled(true);
			sample.setEnabled(true);
			predictBt.setEnabled(true);
		} else {
			startDate.setEnabled(false);
			endDate.setEnabled(false);
			clearBt.setEnabled(false);
			drawBt.setEnabled(false);
			sample.setEnabled(false);
			predictBt.setEnabled(false);
		}
	}
	
	private void addTrend(List<List<CurrenciesModel>> subDrawList) {
		HorizontalLayout trendLayout = new HorizontalLayout();
		subDrawList.stream().forEach(d->{
			double trend = 0;
			trend = d.get(d.size()-1).getCurrency_value()-d.get(d.size()-2).getCurrency_value();
			trend = Math.round(trend*10000.0)/10000.0;
			String trendBtString = d.get(0).getCurrency_name() + " " + String.valueOf(trend);
			Button trendBt;

			if(trend >=0) {

				trendBt = new Button(trendBtString,VaadinIcons.ARROW_CIRCLE_UP);
				trendBt.setStyleName(ValoTheme.BUTTON_FRIENDLY);
				trendLayout.addComponent(trendBt);
			}else {
				trendBt = new Button(trendBtString,VaadinIcons.ARROW_CIRCLE_DOWN);
				trendBt.setStyleName(ValoTheme.BUTTON_DANGER);
				trendLayout.addComponent(trendBt);
			}
			
		});
		root.addComponent(trendLayout);
	}

}
