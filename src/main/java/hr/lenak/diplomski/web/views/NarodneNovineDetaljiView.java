package hr.lenak.diplomski.web.views;

import static hr.lenak.diplomski.web.util.Converters.DECIMAL_TO_STRING_CONVERTER;
import static hr.lenak.diplomski.web.util.Converters.LOCALDATETIME_TO_LOCALDATE_CONVERTER;
import static hr.lenak.diplomski.web.util.Styles.COLUMN_WORD_WRAP;
import static hr.lenak.diplomski.web.views.NarodneNovineDetaljiView.NARODNE_NOVINE_DETALJI_VIEW;
import static hr.lenak.diplomski.web.views.NarodneNovineView.NARODNE_NOVINE_VIEW;
import static hr.lenak.diplomski.web.views.PocetnaView.POCETNA_VIEW;
import static hr.lenak.diplomski.web.views.SluzbeniDijeloviDetaljiView.SLUZBENI_DIJELOVI_DETALJI_VIEW;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.data.Binder;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.DateField;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.SelectionMode;

import hr.lenak.diplomski.core.model.NarodneNovine;
import hr.lenak.diplomski.core.model.SluzbeniDijelovi;
import hr.lenak.diplomski.web.ViewNames;
import hr.lenak.diplomski.web.util.Repositories;

import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

@SpringView(name = NARODNE_NOVINE_DETALJI_VIEW)
public class NarodneNovineDetaljiView extends VerticalLayout implements View {
	public static final String NARODNE_NOVINE_DETALJI_VIEW = ViewNames.NARODNE_NOVINE_DETALJI_VIEW;
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	private TextField idText;
	private TextField brojText;
	private DateField date;
	private Button detaljiButton;
	private Grid<SluzbeniDijelovi> dijeloviGrid;
	
	private Button povratakButton;
	private NarodneNovine novine;
	private Binder<NarodneNovine> binder = new Binder<>(NarodneNovine.class);
	

	@Override
	public void enter(ViewChangeEvent event) {
		View.super.enter(event);
		log.debug("Otvara se view: {}", this.getClass().getSimpleName());

		String nnId = event.getParameterMap().get("nnId");
		if (nnId == null) {
			event.getNavigator().navigateTo(POCETNA_VIEW);
			log.debug("Nepostojeci parametar");
			return;
		}
		
		novine = Repositories.narodneNovineRepository.findById(Long.valueOf(nnId));
		if (novine == null) {
			event.getNavigator().navigateTo(POCETNA_VIEW);
			log.debug("Neispravan parametar");
			return;
		}
		
		createComponents();
		composeView();
		
		binder.readBean(novine);
		binder.setReadOnly(true);
	}
	
	private void createComponents() {
		
		idText = new TextField("Id");
		idText.setWidth(100, Unit.PERCENTAGE);
		binder.forField(idText).withConverter(Long::valueOf, String::valueOf).bind(NarodneNovine::getNnId, NarodneNovine::setNnId);
		
		brojText = new TextField("Broj");
		brojText.setWidth(100, Unit.PERCENTAGE);
		binder.forField(brojText).withConverter(DECIMAL_TO_STRING_CONVERTER).bind(NarodneNovine::getBroj, null);
		
		date = new DateField("Datum izdanja");
		date.setWidth(100, Unit.PERCENTAGE);
		binder.forField(date).withConverter(LOCALDATETIME_TO_LOCALDATE_CONVERTER).bind(NarodneNovine::getDatumIzdanja, null);
		
		detaljiButton = new Button("Detalji");
		detaljiButton.addClickListener((e) -> detaljiSluzbenogDijela());
		detaljiButton.setEnabled(false);
		
		dijeloviGrid = new Grid<>();
		dijeloviGrid.setRowHeight(180);
		dijeloviGrid.setSelectionMode(SelectionMode.SINGLE);
		dijeloviGrid.setWidth(832, Unit.PIXELS);
		dijeloviGrid.addColumn(SluzbeniDijelovi::getNaslov).setCaption("Naslov").setWidth(450)
			.setStyleGenerator(colRef -> COLUMN_WORD_WRAP);
		dijeloviGrid.addColumn(SluzbeniDijelovi::getDonositelj).setCaption("Donositelj").setWidth(270)
			.setStyleGenerator(colRef -> COLUMN_WORD_WRAP);
		dijeloviGrid.addColumn(SluzbeniDijelovi::getSortIndex).setCaption("Sort indeks").setWidth(110);
		dijeloviGrid.getSelectionModel().addSelectionListener(click -> {
			Optional<SluzbeniDijelovi> opt = click.getFirstSelectedItem();
			if (opt.isPresent()) {
				detaljiButton.setEnabled(true);
			}
			else {
				detaljiButton.setEnabled(false);
			}
		});
		List<SluzbeniDijelovi> dijelovi = Repositories.sluzbeniDijeloviRepository.findByNarodneNovine(novine);
		dijeloviGrid.setItems(dijelovi);
		dijeloviGrid.setHeightByRows(dijelovi.size());
		
		povratakButton = new Button("Povratak");
		povratakButton.addClickListener(e -> getUI().getNavigator().navigateTo(NARODNE_NOVINE_VIEW));
	}
	
	private void composeView() {
		setWidthUndefined();
		setSpacing(true);
		setMargin(false);
		
		setCaption("Narodne novine detalji detalji");
		
		addComponent(new HorizontalLayout(idText));
		addComponent(new HorizontalLayout(brojText));
		addComponent(new HorizontalLayout(date));
		addComponent(new HorizontalLayout(detaljiButton));
		addComponent(dijeloviGrid);
		addComponent(new HorizontalLayout(povratakButton));
	}

	private void detaljiSluzbenogDijela() {
		SluzbeniDijelovi selected = dijeloviGrid.getSelectedItems().iterator().next();
		StringBuilder sb = new StringBuilder();
		sb.append("sdoId=").append(selected.getSdoId())
			.append("&prethodniPrikaz=").append(NARODNE_NOVINE_DETALJI_VIEW);
		getUI().getNavigator().navigateTo(SLUZBENI_DIJELOVI_DETALJI_VIEW + "/" + sb.toString());
	}
	
}
