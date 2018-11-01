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
import com.vaadin.ui.ComboBox;
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
		
		ComboBox<TekstZakona> tzCombo = new ComboBox<>();
		tzCombo.setItems(Repositories.tekstZakonaRepository.findAll());
		tzCombo.setItemCaptionGenerator(tz -> tz.getBrojFilea().toString());		
		
		addComponent(tzCombo);

		
		VerticalLayout kljucneRijeci = new VerticalLayout();
		Label textRankLabel = new Label();
		textRankLabel.setWidth(400, Unit.PIXELS);
		kljucneRijeci.addComponent(new Label("textrank"));
		kljucneRijeci.addComponent(new Panel(textRankLabel));
		
		Label textRankMulWinLabel = new Label();
		textRankMulWinLabel.setWidth(400, Unit.PIXELS);
		kljucneRijeci.addComponent(new Label("textrank mul win size"));
		kljucneRijeci.addComponent(new Panel(textRankMulWinLabel));
		
		Label tfidfLabel = new Label();
		tfidfLabel.setWidth(400, Unit.PIXELS);
		kljucneRijeci.addComponent(new Label("tfidf"));
		kljucneRijeci.addComponent(new Panel(tfidfLabel));
		
		Label textRankIdfLabel = new Label();
		textRankIdfLabel.setWidth(400, Unit.PIXELS);
		kljucneRijeci.addComponent(new Label("textrank idf"));
		kljucneRijeci.addComponent(new Panel(textRankIdfLabel));
		
		Label textRankMulWinIdfLabel = new Label();
		textRankMulWinIdfLabel.setWidth(400, Unit.PIXELS);
		kljucneRijeci.addComponent(new Label("textrank mul win idf"));
		kljucneRijeci.addComponent(new Panel(textRankMulWinIdfLabel));
		
		Label l = new Label();
		l.setContentMode(ContentMode.HTML);
		l.setWidth(750, Unit.PIXELS);
		
		Label raw = new Label();
		l.setWidth(750, Unit.PIXELS);
		
		HorizontalLayout layout = new HorizontalLayout();
		layout.addComponent(kljucneRijeci);
		layout.addComponent(new VerticalLayout(new Panel(l), new Panel(raw)));
		
		tzCombo.addValueChangeListener(e -> {
			
			TekstZakona tz = e.getValue();
			if (tz != null) {
				TekstoviSluzbeni ts = Repositories.tekstoviSluzbeniRepository.findById(tz.getTsiId());
				KljucneRijeci kr = Repositories.kljucneRijeciRepository.findKljucneRijeci(tz.getTsiId(), tz.getBrojFilea(), tz.getTekstZakonaId());
				
				textRankLabel.setValue(kr.getKwTextrank());
				textRankMulWinLabel.setValue(kr.getKwTextrankMulWinSize());
				tfidfLabel.setValue(kr.getKwTfidf());
				textRankIdfLabel.setValue(kr.getKwTextrankIdf());
				textRankMulWinIdfLabel.setValue(kr.getKwTextrankMulWinIdf());
				
				l.setValue(new String(ts.getTekst()));
				raw.setValue(new String(ts.getTekst()));
			}
		});
		
		addComponent(layout);
	}

	private void composeView() {
	}
}
