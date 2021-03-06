package hr.lenak.diplomski.web.views;

import static hr.lenak.diplomski.web.util.HelperMethods.getTableLength;
import static hr.lenak.diplomski.web.util.Styles.COLUMN_WORD_WRAP;
import static hr.lenak.diplomski.web.views.SluzbeniDijeloviDetaljiView.SLUZBENI_DIJELOVI_DETALJI_VIEW;
import static hr.lenak.diplomski.web.views.SluzbeniDijeloviView.SLUZBENI_DIJELOVI_VIEW;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.data.Binder;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;

import hr.lenak.diplomski.core.model.NarodneNovine;
import hr.lenak.diplomski.core.model.SluzbeniDijelovi;
import hr.lenak.diplomski.web.ViewNames;
import hr.lenak.diplomski.web.util.HelperMethods;
import hr.lenak.diplomski.web.util.Repositories;
import hr.lenak.diplomski.web.util.Styles;

import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

@SpringView(name = SLUZBENI_DIJELOVI_VIEW)
public class SluzbeniDijeloviView extends VerticalLayout implements View {

	public static final String SLUZBENI_DIJELOVI_VIEW = ViewNames.SLUZBENI_DIJELOVI_VIEW;
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	private ComboBox<NarodneNovine> novineCombo;
	private TextField donositeljText;
	private TextField naslovText;
	
	private Button traziButton;
	private Button ponistiButton;
	
	private Grid<SluzbeniDijelovi> rezultatiGrid;
	private Button detaljiButton;
	
	private Binder<KriterijPretrage> binder = new Binder<>(KriterijPretrage.class);
	private KriterijPretrage kriterijPretrage = new KriterijPretrage();
	
	@Override
	public void enter(ViewChangeEvent event) {
		View.super.enter(event);
		log.debug("Otvara se view: {}", this.getClass().getSimpleName());
		createComponents();
		composeView();
		
		binder.readBean(kriterijPretrage);
		izvrsiPretragu();
	}
	
	private void createComponents() {
		
		novineCombo = createNovineCombo();
		novineCombo.addStyleName(Styles.CUSTOM);
		binder.forField(novineCombo).bind(KriterijPretrage::getNovine, KriterijPretrage::setNovine);
		
		donositeljText = new TextField();
		donositeljText.addStyleName(Styles.CUSTOM);
		donositeljText.setWidth(400, Unit.PIXELS);
		binder.forField(donositeljText).bind(KriterijPretrage::getDonositelj, KriterijPretrage::setDonositelj);

		naslovText = new TextField();
		naslovText.addStyleName(Styles.CUSTOM);
		naslovText.setWidth(850, Unit.PIXELS);
		binder.forField(naslovText).bind(KriterijPretrage::getNaslov, KriterijPretrage::setNaslov);

		traziButton = new Button("Traži");
		traziButton.addStyleName(Styles.CUSTOM);
		traziButton.setClickShortcut(KeyCode.ENTER);
		traziButton.addClickListener((e) -> novaPretraga());
		
		ponistiButton = new Button("Poništi");
		ponistiButton.addStyleNames(Styles.CUSTOM, Styles.BORDER);
		ponistiButton.addClickListener((e) -> ponistiPretragu());

		detaljiButton = new Button("Detalji");
		detaljiButton.addStyleNames(Styles.CUSTOM, Styles.BORDER);
		detaljiButton.addClickListener((e) -> detaljiSluzbenogDijela());
		detaljiButton.setEnabled(false);
		
		rezultatiGrid = new Grid<>();
		rezultatiGrid.setHeightByRows(getTableLength());
		rezultatiGrid.setSelectionMode(SelectionMode.SINGLE);
		rezultatiGrid.setWidth(95, Unit.PERCENTAGE);
		rezultatiGrid.setRowHeight(60);
		rezultatiGrid.addColumn(this::getNovineCaption).setCaption("Novine").setWidth(180);
		rezultatiGrid.addColumn(SluzbeniDijelovi::getDonositelj).setCaption("Donositelj").setWidth(270)
			.setStyleGenerator(colRef -> COLUMN_WORD_WRAP);
		rezultatiGrid.addColumn(SluzbeniDijelovi::getNaslov).setCaption("Naslov")
			.setStyleGenerator(colRef -> COLUMN_WORD_WRAP);
		
		rezultatiGrid.getSelectionModel().addSelectionListener(click -> {
			Optional<SluzbeniDijelovi> opt = click.getFirstSelectedItem();
			if (opt.isPresent()) {
				detaljiButton.setEnabled(true);
			}
			else {
				detaljiButton.setEnabled(false);
			}
		});
	}
	
	private void composeView() {
		setWidth(100, Unit.PERCENTAGE);
		setSpacing(true);
		setMargin(false);
		
		Label nasLabel = new Label("Službeni dijelovi");
		nasLabel.addStyleName(Styles.TITLE);
		addComponent(nasLabel);
		
		VerticalLayout pretragaLayout = new VerticalLayout();
		pretragaLayout.setSpacing(true);
		pretragaLayout.setWidth(100, Unit.PERCENTAGE);
		HorizontalLayout prviRed = new HorizontalLayout();
		Label novineLabel = new Label("Narodne novine");
		novineLabel.addStyleName(Styles.ALIGN_RIGHT);
		novineLabel.setWidth(150, Unit.PIXELS);
		Label donositeljLabel = new Label("Donositelj");
		donositeljLabel.addStyleName(Styles.ALIGN_RIGHT);
		donositeljLabel.setWidth(150, Unit.PIXELS);
		prviRed.addComponents(novineLabel, novineCombo, donositeljLabel, donositeljText);
		
		HorizontalLayout drugiRed = new HorizontalLayout();
		Label naslovLabel = new Label("Naslov");
		naslovLabel.addStyleName(Styles.ALIGN_RIGHT);
		naslovLabel.setWidth(150, Unit.PIXELS);
		drugiRed.addComponents(naslovLabel, naslovText);
		pretragaLayout.addComponents(prviRed, drugiRed);
		
		HorizontalLayout spaceLayout = new HorizontalLayout();
		spaceLayout.setWidth(50, Unit.PIXELS);
		pretragaLayout.addComponents(new HorizontalLayout(spaceLayout, traziButton, ponistiButton));
		
		addComponent(pretragaLayout);
		addComponent(rezultatiGrid);
		addComponent(new HorizontalLayout(detaljiButton));
	}
	
	private void novaPretraga() {
		if (binder.validate().hasErrors()) {
			Notification.show(HelperMethods.showInvalidBinderFields(binder.validate().getBeanValidationErrors()), Type.ERROR_MESSAGE);
			return;
		}
		
		binder.writeBeanIfValid(kriterijPretrage);
		izvrsiPretragu();
	}
	
	private void ponistiPretragu() {
		kriterijPretrage = new KriterijPretrage();
		binder.readBean(kriterijPretrage);
		izvrsiPretragu();
	}
	
	private void detaljiSluzbenogDijela() {
		SluzbeniDijelovi selected = rezultatiGrid.getSelectedItems().iterator().next();
		StringBuilder sb = new StringBuilder();
		sb.append("sdoId=").append(selected.getSdoId())
			.append("&prethodniPrikaz=").append(SLUZBENI_DIJELOVI_VIEW);
		getUI().getNavigator().navigateTo(SLUZBENI_DIJELOVI_DETALJI_VIEW + "/" + sb.toString());
	}
	
	private void izvrsiPretragu() {
		
		List<SluzbeniDijelovi> lista = Repositories.sluzbeniDijeloviRepository.findByKriterij(
			kriterijPretrage.getNaslov(), kriterijPretrage.getDonositelj(), kriterijPretrage.getNovine());
		
		rezultatiGrid.setItems(lista);
	}
	
	private ComboBox<NarodneNovine> createNovineCombo() {
		ComboBox<NarodneNovine> combo = new ComboBox<>();
		combo.setItems(Repositories.narodneNovineRepository.findAll());
		combo.setItemCaptionGenerator(this::getNovineCaption);
		combo.setWidth(300, Unit.PIXELS);
		return combo;
	}
	
	private String getNovineCaption(NarodneNovine novine) {
		return "Broj: " + novine.getBroj() + ", " + HelperMethods.formatDate(novine.getDatumIzdanja());
	}
	
	private String getNovineCaption(SluzbeniDijelovi dijelovi) {
		return getNovineCaption(dijelovi.getNarodneNovine());
	}
	
	public static class KriterijPretrage {
		private String naslov;
		private String donositelj;
		private NarodneNovine novine;

		public String getNaslov() {
			return naslov;
		}
		public void setNaslov(String naslov) {
			this.naslov = naslov;
		}
		public String getDonositelj() {
			return donositelj;
		}
		public void setDonositelj(String donositelj) {
			this.donositelj = donositelj;
		}
		public NarodneNovine getNovine() {
			return novine;
		}
		public void setNovine(NarodneNovine novine) {
			this.novine = novine;
		}
	}
}
