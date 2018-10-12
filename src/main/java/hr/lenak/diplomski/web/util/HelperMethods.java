package hr.lenak.diplomski.web.util;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.vaadin.data.ValidationResult;

import hr.lenak.diplomski.ApplicationProperties;

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
}
