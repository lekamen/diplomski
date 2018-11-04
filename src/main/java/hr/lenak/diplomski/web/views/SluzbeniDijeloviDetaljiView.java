package hr.lenak.diplomski.web.views;

import static hr.lenak.diplomski.web.views.NarodneNovineDetaljiView.NARODNE_NOVINE_DETALJI_VIEW;
import static hr.lenak.diplomski.web.views.NarodneNovineView.NARODNE_NOVINE_VIEW;
import static hr.lenak.diplomski.web.views.SluzbeniDijeloviDetaljiView.SLUZBENI_DIJELOVI_DETALJI_VIEW;
import static hr.lenak.diplomski.web.views.SluzbeniDijeloviView.SLUZBENI_DIJELOVI_VIEW;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

import hr.lenak.diplomski.core.model.SluzbeniDijelovi;
import hr.lenak.diplomski.web.ViewNames;
import hr.lenak.diplomski.web.dialogs.DetaljiSluzbenogTekstaDialog;
import hr.lenak.diplomski.web.util.Repositories;
import hr.lenak.diplomski.web.util.Styles;

@SpringView(name = SLUZBENI_DIJELOVI_DETALJI_VIEW)
public class SluzbeniDijeloviDetaljiView extends VerticalLayout implements View {

	public static final String SLUZBENI_DIJELOVI_DETALJI_VIEW = ViewNames.SLUZBENI_DIJELOVI_DETALJI_VIEW;
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	private Label naslovLabel;
	private Label donositeljLabel;
	private Label tipLabel;
	
	private Button prikazTekstaButton;
	
	private Button povratakButton;
	private SluzbeniDijelovi sd;
	private String prethodniPrikaz;
	
	@Override
	public void enter(ViewChangeEvent event) {
		View.super.enter(event);
		log.debug("Otvara se view: {}", this.getClass().getSimpleName());

		String sdoId = event.getParameterMap().get("sdoId");
		prethodniPrikaz = event.getParameterMap().get("prethodniPrikaz");
		if (sdoId == null || prethodniPrikaz == null) {
			event.getNavigator().navigateTo(NARODNE_NOVINE_VIEW);
			log.debug("Nepostojeci parametar");
			return;
		}
		
		sd = Repositories.sluzbeniDijeloviRepository.findById(Long.valueOf(sdoId));
		if (sd == null) {
			event.getNavigator().navigateTo(NARODNE_NOVINE_VIEW);
			log.debug("Neispravan parametar");
			return;
		}
		
		createComponents();
		composeView();
	}
	
	private void createComponents() {

		naslovLabel = new Label();
		naslovLabel.setWidth(700, Unit.PIXELS);
		naslovLabel.setValue(sd.getNaslov());
		
		donositeljLabel = new Label();
		donositeljLabel.setWidth(70, Unit.PERCENTAGE);
		donositeljLabel.setValue(sd.getDonositelj());

		tipLabel = new Label();
		tipLabel.setWidth(70, Unit.PERCENTAGE);
		tipLabel.setValue(getNazivTipaDokumenta(sd));

		prikazTekstaButton = new Button("Prikaz teksta");
		prikazTekstaButton.addStyleName(Styles.CUSTOM);
		prikazTekstaButton.setClickShortcut(KeyCode.ENTER);
		prikazTekstaButton.addClickListener(e -> otvoriTekstDialog());
		
		povratakButton = new Button("Povratak");
		povratakButton.addStyleNames(Styles.CUSTOM, Styles.BORDER);
		povratakButton.addClickListener(e -> onPovratakClick());
	}
	
	private void composeView() {
		//setWidth(100, Unit.PERCENTAGE);
		setSpacing(true);
		setMargin(false);
		
		Label nasLabel = new Label("Službeni dijelovi detalji");
		nasLabel.addStyleName(Styles.TITLE);
		addComponent(nasLabel);
		
		Label naslovCaptionLabel = new Label("Naslov");
		naslovCaptionLabel.setWidth(130, Unit.PIXELS);
		naslovCaptionLabel.addStyleName(Styles.ALIGN_RIGHT);
		
		HorizontalLayout naslovLayout = new HorizontalLayout(naslovCaptionLabel, naslovLabel);
		
		Label donositeljCaptionLabel = new Label("Donositelj");
		donositeljCaptionLabel.setWidth(130, Unit.PIXELS);
		donositeljCaptionLabel.addStyleName(Styles.ALIGN_RIGHT);
		
		HorizontalLayout donositeljLayout = new HorizontalLayout(donositeljCaptionLabel, donositeljLabel);
		
		Label tipDokumentaCaptionLabel = new Label("Tip dokumenta");
		tipDokumentaCaptionLabel.setWidth(130, Unit.PIXELS);
		tipDokumentaCaptionLabel.addStyleName(Styles.ALIGN_RIGHT);
		
		HorizontalLayout tipDokumentaLayout = new HorizontalLayout(tipDokumentaCaptionLabel, tipLabel);
		
		HorizontalLayout spaceLayout = new HorizontalLayout();
		spaceLayout.setWidth(100, Unit.PIXELS);
		
		addComponent(naslovLayout);
		addComponent(donositeljLayout);
		addComponent(tipDokumentaLayout);
		addComponent(new HorizontalLayout(spaceLayout, prikazTekstaButton));

		addComponent(new HorizontalLayout(povratakButton));
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
	
	private void otvoriTekstDialog() {
		DetaljiSluzbenogTekstaDialog.show(Repositories.tekstoviSluzbeniRepository.findBySluzbeniDio(sd).get(0));
	}
}

