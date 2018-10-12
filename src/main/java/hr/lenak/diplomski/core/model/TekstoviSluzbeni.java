package hr.lenak.diplomski.core.model;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;


/**
 * The persistent class for the TEKSTOVI_SLUZBENI database table.
 * 
 */
@Entity
@Table(name="TEKSTOVI_SLUZBENI")
public class TekstoviSluzbeni implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long tsiId;
	private String chrset;
	private String ctxprivate;
	private String euLink;
	private BigDecimal sort;
	private byte[] tekst;
	private SluzbeniDijelovi sluzbeniDijelovi;

	@Id
	@SequenceGenerator(name="TEKSTOVI_SLUZBENI_TSIID_GENERATOR", sequenceName="SEQUENCEGENERATOR")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TEKSTOVI_SLUZBENI_TSIID_GENERATOR")
	@Column(name="TSI_ID", nullable = false)
	public Long getTsiId() {
		return this.tsiId;
	}

	public void setTsiId(Long tsiId) {
		this.tsiId = tsiId;
	}

	@Column(name = "CHRSET", length = 30)
	public String getChrset() {
		return this.chrset;
	}

	public void setChrset(String chrset) {
		this.chrset = chrset;
	}

	@Column(name = "CTXPRIVATE", length = 1)
	public String getCtxprivate() {
		return this.ctxprivate;
	}

	public void setCtxprivate(String ctxprivate) {
		this.ctxprivate = ctxprivate;
	}

	@Column(name="EU_LINK", length = 20)
	public String getEuLink() {
		return this.euLink;
	}

	public void setEuLink(String euLink) {
		this.euLink = euLink;
	}

	@Column(name="SORT", nullable = false)
	public BigDecimal getSort() {
		return this.sort;
	}

	public void setSort(BigDecimal sort) {
		this.sort = sort;
	}

	@Lob
	@Column(name = "TEKST")
	public byte[] getTekst() {
		return this.tekst;
	}

	public void setTekst(byte[] tekst) {
		this.tekst = tekst;
	}

	//bi-directional many-to-one association to SluzbeniDijelovi
	@ManyToOne
	@JoinColumn(name="SDO_SDO_ID", nullable = false)
	public SluzbeniDijelovi getSluzbeniDijelovi() {
		return this.sluzbeniDijelovi;
	}

	public void setSluzbeniDijelovi(SluzbeniDijelovi sluzbeniDijelovi) {
		this.sluzbeniDijelovi = sluzbeniDijelovi;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((tsiId == null) ? 0 : tsiId.hashCode());
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
		TekstoviSluzbeni other = (TekstoviSluzbeni) obj;
		if (tsiId == null) {
			if (other.tsiId != null) {
				return false;
			}
			else {
				return this == obj;
			}
		}
		else if (!tsiId.equals(other.tsiId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getClass().getSimpleName());
		sb.append(" [");
		sb.append("id=").append(tsiId);
		sb.append("]");
		return sb.toString();
	}

}