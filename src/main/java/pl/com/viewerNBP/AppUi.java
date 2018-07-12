package pl.com.viewerNBP;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;


import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.DateField;
import com.vaadin.ui.DateTimeField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

import pl.com.viewerNBP.data.CurrenciesModel;
import pl.com.viewerNBP.data.CurrenciesModelRepo;

@SpringUI
public class AppUi extends UI {
	@Autowired
	private CurrenciesModelRepo currenciesRepo;

	private ComboBox<String> currencyChoose=new ComboBox<>();
    private DateField startDate = new DateField();
    private DateField endDate =  new DateField();
	private Button get = new Button("Get");
	private HorizontalLayout layout = new HorizontalLayout();
	private NbpApiSender nbpSender = new NbpApiSender("http://api.nbp.pl/api/exchangerates/tables/a/");

	public AppUi() {
	}

	@Override
	protected void init(VaadinRequest request) {

		StringBuilder startdateString = new StringBuilder();
		List<String> currencyList = new ArrayList();
		JSONArray NbpArray = nbpSender.getJsonCurrenciesFromDates("", "");
		JSONArray currencyNbpArray = NbpArray.getJSONObject(0).getJSONArray("rates");
		for (int i = 0; i < currencyNbpArray.length(); i++) {
			currencyList.add(currencyNbpArray.getJSONObject(i).getString("currency"));
			}
		currencyChoose.setItems(currencyList);
		endDate.setDateFormat("yyyy-MM-dd");
		endDate.setValue(LocalDate.now());
		currencyChoose.setPlaceholder("choose currency");
		startDate.setDateFormat("yyyy-MM-dd");
		startDate.addValueChangeListener(v->{

			Notification.show(startDate.getValue().toString());
		});
		

		get.addClickListener(c -> {
			JSONArray response = nbpSender.getJsonCurrenciesFromDates("2018-07-11", "2018-07-11");
			Notification.show(response.getJSONObject(0).getJSONArray("rates").getJSONObject(1).getString("code"));
//			List<CurrenciesModel> testRepo = currenciesRepo.findAll();
//			Notification.show(String.valueOf(testRepo.size()));
//			testLabel.setValue(nbpSender.getStringCurrenciesFromDates("2018-07-01", "2018-07-10"));
		});

		layout.addComponents(get, currencyChoose,startDate,endDate);
		setContent(layout);
	}

}
