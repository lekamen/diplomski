package hr.lenak.diplomski.core.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the KLJUCNE_RIJECI database table.
 * 
 */
@Entity
@Table(name="KLJUCNE_RIJECI")
public class KljucneRijeci implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="KLJ_RIJ_SEQUENCEGENERATOR", sequenceName="SEQUENCEGENERATOR")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="KLJ_RIJ_SEQUENCEGENERATOR")
	@Column(name="KLJUCNE_RIJECI_ID")
	private Long kljucneRijeciId;

	@Column(name="BROJ_FILEA")
	private Integer brojFilea;

	@Column(name="KW_TEXTRANK")
	private String kwTextrank;

	@Column(name="KW_TEXTRANK_MUL_WIN_SIZE")
	private String kwTextrankMulWinSize;

	@Column(name="KW_TFIDF")
	private String kwTfidf;
	
	@Column(name="KW_TEXTRANK_IDF")
	private String kwTextrankIdf;

	@Column(name="TEKST_ZAKONA_ID")
	private Long tekstZakonaId;

	@Column(name="TSI_ID")
	private Long tsiId;

	public Long getKljucneRijeciId() {
		return this.kljucneRijeciId;
	}

	public void setKljucneRijeciId(Long kljucneRijeciId) {
		this.kljucneRijeciId = kljucneRijeciId;
	}

	public Integer getBrojFilea() {
		return this.brojFilea;
	}

	public void setBrojFilea(Integer brojFilea) {
		this.brojFilea = brojFilea;
	}

	public String getKwTextrank() {
		return this.kwTextrank;
	}

	public void setKwTextrank(String kwTextrank) {
		this.kwTextrank = kwTextrank;
	}

	public String getKwTextrankMulWinSize() {
		return this.kwTextrankMulWinSize;
	}

	public void setKwTextrankMulWinSize(String kwTextrankMulWinSize) {
		this.kwTextrankMulWinSize = kwTextrankMulWinSize;
	}

	public String getKwTfidf() {
		return this.kwTfidf;
	}

	public void setKwTfidf(String kwTfidf) {
		this.kwTfidf = kwTfidf;
	}

	public String getKwTextrankIdf() {
		return kwTextrankIdf;
	}

	public void setKwTextrankIdf(String kwTextrankIdf) {
		this.kwTextrankIdf = kwTextrankIdf;
	}

	public Long getTekstZakonaId() {
		return this.tekstZakonaId;
	}

	public void setTekstZakonaId(Long tekstZakonaId) {
		this.tekstZakonaId = tekstZakonaId;
	}

	public Long getTsiId() {
		return this.tsiId;
	}

	public void setTsiId(Long tsiId) {
		this.tsiId = tsiId;
	}
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((brojFilea == null) ? 0 : brojFilea.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		KljucneRijeci other = (KljucneRijeci) obj;
		if (kljucneRijeciId == null) {
			if (other.kljucneRijeciId != null) {
				return false;
			}
			else {
				return this == obj;
			}
		}
		else if (!kljucneRijeciId.equals(other.kljucneRijeciId))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("KljucneRijeci[kljucneRijeciId=")
			.append(kljucneRijeciId).append("]");
		return sb.toString();
	}


}