package hr.lenak.diplomski.web.dialogs;

import java.nio.charset.Charset;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

import hr.lenak.diplomski.core.model.TekstoviSluzbeni;
import hr.lenak.diplomski.web.util.HelperMethods;

public class DetaljiSluzbenogTekstaDialog extends VerticalLayout{

	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	private Panel tekstPanel;
	private TekstoviSluzbeni tekstSluzbeni;
	public static Window show(TekstoviSluzbeni tekstSluzbeni) {
		Window window = new Window("Detalji službenog teksta");
		window.setWidth(70, Unit.PERCENTAGE);
		window.setContent(new DetaljiSluzbenogTekstaDialog(tekstSluzbeni));
		window.center();
		window.addCloseShortcut(KeyCode.ESCAPE);
		UI.getCurrent().addWindow(window);
		window.focus();
		return window;
	}
	
	private DetaljiSluzbenogTekstaDialog(TekstoviSluzbeni tekstSluzbeni) {
		this.tekstSluzbeni = tekstSluzbeni;
		log.debug("Otvara se dialog: {}", this.getClass().getSimpleName());
		createComponents();
		composeDialog();
	}
	
	private void createComponents() {
		Label label = new Label(HelperMethods.izbaciTitleIzDokumenta(tekstSluzbeni), ContentMode.HTML);
		label.setWidth(100, Unit.PERCENTAGE);
		tekstPanel = new Panel(label);
	}

	private void composeDialog() {
		setWidth(100, Unit.PERCENTAGE);
		setSpacing(true);
		setMargin(false);
		
		addComponent(tekstPanel);
	}

}
