package hr.lenak.diplomski.core.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import hr.lenak.diplomski.core.model.enums.DaNeEnum;


/**
 * The persistent class for the TIPOVI_DOKUMENATA database table.
 * 
 */
@Entity
@Table(name="TIPOVI_DOKUMENATA")
public class TipoviDokumenata implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long tdaId;
	private DaNeEnum tdaAktivan;
	private String tdaNaziv;
	private BigDecimal tdaTip;

	@Id
	@SequenceGenerator(name="TIPOVI_DOKUMENATA_TDAID_GENERATOR", sequenceName="SEQUENCEGENERATOR")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TIPOVI_DOKUMENATA_TDAID_GENERATOR")
	@Column(name="TDA_ID", nullable = false)
	public Long getTdaId() {
		return this.tdaId;
	}

	public void setTdaId(Long tdaId) {
		this.tdaId = tdaId;
	}

	@Column(name="TDA_AKTIVAN", nullable = false)
	public DaNeEnum getTdaAktivan() {
		return this.tdaAktivan;
	}

	public void setTdaAktivan(DaNeEnum tdaAktivan) {
		this.tdaAktivan = tdaAktivan;
	}

	@Column(name="TDA_NAZIV", nullable = false, length = 125)
	public String getTdaNaziv() {
		return this.tdaNaziv;
	}

	public void setTdaNaziv(String tdaNaziv) {
		this.tdaNaziv = tdaNaziv;
	}

	@Column(name="TDA_TIP", length = 1)
	public BigDecimal getTdaTip() {
		return this.tdaTip;
	}

	public void setTdaTip(BigDecimal tdaTip) {
		this.tdaTip = tdaTip;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((tdaId == null) ? 0 : tdaId.hashCode());
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
		TipoviDokumenata other = (TipoviDokumenata) obj;
		if (tdaId == null) {
			if (other.tdaId != null) {
				return false;
			}
			else {
				return this == obj;
			}
		}
		else if (!tdaId.equals(other.tdaId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getClass().getSimpleName());
		sb.append(" [");
		sb.append("id=").append(tdaId);
		sb.append("]");
		return sb.toString();
	}

}