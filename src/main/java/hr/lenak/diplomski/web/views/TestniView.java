package hr.lenak.diplomski.web.views;

import static hr.lenak.diplomski.web.views.TestniView.TESTNI_VIEW;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

import hr.lenak.diplomski.core.model.TekstoviSluzbeni;
import hr.lenak.diplomski.web.ViewNames;
import hr.lenak.diplomski.web.util.Repositories;

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
		
		addComponent(new Label("test"));
		addComponent(addPanel(Repositories.tekstoviSluzbeniRepository.findById(434271L)));
		addComponent(addPanel(Repositories.tekstoviSluzbeniRepository.findById(434272L)));
		addComponent(addPanel(Repositories.tekstoviSluzbeniRepository.findById(434273L)));
		addComponent(addPanel(Repositories.tekstoviSluzbeniRepository.findById(434274L)));
		addComponent(addPanel(Repositories.tekstoviSluzbeniRepository.findById(434275L)));
		addComponent(addPanel(Repositories.tekstoviSluzbeniRepository.findById(434276L)));
		addComponent(addPanel(Repositories.tekstoviSluzbeniRepository.findById(434277L)));
		addComponent(addPanel(Repositories.tekstoviSluzbeniRepository.findById(434278L)));
		addComponent(addPanel(Repositories.tekstoviSluzbeniRepository.findById(434279L)));
		addComponent(addPanel(Repositories.tekstoviSluzbeniRepository.findById(434280L)));

		
	}
	
	private Panel addPanel(TekstoviSluzbeni t) {
		Label l = new Label(new String(t.getTekst()), ContentMode.HTML);
		l.setWidth(900, Unit.PIXELS);
		Panel p = new Panel(l);
		return p;
	}
	
	private void composeView() {
	}
}
