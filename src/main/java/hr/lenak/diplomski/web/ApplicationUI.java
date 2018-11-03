package hr.lenak.diplomski.web;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.annotations.Theme;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.navigator.SpringNavigator;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@SpringUI
@Theme("custom")
public class ApplicationUI extends UI {

	@Autowired
	private SpringNavigator navigator;

	@Override
	protected void init(VaadinRequest request) {
		setLocale(new Locale("hr", "HR"));
		
		Page.getCurrent().setTitle("Diplomski");

		VerticalLayout application = new VerticalLayout();
		application.setSpacing(false);
		
		Header header = new Header();
		
		VerticalLayout middle = new VerticalLayout();
		middle.setWidth(100, Unit.PERCENTAGE);
		middle.setSpacing(false);
		
		VerticalLayout view = new VerticalLayout();
		view.setMargin(false);
		view.setSpacing(false);
		view.setWidth(100, Unit.PERCENTAGE);
		middle.addComponent(view);
		
		application.addComponent(header);
		application.addComponent(middle);

		navigator.init(this, view);
		
		application.setMargin(false);
		application.setSpacing(false);
		application.setWidth(100, Unit.PERCENTAGE);
		setContent(application);
	}
	
}
