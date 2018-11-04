package hr.lenak.diplomski.web.util;

import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.vaadin.data.ValidationResult;

import hr.lenak.diplomski.ApplicationProperties;
import hr.lenak.diplomski.core.model.TekstoviSluzbeni;

@Component
public class HelperMethods {
	
	public static ApplicationProperties applicationProperties;
	
	@Autowired
	public HelperMethods(ApplicationProperties applicationProperties) {
		HelperMethods.applicationProperties = applicationProperties;
	}

	public static int getTableLength() {
		return applicationProperties.getTableLength();
	}
	
	public static String showInvalidBinderFields(List<ValidationResult> errors) {
		StringBuilder builder = new StringBuilder();
		for (ValidationResult error : errors) {
			builder.append(error.getErrorMessage()).append("\n");
		}
		return builder.toString();
	}
	
	public static String formatDate(LocalDateTime date) {
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
		return date.format(dateFormatter);
	}
	
	
	public static String izbaciTitleIzDokumenta(TekstoviSluzbeni tekstSluzbeni) {
		String tekst = new String(tekstSluzbeni.getTekst(), Charset.forName("UTF-8"));
		Document doc = Jsoup.parse(tekst);
		doc.select("title").remove();
		return doc.outerHtml();
	}
}
