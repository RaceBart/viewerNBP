package pl.com.viewerNBP;


import org.springframework.beans.factory.annotation.Autowired;


import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

import pl.com.viewerNBP.data.CurrenciesModelRepo;

@SpringUI
public class AppUi extends UI {
	
	@Autowired
	private CurrenciesModelRepo currenciesRepo;

	private Label testLabel = new Label("test");
	private Button test = new Button("test");
	private VerticalLayout layout = new VerticalLayout();
	private NbpApiSender nbpSender = new NbpApiSender("http://api.nbp.pl/api/exchangerates/tables/a/");

	@Autowired
	public AppUi() {
	}

	@Override
	protected void init(VaadinRequest request) {

		test.addClickListener(c -> {
//			testLabel.setValue(nbpSender.getCurrenciesFromDates("2018-07-01", "2018-07-10"));
Notification.show(currenciesRepo.findAll().toString());
		});

		layout.addComponents(test, testLabel);
		setContent(layout);
	}

}
