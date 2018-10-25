package hr.lenak.diplomski.core.processing;

public class KljucnaRijec {

	private String kljucnaRijec;
	private Double vrijednost;
	
	public KljucnaRijec(String kljucnaRijec, Double vrijednost) {
		this.kljucnaRijec = kljucnaRijec;
		this.vrijednost = vrijednost;
	}
	public String getKljucnaRijec() {
		return kljucnaRijec;
	}
	public void setKljucnaRijec(String kljucnaRijec) {
		this.kljucnaRijec = kljucnaRijec;
	}
	public Double getVrijednost() {
		return vrijednost;
	}
	public void setVrijednost(Double vrijednost) {
		this.vrijednost = vrijednost;
	}
	
	@Override
	public int hashCode() {
		return kljucnaRijec.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof KljucnaRijec))
			return false;
		KljucnaRijec other = (KljucnaRijec) obj;
		return kljucnaRijec.equals(other.kljucnaRijec);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("KljucnaRijec[kljucnaRijec=").append(kljucnaRijec)
			.append(",vrijednost=").append(vrijednost).append("]");
		return sb.toString();
	}
}
