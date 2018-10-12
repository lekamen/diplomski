package hr.lenak.diplomski.core.model;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

public class Converters {

	@Converter(autoApply = true)
	public static class LocalDateTimeConverter implements AttributeConverter<LocalDateTime, Timestamp> {

		@Override
		public Timestamp convertToDatabaseColumn(LocalDateTime locDateTime) {
			return (locDateTime == null ? null : Timestamp.valueOf(locDateTime));
		}

		@Override
		public LocalDateTime convertToEntityAttribute(Timestamp sqlTimestamp) {
			return (sqlTimestamp == null ? null : sqlTimestamp.toLocalDateTime());
		}
	}
}
