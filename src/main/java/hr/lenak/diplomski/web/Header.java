package hr.lenak.diplomski.web;

import static hr.lenak.diplomski.web.views.NarodneNovineView.NARODNE_NOVINE_VIEW;
import static hr.lenak.diplomski.web.views.PocetnaView.POCETNA_VIEW;
import static hr.lenak.diplomski.web.views.SluzbeniDijeloviView.SLUZBENI_DIJELOVI_VIEW;
import static hr.lenak.diplomski.web.views.PregledDokumenataView.PREGLED_DOKUMENATA_VIEW;
import static hr.lenak.diplomski.web.views.PretragaPoKljucnimRijecimaView.PRETRAGA_PO_KLJUCNIM_RIJECIMA_VIEW;

import com.vaadin.server.ExternalResource;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Link;

public class Header extends HorizontalLayout {

	private Link pocetnaLink;
	private Link narodneNovineLink;
	private Link sluzbeniDijeloviLink;
	private Link pregledDokumenataLink;
	private Link pretragaPoKljucnimRijecimaLink;
	
	public Header() {
		createComponents();
		composeView();
	}
	
	private void createComponents() {
		pocetnaLink = new Link("Pocetna", new ExternalResource("#!" + POCETNA_VIEW));
		narodneNovineLink = new Link("Narodne novine", new ExternalResource("#!" + NARODNE_NOVINE_VIEW));
		sluzbeniDijeloviLink = new Link("Službeni dijelovi", new ExternalResource("#!" + SLUZBENI_DIJELOVI_VIEW));
		pregledDokumenataLink = new Link("Pregled dokumenata", new ExternalResource("#!" + PREGLED_DOKUMENATA_VIEW));
		pretragaPoKljucnimRijecimaLink = new Link("Pretraga po ključnim riječima", new ExternalResource("#!" + PRETRAGA_PO_KLJUCNIM_RIJECIMA_VIEW));
	}
	
	private void composeView() {
		setHeight(40, Unit.PIXELS);
		setSpacing(true);
		setMargin(true);
		addComponent(pocetnaLink);
		addComponent(narodneNovineLink);
		addComponent(sluzbeniDijeloviLink);
		addComponent(pregledDokumenataLink);
		addComponent(pretragaPoKljucnimRijecimaLink);
	}
}
