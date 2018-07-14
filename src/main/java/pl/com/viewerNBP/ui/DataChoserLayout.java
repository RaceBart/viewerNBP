package pl.com.viewerNBP.ui;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.DateField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.VerticalLayout;

@SpringComponent
public class DataChoserLayout extends  HorizontalLayout{
	
	private Button add = new Button("Add");
	private Button clear = new Button("Clear");
	private Button draw = new Button("Draw");
	private ComboBox<String> currencyChoose=new ComboBox<>();
    private DateField startDate = new DateField();
    private DateField endDate =  new DateField();





	private List<String> currencyList;
	private NbpApiSender nbpSender = new NbpApiSender();
	
	public DataChoserLayout() {
		super();
		currencyList = new ArrayList<String>(nbpSender.getCurrenciesList());
		currencyChoose.setItems(currencyList);
		currencyChoose.setPlaceholder("Choose Currency");
		startDate.setDateFormat("yyyy-MM-dd");
		endDate.setDateFormat("yyyy-MM-dd");
		endDate.setValue(LocalDate.now());
		addComponents(add,clear,draw,currencyChoose,startDate,endDate);
		
	}

}
