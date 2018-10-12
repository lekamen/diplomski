package hr.lenak.diplomski.web.util;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.vaadin.data.Converter;
import com.vaadin.data.Result;
import com.vaadin.data.ValueContext;

public class Converters {

	public static final Converter<String, BigDecimal> DECIMAL_TO_STRING_CONVERTER = new Converter<String, BigDecimal>() {
		@Override
		public Result<BigDecimal> convertToModel(String value, ValueContext context) {
			return Result.ok(value != null && value != "" ? new BigDecimal(value) : null);
		}
		@Override
		public String convertToPresentation(BigDecimal value, ValueContext context) {
			return value != null ? value.toString() : "";
		}
	};
	
	public static final Converter<LocalDate, LocalDateTime> LOCALDATETIME_TO_LOCALDATE_CONVERTER = new Converter<LocalDate, LocalDateTime>() {

		@Override
		public Result<LocalDateTime> convertToModel(LocalDate value, ValueContext context) {
			return Result.ok(value != null ? value.atStartOfDay() : null);
		}

		@Override
		public LocalDate convertToPresentation(LocalDateTime value, ValueContext context) {
			return value != null ? value.toLocalDate() : null;
		}
		
	};
}
