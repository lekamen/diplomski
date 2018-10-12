package hr.lenak.diplomski.web.views;

import static hr.lenak.diplomski.web.views.TestniView.TESTNI_VIEW;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.VerticalLayout;

import hr.lenak.diplomski.web.ViewNames;

@SpringView(name = TESTNI_VIEW)
public class TestniView extends VerticalLayout implements View {
	public static final String TESTNI_VIEW = ViewNames.TESTNI_VIEW;
	private Logger log = LoggerFactory.getLogger(this.getClass());
	

	@Override
	public void enter(ViewChangeEvent event) {
		View.super.enter(event);
		log.debug("Otvara se view: {}", this.getClass().getSimpleName());
		
		createComponents();
		composeView();
	}
	
	private void createComponents() {	
	}
	
	private void composeView() {
	}
}
