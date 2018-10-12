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
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;

import hr.lenak.diplomski.ApplicationProperties;
import hr.lenak.diplomski.core.model.NarodneNovine;
import hr.lenak.diplomski.core.model.SluzbeniDijelovi;
import hr.lenak.diplomski.web.ViewNames;
import hr.lenak.diplomski.web.util.HelperMethods;
import hr.lenak.diplomski.web.util.Repositories;

import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

@SpringView(name = SLUZBENI_DIJELOVI_VIEW)
public class SluzbeniDijeloviView extends VerticalLayout implements View {

	public static final String SLUZBENI_DIJELOVI_VIEW = ViewNames.SLUZBENI_DIJELOVI_VIEW;
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	private TextField naslovText;
	private TextField donositeljText;
	private ComboBox<NarodneNovine> novineCombo;
	private TextField sortIndexText;
	
	private Button traziButton;
	private Button ponistiButton;
	
	private Button detaljiButton;
	private Grid<SluzbeniDijelovi> rezultatiGrid;
	
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
		naslovText = new TextField("Naslov");
		naslovText.setWidth(100, Unit.PERCENTAGE);
		binder.forField(naslovText).bind(KriterijPretrage::getNaslov, KriterijPretrage::setNaslov);
		
		donositeljText = new TextField("Donositelj");
		donositeljText.setWidth(100, Unit.PERCENTAGE);
		binder.forField(donositeljText).bind(KriterijPretrage::getDonositelj, KriterijPretrage::setDonositelj);
		
		novineCombo = createNovineCombo();
		binder.forField(novineCombo).bind(KriterijPretrage::getNovine, KriterijPretrage::setNovine);
		
		sortIndexText = new TextField("Sort indeks");
		sortIndexText.setWidth(100, Unit.PERCENTAGE);
		binder.forField(sortIndexText).bind(KriterijPretrage::getSortIndex, KriterijPretrage::setSortIndex);
		
		traziButton = new Button("Traži");
		traziButton.addClickListener((e) -> novaPretraga());
		
		ponistiButton = new Button("Poništi");
		ponistiButton.addClickListener((e) -> ponistiPretragu());

		detaljiButton = new Button("Detalji");
		detaljiButton.addClickListener((e) -> detaljiSluzbenogDijela());
		detaljiButton.setEnabled(false);
		
		rezultatiGrid = new Grid<>();
		rezultatiGrid.setRowHeight(180);
		rezultatiGrid.setHeightByRows(getTableLength());
		rezultatiGrid.setSelectionMode(SelectionMode.SINGLE);
		rezultatiGrid.setWidth(1000, Unit.PIXELS);
		rezultatiGrid.addColumn(SluzbeniDijelovi::getNaslov).setCaption("Naslov").setWidth(450)
			.setStyleGenerator(colRef -> COLUMN_WORD_WRAP);
		rezultatiGrid.addColumn(SluzbeniDijelovi::getDonositelj).setCaption("Donositelj").setWidth(270)
			.setStyleGenerator(colRef -> COLUMN_WORD_WRAP);
		rezultatiGrid.addColumn(this::getNovineCaption).setCaption("Novine").setWidth(180);
		rezultatiGrid.addColumn(SluzbeniDijelovi::getSortIndex).setCaption("Sort indeks").setWidth(110);
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
		//setWidth(100, Unit.PERCENTAGE);
		setWidthUndefined();
		setSpacing(true);
		setMargin(false);
		
		setCaption("Službeni dijelovi");
		
		VerticalLayout pretragaLayout = new VerticalLayout();
		pretragaLayout.setSpacing(true);
		HorizontalLayout prviRed = new HorizontalLayout();
		prviRed.addComponents(naslovText, donositeljText);
		HorizontalLayout drugiRed = new HorizontalLayout();
		drugiRed.addComponents(novineCombo, sortIndexText);
		pretragaLayout.addComponents(prviRed, drugiRed);
		
		addComponent(pretragaLayout);
		addComponent(new HorizontalLayout(traziButton, ponistiButton));
		addComponent(new HorizontalLayout(detaljiButton));
		addComponent(rezultatiGrid);
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
			kriterijPretrage.getNaslov(), kriterijPretrage.getDonositelj(), kriterijPretrage.getNovine(), kriterijPretrage.getSortIndex());
		
		rezultatiGrid.setItems(lista);
	}
	
	private ComboBox<NarodneNovine> createNovineCombo() {
		ComboBox<NarodneNovine> combo = new ComboBox<>("Narodne novine");
		combo.setItems(Repositories.narodneNovineRepository.findAll());
		combo.setItemCaptionGenerator(this::getNovineCaption);
		combo.setWidth(100, Unit.PERCENTAGE);
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
		private String sortIndex;

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
		public String getSortIndex() {
			return sortIndex;
		}
		public void setSortIndex(String sortIndex) {
			this.sortIndex = sortIndex;
		}
	}
}
