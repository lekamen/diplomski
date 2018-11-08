package hr.lenak.diplomski.web.views;

import static hr.lenak.diplomski.web.views.PretragaPoKljucnimRijecimaView.PRETRAGA_PO_KLJUCNIM_RIJECIMA_VIEW;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.data.Binder;
import com.vaadin.data.TreeData;
import com.vaadin.data.provider.TreeDataProvider;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.TextField;
import com.vaadin.ui.TreeGrid;
import com.vaadin.ui.VerticalLayout;

import hr.lenak.diplomski.core.model.TekstoviSluzbeni;
import hr.lenak.diplomski.core.processing.VrstaAlgoritmaEnum;
import hr.lenak.diplomski.web.ViewNames;
import hr.lenak.diplomski.web.dialogs.DetaljiSluzbenogTekstaDialog;
import hr.lenak.diplomski.web.util.HelperMethods;
import hr.lenak.diplomski.web.util.PythonModule;
import hr.lenak.diplomski.web.util.RezultatiPretrage;
import hr.lenak.diplomski.web.util.Styles;

@SpringView(name = PRETRAGA_PO_KLJUCNIM_RIJECIMA_VIEW)
public class PretragaPoKljucnimRijecimaView extends VerticalLayout implements View {
	
	public static final String PRETRAGA_PO_KLJUCNIM_RIJECIMA_VIEW = ViewNames.PRETRAGA_PO_KLJUCNIM_RIJECIMA_VIEW;
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	private TextField pretragaText;
	private Button traziButton;
	private TreeGrid<RezultatiPretrage> rezultatiGrid;
	private Button detaljiButton;
	private List<RezultatiPretrage> rootItems;
	
	private Binder<KriterijPretrage> binder = new Binder<>(KriterijPretrage.class);
	private KriterijPretrage kriterijPretrage = new KriterijPretrage();
	
	@Override
	public void enter(ViewChangeEvent event) {
		View.super.enter(event);
		log.debug("Otvara se view: {}", this.getClass().getSimpleName());
		createComponents();
		composeView();
	}
	

	private void createComponents() {
		pretragaText = new TextField("Pretraži po ključnim riječima:");
		pretragaText.setWidth(60, Unit.PERCENTAGE);
		pretragaText.addStyleName(Styles.CUSTOM);
		binder.forField(pretragaText).bind(KriterijPretrage::getKljucneRijeci, KriterijPretrage::setKljucneRijeci);

		traziButton = new Button("Traži");
		traziButton.addStyleName(Styles.CUSTOM);
		traziButton.setClickShortcut(KeyCode.ENTER);
		traziButton.addClickListener((e) -> novaPretraga());
		
		detaljiButton = new Button("Detalji");
		detaljiButton.addStyleNames(Styles.CUSTOM, Styles.BORDER);
		detaljiButton.addClickListener((e) -> otvoriDetaljeTeksta());
		
		inicijalizirajRootItems();
		
		rezultatiGrid = new TreeGrid<>();
		rezultatiGrid.addStyleName(Styles.CUSTOM);
		rezultatiGrid.setSelectionMode(SelectionMode.SINGLE);
		rezultatiGrid.addColumn(this::getCaption);
		
		rezultatiGrid.setItems(rootItems);
		
		rezultatiGrid.setWidth(95, Unit.PERCENTAGE);
		
		rezultatiGrid.getSelectionModel().addSelectionListener(click -> {
			Optional<RezultatiPretrage> opt = click.getFirstSelectedItem();
			detaljiButton.setEnabled(opt.isPresent() && opt.get().getTekstSluzbeni() != null);
		});
	}
	
	private void composeView() {
		setWidth(100, Unit.PERCENTAGE);
		setSpacing(true);
		setMargin(false);
		
		Label nasLabel = new Label("Pretraga po ključnim riječima");
		nasLabel.addStyleName(Styles.TITLE);
		addComponent(nasLabel);
		
		VerticalLayout pretragaLayout = new VerticalLayout();
		pretragaLayout.setSpacing(true);
		pretragaLayout.addComponent(pretragaText);
		
		addComponent(pretragaLayout);
		addComponent(new HorizontalLayout(traziButton));
		addComponent(rezultatiGrid);
		addComponent(new HorizontalLayout(detaljiButton));
	}
	
	private void novaPretraga() {
		if (binder.validate().hasErrors()) {
			Notification.show(HelperMethods.showInvalidBinderFields(binder.validate().getBeanValidationErrors()), Type.ERROR_MESSAGE);
			return;
		}
		String keywords = PythonModule.lemmatizeWords(pretragaText.getValue());
		if (keywords != null) {
			pretragaText.setValue(keywords);
		}
		
		binder.writeBeanIfValid(kriterijPretrage);
		izvrsiPretragu();
	}
	
	private void otvoriDetaljeTeksta() {
		RezultatiPretrage selected = rezultatiGrid.getSelectedItems().iterator().next();
		DetaljiSluzbenogTekstaDialog.show(selected.getTekstSluzbeni());
	}
	
	private void izvrsiPretragu() {
		log.debug(kriterijPretrage.getKljucneRijeci());
		Map<VrstaAlgoritmaEnum, List<RezultatiPretrage>> lista = RezultatiPretrage.findByKriterijPretrage(kriterijPretrage);
		postaviRezultatePretrage(lista);
	}
	
	@SuppressWarnings("unchecked")
	private void postaviRezultatePretrage(Map<VrstaAlgoritmaEnum, List<RezultatiPretrage>> mapa) {
		TreeDataProvider<RezultatiPretrage> dataProvider = (TreeDataProvider<RezultatiPretrage>) rezultatiGrid.getDataProvider();
		TreeData<RezultatiPretrage> data = dataProvider.getTreeData();
		data.clear();
		inicijalizirajRootItems();
		for (RezultatiPretrage root : rootItems) {
			data.addItem(null, root);
			mapa.get(root.getVrstaAlgoritma()).forEach(item -> data.addItem(root, item));
		}

		dataProvider.refreshAll();
	}
	
	private void inicijalizirajRootItems() {
		rootItems = new ArrayList<>();
		for (VrstaAlgoritmaEnum vrstaAlgoritma : VrstaAlgoritmaEnum.values()) {
			rootItems.add(new RezultatiPretrage(vrstaAlgoritma, null));
		}
	}
	
	private String getCaption(RezultatiPretrage rezultat) {
		if (rezultat.getTekstSluzbeni() == null) {
			//root item
			return VrstaAlgoritmaEnum.getName(rezultat.getVrstaAlgoritma());
		}
		TekstoviSluzbeni ts = rezultat.getTekstSluzbeni();
		return ts.getSluzbeniDijelovi().getNaslov();
	}
	
	public static class KriterijPretrage {
		
		private String kljucneRijeci;
		
		public String getKljucneRijeci() {
			return kljucneRijeci;
		}
		public void setKljucneRijeci(String kljucneRijeci) {
			this.kljucneRijeci = kljucneRijeci;
		}
	}
}