package hr.lenak.diplomski.web.views;

import static hr.lenak.diplomski.web.views.PregledDokumenataView.PREGLED_DOKUMENATA_VIEW;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.data.HasValue.ValueChangeEvent;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.ComboBox.CaptionFilter;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.ComboBox.CaptionFilter;

import hr.lenak.diplomski.core.model.KljucneRijeci;
import hr.lenak.diplomski.core.model.TekstZakona;
import hr.lenak.diplomski.core.model.TekstoviSluzbeni;
import hr.lenak.diplomski.web.ViewNames;
import hr.lenak.diplomski.web.util.HelperMethods;
import hr.lenak.diplomski.web.util.Repositories;
import hr.lenak.diplomski.web.util.Styles;

@SpringView(name = PREGLED_DOKUMENATA_VIEW)
public class PregledDokumenataView extends VerticalLayout implements View {
	public static final String PREGLED_DOKUMENATA_VIEW = ViewNames.PREGLED_DOKUMENATA_VIEW;
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	private ComboBox<TekstZakona> tzCombo;
	private Label textRankLabel;
	private Label textRankMulWinLabel;
	private Label tfidfLabel;
	private Label textRankIdfLabel;
	private Label textRankMulWinIdfLabel;
	
	private Label dokumentLabel;

	@Override
	public void enter(ViewChangeEvent event) {
		View.super.enter(event);
		log.debug("Otvara se view: {}", this.getClass().getSimpleName());
		createComponents();
		composeView();
	}
	
	private void createComponents() {
		tzCombo = new ComboBox<>();
		tzCombo.setWidth(95, Unit.PERCENTAGE);
		tzCombo.addStyleName(Styles.CUSTOM);
		tzCombo.setItems(Repositories.tekstZakonaRepository.findAll());
		tzCombo.setItemCaptionGenerator(tz -> getCaption(tz));		
		
		textRankLabel = new Label();
		textRankLabel.setWidth(500, Unit.PIXELS);
		
		textRankMulWinLabel = new Label();
		textRankMulWinLabel.setWidth(500, Unit.PIXELS);
		
		tfidfLabel = new Label();
		tfidfLabel.setWidth(500, Unit.PIXELS);

		textRankIdfLabel = new Label();
		textRankIdfLabel.setWidth(500, Unit.PIXELS);
		
		textRankMulWinIdfLabel = new Label();
		textRankMulWinIdfLabel.setWidth(500, Unit.PIXELS);

		dokumentLabel = new Label();
		dokumentLabel.setHeightUndefined();
		dokumentLabel.setContentMode(ContentMode.HTML);
		dokumentLabel.setWidth(900, Unit.PIXELS);

		tzCombo.addValueChangeListener(this::setLabels);
	}

	private void composeView() {
		setWidth(100, Unit.PERCENTAGE);
		setMargin(false);
		
		Label nasLabel = new Label("Pregled dokumenata");
		nasLabel.addStyleName(Styles.TITLE);
		addComponent(nasLabel);
		
		addComponent(tzCombo);

		VerticalLayout kljucneRijeci = new VerticalLayout();
		kljucneRijeci.addComponent(new Label("TextRank algoritam"));
		kljucneRijeci.addComponent(new Panel(textRankLabel));
		
		kljucneRijeci.addComponent(new Label("TextRankMulWin algoritam"));
		kljucneRijeci.addComponent(new Panel(textRankMulWinLabel));
		
		kljucneRijeci.addComponent(new Label("tf-idf algoritam"));
		kljucneRijeci.addComponent(new Panel(tfidfLabel));
		
		kljucneRijeci.addComponent(new Label("TextRank-idf algoritam"));
		kljucneRijeci.addComponent(new Panel(textRankIdfLabel));
		
		kljucneRijeci.addComponent(new Label("TextRankMulWin-idf algoritam"));
		kljucneRijeci.addComponent(new Panel(textRankMulWinIdfLabel));
		
		HorizontalLayout layout = new HorizontalLayout();
		layout.addComponent(kljucneRijeci);
		
		VerticalLayout dokumentLayout = new VerticalLayout(dokumentLabel);
		dokumentLayout.setHeightUndefined();
		Panel dokumentPanel = new Panel(dokumentLayout);
		dokumentPanel.setHeight(620, Unit.PIXELS);
		layout.addComponent(dokumentPanel);
		
		addComponent(layout);
	}
	
	private void setLabels(ValueChangeEvent<TekstZakona> e) {
		TekstZakona tz = e.getValue();
		if (tz != null) {
			TekstoviSluzbeni ts = Repositories.tekstoviSluzbeniRepository.findById(tz.getTsiId());
			KljucneRijeci kr = Repositories.kljucneRijeciRepository.findKljucneRijeci(tz.getTsiId(), tz.getBrojFilea(), tz.getTekstZakonaId());
			
			textRankLabel.setValue(kr.getKwTextrank());
			textRankMulWinLabel.setValue(kr.getKwTextrankMulWinSize());
			tfidfLabel.setValue(kr.getKwTfidf());
			textRankIdfLabel.setValue(kr.getKwTextrankIdf());
			textRankMulWinIdfLabel.setValue(kr.getKwTextrankMulWinIdf());
			
			dokumentLabel.setValue(HelperMethods.izbaciTitleIzDokumenta(ts));
		}
	}
	
	private String getCaption(TekstZakona tz) {
		String naslov = Repositories.tekstoviSluzbeniRepository.findById(tz.getTsiId()).getSluzbeniDijelovi().getNaslov();
		return tz.getBrojFilea().toString() + ", " + naslov;
	}
}
