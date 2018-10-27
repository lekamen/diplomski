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
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

import hr.lenak.diplomski.core.model.KljucneRijeci;
import hr.lenak.diplomski.core.model.TekstZakona;
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

//		lista = Repositories.tekstoviSluzbeniRepository.findAllNajmanji();
//		Collections.sort(lista, (o1, o2) -> {
//			Integer l1 = o1.getTekst().length;
//			Integer l2 = o2.getTekst().length;
//			return l1.compareTo(l2);
//		});
		createComponents();
		composeView();
	}
	
	private void createComponents() {
			
		TekstZakona tekst = Repositories.tekstZakonaRepository.findByBrojFilea(28);
		TekstoviSluzbeni ts = Repositories.tekstoviSluzbeniRepository.findById(tekst.getTsiId());
		KljucneRijeci kr = Repositories.kljucneRijeciRepository.findKljucneRijeci(tekst.getTsiId(), tekst.getBrojFilea(), tekst.getTekstZakonaId());
		
		VerticalLayout kljucneRijeci = new VerticalLayout();
		Label textRankLabel = new Label(kr.getKwTextrank());
		textRankLabel.setWidth(400, Unit.PIXELS);
		kljucneRijeci.addComponent(new Panel(textRankLabel));
		
		Label textRankMulWinLabel = new Label(kr.getKwTextrankMulWinSize());
		textRankMulWinLabel.setWidth(400, Unit.PIXELS);
		kljucneRijeci.addComponent(new Panel(textRankMulWinLabel));
		
		Label tfidfLabel = new Label(kr.getKwTfidf());
		tfidfLabel.setWidth(400, Unit.PIXELS);
		kljucneRijeci.addComponent(new Panel(tfidfLabel));
		
		Label textRankIdfLabel = new Label(kr.getKwTextrankIdf());
		textRankIdfLabel.setWidth(400, Unit.PIXELS);
		kljucneRijeci.addComponent(new Panel(textRankIdfLabel));
		
		Label textRankMulWinIdfLabel = new Label(kr.getKwTextrankMulWinIdf());
		textRankMulWinIdfLabel.setWidth(400, Unit.PIXELS);
		kljucneRijeci.addComponent(new Panel(textRankMulWinIdfLabel));
		
		Label l = new Label(new String(ts.getTekst()), ContentMode.HTML);
		l.setWidth(750, Unit.PIXELS);
		
		HorizontalLayout layout = new HorizontalLayout();
		layout.addComponent(kljucneRijeci);
		layout.addComponent(new Panel(l));
		
		addComponent(layout);
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
