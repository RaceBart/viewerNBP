package pl.com.viewerNBP.ui;




import java.time.LocalDate;
import java.util.Collections;
import java.util.Comparator;
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
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@SpringUI
public class AppUi extends UI {
	
	@Autowired
	CurrenciesModelRepo modelRepo;

	private VerticalLayout root;
	private VerticalLayout currencyList;
	private Button addBt = new Button("Add",VaadinIcons.PLUS);
	private Button clearBt = new Button("Clear",VaadinIcons.CLOSE);
	private Button drawBt = new Button("Draw",VaadinIcons.PENCIL);
	private Button databaseValueSettBt = new Button("DB Settings",VaadinIcons.COG_O);
	private Button predictBt = new Button("Predict",VaadinIcons.CLOCK);
	private ComboBox<String> currencyChoose=new ComboBox<>();
    private DateField startDate = new DateField();
    private DateField endDate =  new DateField();	
    
	private NbpApiSender nbpSender = new NbpApiSender("http://api.nbp.pl/api/exchangerates/tables/a/");
	private WykresSlup slup;
    
	@Override
	protected void init(VaadinRequest request) {
		root = new VerticalLayout();
		setContent(root);
		addButtonsLayout();
		setupButtonsBehaviour();
		addChartLayout();
		getVaules();
		drawChart();



	}


	private void addChartLayout() {
		List<CurrenciesModel> cur1 = modelRepo.findByCurrencyname("euro");
	    Collections.sort(cur1, new Comparator<CurrenciesModel>() {
	        public int compare(CurrenciesModel o1, CurrenciesModel o2) {
	            return o1.getCurrency_date().compareTo(o2.getCurrency_date());
	            }
	    });
		slup = new WykresSlup();
		Component chartComp = slup.wykresLin(cur1);
		chartComp.setSizeFull();
		HorizontalLayout chartLayout = new HorizontalLayout();
//		currencyList = new VerticalLayout();
//		currencyList.setWidth("10%");
//		chartLayout.addComponentsAndExpand(slup.wykresLin());
//		chartLayout.addComponent(currencyList);
		chartLayout.addComponentsAndExpand(chartComp);
		chartLayout.setSizeFull();
		root.addComponent(chartLayout);

	}


	private void addButtonsLayout() {
		HorizontalLayout dataChooseLayout = new HorizontalLayout();
		currencyChoose.setItems(nbpSender.getCurrenciesList());
		currencyChoose.setPlaceholder("Choose Currency");
		startDate.setDateFormat("yyyy-MM-dd");
		endDate.setDateFormat("yyyy-MM-dd");
		endDate.setValue(LocalDate.now());
		dataChooseLayout.addComponents(addBt,clearBt,drawBt,currencyChoose,startDate,endDate,databaseValueSettBt,predictBt);
		root.addComponent(dataChooseLayout);		
	}


	private void setupButtonsBehaviour() {
		addBt.addClickListener(c->{
//			currencyList.addComponent(new Label("aaaaaaaaaaaaaa"));
			Notification.show(modelRepo.findAll().get(0).getCurrency_name());
		});
		databaseValueSettBt.addClickListener(s->{
			SettingsWindow settingsWindow = new SettingsWindow("Database Value Settings",modelRepo);
			settingsWindow.setHeight(50, Unit.PERCENTAGE);
			settingsWindow.setWidth(40, Unit.PERCENTAGE);
			settingsWindow.center();
			settingsWindow.setDraggable(true);
			settingsWindow.setModal(true);
			UI.getCurrent().addWindow(settingsWindow);
		});
	}
	
	private void drawChart() {
		// TODO Auto-generated method stub
		
	}

	private void getVaules() {
		// TODO Auto-generated method stub
		
	}



}
