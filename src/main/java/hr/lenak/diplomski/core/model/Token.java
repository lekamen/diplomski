package hr.lenak.diplomski.core.model;

import java.io.Serializable;
import javax.persistence.*;

import hr.lenak.diplomski.core.model.enums.KategorijaTokenaEnum;


/**
 * The persistent class for the TOKEN database table.
 * 
 */
@Entity
public class Token implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TOKEN_TOKENID_GENERATOR", sequenceName="TOKEN_SEQUENCEGENERATOR")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TOKEN_TOKENID_GENERATOR")
	@Column(name="TOKEN_ID")
	private Long tokenId;

	private KategorijaTokenaEnum kategorija;

	private String lemma;

	private String tag;

	@Column(name="\"VALUE\"")
	private String value;
	
	@Column(name="POSITION")
	private Integer position;
	
	//bi-directional many-to-one association to TekstZakona
	@ManyToOne
	@JoinColumn(name="TEKST_ZAKONA_ID")
	private TekstZakona tekstZakona;

	public Token() {
	}

	public Long getTokenId() {
		return this.tokenId;
	}

	public void setTokenId(Long tokenId) {
		this.tokenId = tokenId;
	}

	public KategorijaTokenaEnum getKategorija() {
		return this.kategorija;
	}

	public void setKategorija(KategorijaTokenaEnum kategorija) {
		this.kategorija = kategorija;
	}

	public String getLemma() {
		return this.lemma;
	}

	public void setLemma(String lemma) {
		this.lemma = lemma;
	}

	public String getTag() {
		return this.tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	public TekstZakona getTekstZakona() {
		return this.tekstZakona;
	}

	public void setTekstZakona(TekstZakona tekstZakona) {
		this.tekstZakona = tekstZakona;
	}
	
	public Integer getPosition() {
		return position;
	}

	public void setPosition(Integer position) {
		this.position = position;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + (kategorija == null ? 0 : kategorija.hashCode());
		hash = hash * prime + (lemma == null ? 0 : lemma.hashCode());
		return hash;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
			
		if (obj == null) {
			return false;
		}
			
		if (getClass() != obj.getClass()) {
			return false;
		}
		Token other = (Token) obj;
		if (kategorija == null) {
			if (other.kategorija != null) {
				return false;
			}
			else {
				return this == obj;
			}
		}
		else if (!kategorija.equals(other.kategorija)) {
			return false;
		}
			
		
		//kategorije su im jednake, sad još provjera lemme
		if (lemma == null) {
			if (other.lemma != null) {
				return false;
			}
			else {
				return this == obj;
			}
		}
		else {
			if (!lemma.equals(other.lemma)) {
				return false;
			}
		}
			
		return true;
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