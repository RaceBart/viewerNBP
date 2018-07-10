package pl.com.viewerNBP;

import org.springframework.beans.factory.annotation.Autowired;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.client.methods.HttpGet;

import java.io.IOException;

import org.apache.http.HttpResponse;


import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@SpringUI
public class AppUi extends UI{
	
	Label testLabel = new Label("test");
	Button test = new Button("test");
	VerticalLayout layout = new VerticalLayout() ;
	
	@Autowired
	public AppUi() {
	}

	@Override
	protected void init(VaadinRequest request) {
		
		test.addClickListener(c->{
			HttpResponse response = null;
			String connectURL = "http://api.nbp.pl/api/exchangerates/rates/A/USD/";
			HttpClient httpClient = HttpClientBuilder.create().build();
			HttpGet requestGet = new HttpGet(connectURL);
			try {
				response = httpClient.execute(requestGet);
				testLabel.setValue(response.toString());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		});

		layout.addComponents(test,testLabel);
		setContent(layout);		
	}

}
