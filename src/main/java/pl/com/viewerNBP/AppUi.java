package pl.com.viewerNBP;


import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;


import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

import pl.com.viewerNBP.data.CurrenciesModel;
import pl.com.viewerNBP.data.CurrenciesModelRepo;

@SpringUI
public class AppUi extends UI {
	
	
	private CurrenciesModelRepo currenciesRepo;

	private Label testLabel = new Label("test");
	private Button test = new Button("test");
	private VerticalLayout layout = new VerticalLayout();
	private NbpApiSender nbpSender = new NbpApiSender("http://api.nbp.pl/api/exchangerates/tables/a/");

	@Autowired
	public AppUi(CurrenciesModelRepo currenciesRepo) {
		this.currenciesRepo = currenciesRepo;
	}

	@Override
	protected void init(VaadinRequest request) {

		test.addClickListener(c -> {
			JSONArray response = nbpSender.getJsonCurrenciesFromDates("2018-07-11", "2018-07-11");
			Notification.show(response.getJSONObject(0).getJSONArray("rates").getJSONObject(1).getString("code"));
			List<CurrenciesModel> testRepo = currenciesRepo.findAll();
//			testLabel.setValue(nbpSender.getStringCurrenciesFromDates("2018-07-01", "2018-07-10"));
		});

		layout.addComponents(test, testLabel);
		setContent(layout);
	}

}
