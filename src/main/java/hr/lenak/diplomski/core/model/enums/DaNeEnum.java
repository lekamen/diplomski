package hr.lenak.diplomski.core.model.enums;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

public enum DaNeEnum {

	NE(0),
	DA(1);

	private Integer value;

	DaNeEnum(Integer value) {
		this.value = value;
	}

	public Integer getValue() {
		return value;
	}

	public static DaNeEnum fromValue(Integer value) {
		for (DaNeEnum enumeration : DaNeEnum.values()) {
			if (enumeration.getValue().equals(value)) {
				return enumeration;
			}
		}
		return null;
	}


	@Converter(autoApply = true)
	@SuppressWarnings("unused")
	public static class DaNeEnumConverter implements AttributeConverter<DaNeEnum, Integer> {

		@Override
		public Integer convertToDatabaseColumn(DaNeEnum enumeration) {
			return enumeration != null ? enumeration.getValue() : null;
		}

		@Override
		public DaNeEnum convertToEntityAttribute(Integer value) {
			return DaNeEnum.fromValue(value);
		}
	}

}
