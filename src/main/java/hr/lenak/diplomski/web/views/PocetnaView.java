package hr.lenak.diplomski.web.views;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

import hr.lenak.diplomski.core.processing.NadjiKljucneRijeci;
import hr.lenak.diplomski.core.processing.PretProcesiranjeZakona;
import hr.lenak.diplomski.core.processing.UcitajTekstZakonaITokenUBazu;
import hr.lenak.diplomski.web.ViewNames;

import static hr.lenak.diplomski.web.views.PocetnaView.POCETNA_VIEW;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringView(name = POCETNA_VIEW)
public class PocetnaView extends VerticalLayout implements View {

	public static final String POCETNA_VIEW = ViewNames.POCETNA_VIEW;
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	private Label label;
	
	@Override
	public void enter(ViewChangeEvent event) {
		View.super.enter(event);
		log.debug("Otvara se view: {}", this.getClass().getSimpleName());
		createComponents();
		composeView();
		
		processing();
	}
	
	private void createComponents() {
		label = new Label("Pocetna stranica!");
	}
	
	private void composeView() {
		setWidth(100, Unit.PERCENTAGE);
		setMargin(false);
		addComponent(label);
	}
	
	private void processing() {
		//pretprocesiranje zakona
		//PretProcesiranjeZakona.saveAllElementsInFiles();
		
		//ucitavanje u bazu
		//UcitajTekstZakonaITokenUBazu.ucitajTekstZakona();
		//UcitajTekstZakonaITokenUBazu.ucitajTokene();
		
		NadjiKljucneRijeci keyWords = new NadjiKljucneRijeci();
		keyWords.nadji();
	}
}
