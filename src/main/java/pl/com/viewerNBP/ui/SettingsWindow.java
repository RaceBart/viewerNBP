package pl.com.viewerNBP.ui;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.DateField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;


//@SpringComponent
public class SettingsWindow extends Window {


	private CurrenciesModelRepo modelRepo;
	private VerticalLayout mainLayout;
	private HorizontalLayout datesLayout;
	private DateField startDate = new DateField();
	private DateField endDate = new DateField();
	private Label info;
	private LocalDate minDate;
	private LocalDate maxDate;
	private Button getCurBt = new Button("Get New Currency Data");
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	private NbpApiSender nbpSender = new NbpApiSender();

	private static final long serialVersionUID = 1L;

	public SettingsWindow(String caption,CurrenciesModelRepo modelRepo) {
		super(caption);
		this.modelRepo = modelRepo;
		startDate.setDateFormat("yyyy-MM-dd");
		endDate.setDateFormat("yyyy-MM-dd");
		getDbDates();
		
		getCurBt.addClickListener(c->{
//			List<CurrenciesModel> resultList = nbpSender.getAllFromNbp("2018-07-03", "2018-07-06");
			
			
			if((startDate.getValue()==null)||(endDate.getValue()==null)) {
				Notification.show("Choose start and end date");
			}else if(endDate.getValue().isBefore(startDate.getValue())) {
				Notification.show("End date cannot be before start date");
			}else if(endDate.getValue().isAfter(LocalDate.now())) {
				Notification.show("Only past data can be downloaded");
			}else {
				Notification.show("ok");
				modelRepo.deleteAll();
				List<CurrenciesModel> resultList = nbpSender.getAllFromNbp(startDate.getValue(), endDate.getValue());
				modelRepo.save(resultList);
			}
				
				
				
//				LocalDate settedStartDate = startDate.getValue();
//				LocalDate settedEndDate = endDate.getValue();
//				if(settedEndDate.isBefore(settedStartDate)) {
//					Notification.show("End date cannot be before start date");
//				}else {
//					Notification.show(settedEndDate.toString());
//				}
//			}else {
//				Notification.show("Choose start and end date");
//			}
		});

		mainLayout = new VerticalLayout();
		datesLayout = new HorizontalLayout();
		mainLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
		datesLayout.addComponents(startDate,endDate);
		mainLayout.addComponent(info);
		mainLayout.addComponent(datesLayout);
		mainLayout.addComponent(getCurBt);
		setContent(mainLayout);
	}

	private void getDbDates() {
		List<CurrenciesModel> formRepoList = modelRepo.findAll();
		List<LocalDate> datesList = new ArrayList<>();
		formRepoList.stream().forEach(c->{
			if(c.getCurrency_date()!=null) {
			datesList.add(c.getCurrency_date());
			}
		});
		if(!datesList.isEmpty()) {
		minDate = Collections.min(datesList);
		maxDate = Collections.max(datesList);
		startDate.setCaption("From " + minDate.toString().substring(0, 10));
		endDate.setCaption("To "+maxDate.toString().substring(0, 10));		
		}else {
			startDate.setCaption("From " + "...");
			endDate.setCaption("To "+"...");
		}
		info = new Label("In Database currencies are downloaded for dates");

	}

	public SettingsWindow() {
		super();

	}

	public SettingsWindow(String caption, Component content) {
		super(caption, content);
		// TODO Auto-generated constructor stub
	}

}
