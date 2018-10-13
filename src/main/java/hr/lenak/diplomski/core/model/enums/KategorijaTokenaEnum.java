package hr.lenak.diplomski.core.model.enums;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

public enum KategorijaTokenaEnum {
	NOUN("N"),
	VERB("V"),
	ADJECTIVE("A"),
	PRONOUN("P"),
	ADVERB("R"),
	ADPOSITION("S"),
	CONJUNCTION("C"),
	NUMERAL("M"),
	PARTICLE("Q"),
	INTERJECTION("I"),
	ABBREVIATION("Y"),
	RESIDUAL("X");
	
	private String value;
	
	KategorijaTokenaEnum(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}

	public static KategorijaTokenaEnum fromValue(String value) {
		for (KategorijaTokenaEnum enumeration : KategorijaTokenaEnum.values()) {
			if (enumeration.getValue().equals(value)) {
				return enumeration;
			}
		}
		return null;
	}
	

	@Converter(autoApply = true)
	@SuppressWarnings("unused")
	public static class KategorijaTokenaEnumConverter implements AttributeConverter<KategorijaTokenaEnum, String> {

		@Override
		public String convertToDatabaseColumn(KategorijaTokenaEnum enumeration) {
			return enumeration != null ? enumeration.getValue() : null;
		}

		@Override
		public KategorijaTokenaEnum convertToEntityAttribute(String value) {
			return KategorijaTokenaEnum.fromValue(value);
		}
	}


}
