package hr.lenak.diplomski.web.views;

import static hr.lenak.diplomski.web.views.NarodneNovineDetaljiView.NARODNE_NOVINE_DETALJI_VIEW;
import static hr.lenak.diplomski.web.views.PocetnaView.POCETNA_VIEW;
import static hr.lenak.diplomski.web.views.SluzbeniDijeloviDetaljiView.SLUZBENI_DIJELOVI_DETALJI_VIEW;
import static hr.lenak.diplomski.web.views.SluzbeniDijeloviView.SLUZBENI_DIJELOVI_VIEW;

import java.nio.charset.Charset;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.data.Binder;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import hr.lenak.diplomski.core.model.SluzbeniDijelovi;
import hr.lenak.diplomski.core.model.TekstoviSluzbeni;
import hr.lenak.diplomski.web.ViewNames;
import hr.lenak.diplomski.web.util.Repositories;

@SpringView(name = SLUZBENI_DIJELOVI_DETALJI_VIEW)
public class SluzbeniDijeloviDetaljiView extends VerticalLayout implements View {

	public static final String SLUZBENI_DIJELOVI_DETALJI_VIEW = ViewNames.SLUZBENI_DIJELOVI_DETALJI_VIEW;
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	private TextField idText;
	private TextArea naslovText;
	private TextArea donositeljText;
	private TextArea potpisnikText;
	private TextField tipText;
	private TextField kljucneRijeciText;
	
	private VerticalLayout tekstSluzbeniLayout;
	
	private Button povratakButton;
	private SluzbeniDijelovi sd;
	private String prethodniPrikaz;
	private Binder<SluzbeniDijelovi> binder = new Binder<>(SluzbeniDijelovi.class);
	
	@Override
	public void enter(ViewChangeEvent event) {
		View.super.enter(event);
		log.debug("Otvara se view: {}", this.getClass().getSimpleName());

		String sdoId = event.getParameterMap().get("sdoId");
		prethodniPrikaz = event.getParameterMap().get("prethodniPrikaz");
		if (sdoId == null || prethodniPrikaz == null) {
			event.getNavigator().navigateTo(POCETNA_VIEW);
			log.debug("Nepostojeci parametar");
			return;
		}
		
		sd = Repositories.sluzbeniDijeloviRepository.findById(Long.valueOf(sdoId));
		if (sd == null) {
			event.getNavigator().navigateTo(POCETNA_VIEW);
			log.debug("Neispravan parametar");
			return;
		}
		
		createComponents();
		composeView();
		
		binder.readBean(sd);
		binder.setReadOnly(true);
	}
	
	private void createComponents() {
		
		idText = new TextField("Id");
		idText.setWidth(100, Unit.PERCENTAGE);
		binder.forField(idText).withConverter(Long::valueOf, String::valueOf).bind(SluzbeniDijelovi::getSdoId, SluzbeniDijelovi::setSdoId);
		
		naslovText = new TextArea("Naslov");
		naslovText.setWidth(600, Unit.PIXELS);
		binder.forField(naslovText).bind(SluzbeniDijelovi::getNaslov, SluzbeniDijelovi::setNaslov);
		
		donositeljText = new TextArea("Donositelj");
		donositeljText.setWidth(300, Unit.PIXELS);
		binder.forField(donositeljText).bind(SluzbeniDijelovi::getDonositelj, SluzbeniDijelovi::setDonositelj);
		
		potpisnikText = new TextArea("Potpisnik");
		potpisnikText.setWidth(300, Unit.PIXELS);
		binder.forField(potpisnikText).bind(SluzbeniDijelovi::getPotpisnik, null);
		
		tipText = new TextField("Tip dokumenta");
		tipText.setWidth(600, Unit.PIXELS);
		binder.forField(tipText).bind(this::getNazivTipaDokumenta, null);
		
		kljucneRijeciText = new TextField("Ključne riječi");
		kljucneRijeciText.setWidth(100, Unit.PERCENTAGE);
		binder.forField(kljucneRijeciText).bind(SluzbeniDijelovi::getKljucneRijeci, null);
		
		createTekstSluzbeniLayout();
		
		povratakButton = new Button("Povratak");
		povratakButton.addClickListener(e -> onPovratakClick());
	}
	
	private void composeView() {
		setWidthUndefined();
		setSpacing(true);
		setMargin(false);
		
		setCaption("Službeni dijelovi detalji");
		
		addComponent(new HorizontalLayout(idText));
		addComponent(new HorizontalLayout(naslovText));
		addComponent(new HorizontalLayout(donositeljText));
		addComponent(new HorizontalLayout(potpisnikText));
		addComponent(new HorizontalLayout(tipText));
		addComponent(new HorizontalLayout(kljucneRijeciText));
		addComponent(tekstSluzbeniLayout);
		addComponent(new HorizontalLayout(povratakButton));
	}
	
	private void createTekstSluzbeniLayout() {
		TekstoviSluzbeni tekst = Repositories.tekstoviSluzbeniRepository.findBySluzbeniDio(sd).get(0);
		
		Label id = new Label(String.valueOf(tekst.getTsiId()));
		id.setCaption("Id");
			
		Label sort = new Label(tekst.getSort().toString());
		sort.setCaption("Sort");
			
		Label chrSet = new Label(tekst.getChrset());
		chrSet.setCaption("Chrset");
			
		Label ctxPrivate = new Label(tekst.getCtxprivate());
		ctxPrivate.setCaption("Ctx private");
			
		Label euLink = new Label(tekst.getEuLink());
		euLink.setCaption("Eu link");
		Label text = new Label(new String(tekst.getTekst(), Charset.forName("UTF-8")), ContentMode.HTML);
		text.setCaption("Tekst");
		text.setWidth(900, Unit.PIXELS);
		Panel panel = new Panel();
		panel.setContent(text);
		
		tekstSluzbeniLayout = new VerticalLayout();
		tekstSluzbeniLayout.addComponent(new HorizontalLayout(new HorizontalLayout(id), new HorizontalLayout(sort), new HorizontalLayout(chrSet)));
		tekstSluzbeniLayout.addComponent(new HorizontalLayout(new HorizontalLayout(ctxPrivate), new HorizontalLayout(euLink)));
		tekstSluzbeniLayout.addComponent(panel);
			
	}
	
	private void onPovratakClick() {
		StringBuilder sb = new StringBuilder();
		if (prethodniPrikaz.equals(NARODNE_NOVINE_DETALJI_VIEW)) {
			sb.append(NARODNE_NOVINE_DETALJI_VIEW).append("/nnId=").append(sd.getNarodneNovine().getNnId());
		}
		else if (prethodniPrikaz.equals(SLUZBENI_DIJELOVI_VIEW)){
			sb.append(SLUZBENI_DIJELOVI_VIEW);
		}
		
		getUI().getNavigator().navigateTo(sb.toString());
	}
	
	private String getNazivTipaDokumenta(SluzbeniDijelovi dijelovi) {
		return dijelovi.getTipDokumenta().getTdaNaziv();
	}
}

