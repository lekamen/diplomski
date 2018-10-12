package hr.lenak.diplomski.web;

import static hr.lenak.diplomski.web.views.NarodneNovineView.NARODNE_NOVINE_VIEW;
import static hr.lenak.diplomski.web.views.PocetnaView.POCETNA_VIEW;
import static hr.lenak.diplomski.web.views.SluzbeniDijeloviView.SLUZBENI_DIJELOVI_VIEW;
import static hr.lenak.diplomski.web.views.TestniView.TESTNI_VIEW;

import com.vaadin.server.ExternalResource;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Link;

public class Header extends HorizontalLayout {

	private Link pocetnaLink;
	private Link narodneNovineLink;
	private Link sluzbeniDijeloviLink;
	private Link testLink;
	
	public Header() {
		createComponents();
		composeView();
	}
	
	private void createComponents() {
		pocetnaLink = new Link("Pocetna", new ExternalResource("#!" + POCETNA_VIEW));
		narodneNovineLink = new Link("Narodne novine", new ExternalResource("#!" + NARODNE_NOVINE_VIEW));
		sluzbeniDijeloviLink = new Link("Službeni dijelovi", new ExternalResource("#!" + SLUZBENI_DIJELOVI_VIEW));
		testLink = new Link("Test", new ExternalResource("#!" + TESTNI_VIEW));
	}
	
	private void composeView() {
		setHeight(40, Unit.PIXELS);
		setSpacing(true);
		setMargin(true);
		addComponent(pocetnaLink);
		addComponent(narodneNovineLink);
		addComponent(sluzbeniDijeloviLink);
		addComponent(testLink);
	}
}
