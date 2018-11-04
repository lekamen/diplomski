package hr.lenak.diplomski.web;

import static hr.lenak.diplomski.web.views.NarodneNovineView.NARODNE_NOVINE_VIEW;
import static hr.lenak.diplomski.web.views.SluzbeniDijeloviView.SLUZBENI_DIJELOVI_VIEW;
import static hr.lenak.diplomski.web.views.PregledDokumenataView.PREGLED_DOKUMENATA_VIEW;
import static hr.lenak.diplomski.web.views.PretragaPoKljucnimRijecimaView.PRETRAGA_PO_KLJUCNIM_RIJECIMA_VIEW;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.ExternalResource;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Link;

import hr.lenak.diplomski.web.util.Styles;

public class Header extends HorizontalLayout {

	private Link narodneNovineLink;
	private Link sluzbeniDijeloviLink;
	private Link pregledDokumenataLink;
	private Link pretragaPoKljucnimRijecimaLink;
	
	public Header() {
		createComponents();
		composeView();
	}
	
	private void createComponents() {
		narodneNovineLink = new Link("Narodne novine", new ExternalResource("#!" + NARODNE_NOVINE_VIEW));
		narodneNovineLink.setIcon(VaadinIcons.ARCHIVE);
		sluzbeniDijeloviLink = new Link("Službeni dijelovi", new ExternalResource("#!" + SLUZBENI_DIJELOVI_VIEW));
		sluzbeniDijeloviLink.setIcon(VaadinIcons.RECORDS);
		pregledDokumenataLink = new Link("Pregled dokumenata", new ExternalResource("#!" + PREGLED_DOKUMENATA_VIEW));
		pregledDokumenataLink.setIcon(VaadinIcons.OPEN_BOOK);
		pretragaPoKljucnimRijecimaLink = new Link("Pretraga po ključnim riječima", new ExternalResource("#!" + PRETRAGA_PO_KLJUCNIM_RIJECIMA_VIEW));
		pretragaPoKljucnimRijecimaLink.setIcon(VaadinIcons.SEARCH);
	}
	
	private void composeView() {
		setHeight(40, Unit.PIXELS);
		setWidth(100, Unit.PERCENTAGE);
		setStyleName(Styles.HEADER);
		setSpacing(true);
		setMargin(true);
		addComponent(narodneNovineLink);
		addComponent(sluzbeniDijeloviLink);
		addComponent(pregledDokumenataLink);
		addComponent(pretragaPoKljucnimRijecimaLink);
	}
}
