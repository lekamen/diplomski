package hr.lenak.diplomski.core.processing;

public class Token {

	private String value;
	private String lemma;
	private String tag;
	private KategorijaTokenaEnum kategorija;
	
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getLemma() {
		return lemma;
	}
	public void setLemma(String lemma) {
		this.lemma = lemma;
	}
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	public KategorijaTokenaEnum getKategorija() {
		return kategorija;
	}
	public void setKategorija(KategorijaTokenaEnum kategorija) {
		this.kategorija = kategorija;
	}
	
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Token[value=").append(value)
			.append(",kategorija=").append(kategorija)
			.append(",lemma=").append(lemma)
			.append(",tag=").append(tag).append("]");
		return sb.toString();
	}
}
