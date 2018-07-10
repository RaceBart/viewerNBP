package pl.com.viewerNBP;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@SpringUI
public class AppUi extends UI{
	
	Label testLabel = new Label("test");
	VerticalLayout layout = new VerticalLayout() ;
	
	@Autowired
	public AppUi() {
	}

	@Override
	protected void init(VaadinRequest request) {
		layout.addComponent(testLabel);
		setContent(layout);		
	}

}
