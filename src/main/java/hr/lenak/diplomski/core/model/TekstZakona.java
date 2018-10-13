package hr.lenak.diplomski.core.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;


/**
 * The persistent class for the TEKST_ZAKONA database table.
 * 
 */
@Entity
@Table(name="TEKST_ZAKONA")
public class TekstZakona implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TEKST_ZAKONA_TEKSTZAKONAID_GENERATOR", sequenceName="SEQUENCEGENERATOR")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TEKST_ZAKONA_TEKSTZAKONAID_GENERATOR")
	@Column(name="TEKST_ZAKONA_ID")
	private Long tekstZakonaId;

	@Column(name="BROJ_FILEA")
	private Integer brojFilea;

	@Column(name="TSI_ID")
	private Long tsiId;
	
	//bi-directional many-to-one association to Token
	@OneToMany(mappedBy="tekstZakona")
	private List<Token> tokens;

	public TekstZakona() {
	}

	public Long getTekstZakonaId() {
		return this.tekstZakonaId;
	}

	public void setTekstZakonaId(Long tekstZakonaId) {
		this.tekstZakonaId = tekstZakonaId;
	}

	public Integer getBrojFilea() {
		return this.brojFilea;
	}

	public void setBrojFilea(Integer brojFilea) {
		this.brojFilea = brojFilea;
	}

	public Long getTsiId() {
		return this.tsiId;
	}

	public void setTsiId(Long tsiId) {
		this.tsiId = tsiId;
	}
	
	public List<Token> getTokens() {
		return this.tokens;
	}

	public void setTokens(List<Token> tokens) {
		this.tokens = tokens;
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
		TekstZakona other = (TekstZakona) obj;
		if (brojFilea == null) {
			if (other.brojFilea != null) {
				return false;
			}
			else {
				return this == obj;
			}
		}
		else if (!brojFilea.equals(other.brojFilea))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("TekstZakona[brojFilea=")
			.append(brojFilea).append("]");
		return sb.toString();
	}

}