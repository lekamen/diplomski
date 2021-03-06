package hr.lenak.diplomski.web.views;

import static hr.lenak.diplomski.web.util.Converters.DECIMAL_TO_STRING_CONVERTER;
import static hr.lenak.diplomski.web.util.Converters.LOCALDATETIME_TO_LOCALDATE_CONVERTER;
import static hr.lenak.diplomski.web.util.HelperMethods.getTableLength;
import static hr.lenak.diplomski.web.views.NarodneNovineDetaljiView.NARODNE_NOVINE_DETALJI_VIEW;
import static hr.lenak.diplomski.web.views.NarodneNovineView.NARODNE_NOVINE_VIEW;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.data.Binder;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.DateField;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;

import hr.lenak.diplomski.core.model.NarodneNovine;
import hr.lenak.diplomski.web.ViewNames;
import hr.lenak.diplomski.web.util.HelperMethods;
import hr.lenak.diplomski.web.util.Repositories;
import hr.lenak.diplomski.web.util.Styles;

import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

@SpringView(name = NARODNE_NOVINE_VIEW)
public class NarodneNovineView extends VerticalLayout implements View {

	public static final String NARODNE_NOVINE_VIEW = ViewNames.NARODNE_NOVINE_VIEW;
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	private TextField brojText;
	private DateField odDate;
	private DateField doDate;
	
	private Button traziButton;
	private Button ponistiButton;
	
	private Grid<NarodneNovine> rezultatiGrid;
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
		brojText = new TextField();
		brojText.setStyleName(Styles.CUSTOM);
		brojText.setWidth(200, Unit.PIXELS);
		binder.forField(brojText).withConverter(DECIMAL_TO_STRING_CONVERTER).bind(KriterijPretrage::getBroj, KriterijPretrage::setBroj);
		
		odDate = new DateField();
		odDate.setStyleName(Styles.CUSTOM);
		odDate.setWidth(100, Unit.PERCENTAGE);
		binder.forField(odDate).withConverter(LOCALDATETIME_TO_LOCALDATE_CONVERTER).bind(KriterijPretrage::getOdDate, KriterijPretrage::setOdDate);
		
		doDate = new DateField();
		doDate.setStyleName(Styles.CUSTOM);
		doDate.setWidth(100, Unit.PERCENTAGE);
		binder.forField(doDate).withConverter(LOCALDATETIME_TO_LOCALDATE_CONVERTER).bind(KriterijPretrage::getDoDate, KriterijPretrage::setDoDate);

		traziButton = new Button("Traži");
		traziButton.setStyleName(Styles.CUSTOM);
		traziButton.setClickShortcut(KeyCode.ENTER);
		traziButton.addClickListener((e) -> novaPretraga());
		
		ponistiButton = new Button("Poništi");
		ponistiButton.addStyleNames(Styles.CUSTOM, Styles.BORDER);
		ponistiButton.addClickListener((e) -> ponistiPretragu());

		detaljiButton = new Button("Detalji");
		detaljiButton.setStyleName(Styles.CUSTOM);
		detaljiButton.addClickListener((e) -> detaljiNarodnihNovina());
		detaljiButton.setEnabled(false);
		
		rezultatiGrid = new Grid<>();
		rezultatiGrid.setHeightByRows(getTableLength());
		rezultatiGrid.setSelectionMode(SelectionMode.SINGLE);
		rezultatiGrid.setWidth(800, Unit.PIXELS);
		rezultatiGrid.addColumn(NarodneNovine::getBroj).setCaption("Broj");
		rezultatiGrid.addColumn(novine -> HelperMethods.formatDate(novine.getDatumIzdanja())).setCaption("Datum");
		rezultatiGrid.getSelectionModel().addSelectionListener(click -> {
			Optional<NarodneNovine> opt = click.getFirstSelectedItem();
			if (opt.isPresent()) {
				detaljiButton.setEnabled(true);
			}
			else {
				detaljiButton.setEnabled(false);
			}
		});
	}
	
	private void composeView() {
		setWidthUndefined();
		setSpacing(true);
		setMargin(true);
		
		Label naslovLabel = new Label("Narodne novine, službeni list RH");
		naslovLabel.addStyleName(Styles.TITLE);
		addComponent(naslovLabel);
		
		Label brojLabel = new Label("Broj");
		brojLabel.addStyleName(Styles.ALIGN_RIGHT);
		brojLabel.setWidth(150, Unit.PIXELS);
		Label odLabel = new Label("Od");
		odLabel.addStyleName(Styles.ALIGN_RIGHT);
		odLabel.setWidth(150, Unit.PIXELS);
		Label doLabel = new Label("Do");
		doLabel.addStyleName(Styles.ALIGN_RIGHT);
		doLabel.setWidth(70, Unit.PIXELS);
		VerticalLayout pretragaLayout = new VerticalLayout();
		pretragaLayout.setSpacing(true);
		pretragaLayout.addComponents(new HorizontalLayout(brojLabel, brojText), new HorizontalLayout(odLabel, odDate, doLabel, doDate));
		
		HorizontalLayout spaceLayout = new HorizontalLayout();
		spaceLayout.setWidth(100, Unit.PIXELS);
		
		pretragaLayout.addComponent(new HorizontalLayout(spaceLayout, traziButton, ponistiButton));
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
	
	private void detaljiNarodnihNovina() {
		NarodneNovine selected = rezultatiGrid.getSelectedItems().iterator().next();
		getUI().getNavigator().navigateTo(NARODNE_NOVINE_DETALJI_VIEW + "/nnId=" + selected.getNnId());
	}
	
	private void izvrsiPretragu() {
		List<NarodneNovine> lista = Repositories.narodneNovineRepository.findByKriterij(
			kriterijPretrage.getBroj(), kriterijPretrage.getOdDate(), kriterijPretrage.getDoDate());
		
		rezultatiGrid.setItems(lista);
	}

	public static class KriterijPretrage {
		private BigDecimal broj;
		private LocalDateTime odDate;
		private LocalDateTime doDate;
		
		public BigDecimal getBroj() {
			return broj;
		}
		public void setBroj(BigDecimal broj) {
			this.broj = broj;
		}
		public LocalDateTime getOdDate() {
			return odDate;
		}
		public void setOdDate(LocalDateTime odDate) {
			this.odDate = odDate;
		}
		public LocalDateTime getDoDate() {
			return doDate;
		}
		public void setDoDate(LocalDateTime doDate) {
			this.doDate = doDate;
		}
	}
}