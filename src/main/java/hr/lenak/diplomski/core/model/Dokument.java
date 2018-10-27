package hr.lenak.diplomski.core.model;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;


/**
 * The persistent class for the DOKUMENT database table.
 * 
 */
@Entity
@NamedQuery(name="Dokument.findAll", query="SELECT d FROM Dokument d")
public class Dokument implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name="NAJCESCA_RIJEC_ID")
	private Long najcescaRijecId;

	@Id
	@Column(name="TEKST_ZAKONA_ID")
	private Long tekstZakonaId;

	public Dokument() {
	}

	public Long getNajcescaRijecId() {
		return this.najcescaRijecId;
	}

	public void setNajcescaRijecId(Long najcescaRijecId) {
		this.najcescaRijecId = najcescaRijecId;
	}

	public Long getTekstZakonaId() {
		return this.tekstZakonaId;
	}

	public void setTekstZakonaId(Long tekstZakonaId) {
		this.tekstZakonaId = tekstZakonaId;
	}

}