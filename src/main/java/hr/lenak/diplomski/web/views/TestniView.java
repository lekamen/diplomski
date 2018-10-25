package hr.lenak.diplomski.web.views;

import static hr.lenak.diplomski.web.views.TestniView.TESTNI_VIEW;

import java.util.Collections;
import java.util.List;

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
	

	private List<TekstoviSluzbeni> lista;
	@Override
	public void enter(ViewChangeEvent event) {
		View.super.enter(event);
		log.debug("Otvara se view: {}", this.getClass().getSimpleName());

		lista = Repositories.tekstoviSluzbeniRepository.findAllNajmanji();
		Collections.sort(lista, (o1, o2) -> {
			Integer l1 = o1.getTekst().length;
			Integer l2 = o2.getTekst().length;
			return l1.compareTo(l2);
		});
		createComponents();
		composeView();
	}
	
	private void createComponents() {
		
		for (int i = 0; i < 10; i++) {
			addComponent(addPanelRaw(lista.get(i)));
			addComponent(addPanel(lista.get(i)));
		}	
	}
	
	private Panel addPanelRaw(TekstoviSluzbeni t) {
		Label l = new Label(new String(t.getTekst()));
		l.setWidth(900, Unit.PIXELS);
		Panel p = new Panel(l);
		return p;
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
