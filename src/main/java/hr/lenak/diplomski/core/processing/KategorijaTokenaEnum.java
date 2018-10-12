package hr.lenak.diplomski.core.processing;

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

}
