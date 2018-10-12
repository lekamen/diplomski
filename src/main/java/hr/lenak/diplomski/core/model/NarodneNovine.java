package hr.lenak.diplomski.core.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * The persistent class for the NARODNE_NOVINE database table.
 * 
 */
@Entity
@Table(name="NARODNE_NOVINE")
public class NarodneNovine implements Serializable {
	private static final long serialVersionUID = 1L;


	private Long nnId;
	private BigDecimal broj;
	private LocalDateTime datumIzdanja;
	private List<SluzbeniDijelovi> sluzbeniDijelovis;
	
	@Id
	@SequenceGenerator(name="NARODNE_NOVINE_NNID_GENERATOR", sequenceName="SEQUENCEGENERATOR")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="NARODNE_NOVINE_NNID_GENERATOR")
	@Column(name="NN_ID", nullable = false)
	public Long getNnId() {
		return this.nnId;
	}

	public void setNnId(Long nnId) {
		this.nnId = nnId;
	}

	@Column(name = "BROJ", nullable = false)
	public BigDecimal getBroj() {
		return this.broj;
	}

	public void setBroj(BigDecimal broj) {
		this.broj = broj;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="DATUM_IZDANJA", nullable = false)
	public LocalDateTime getDatumIzdanja() {
		return this.datumIzdanja;
	}

	public void setDatumIzdanja(LocalDateTime datumIzdanja) {
		this.datumIzdanja = datumIzdanja;
	}

	//bi-directional many-to-one association to SluzbeniDijelovi
	@OneToMany(mappedBy= "narodneNovine", fetch = FetchType.LAZY)
	public List<SluzbeniDijelovi> getSluzbeniDijelovis() {
		return this.sluzbeniDijelovis;
	}

	public void setSluzbeniDijelovis(List<SluzbeniDijelovi> sluzbeniDijelovis) {
		this.sluzbeniDijelovis = sluzbeniDijelovis;
	}

	public SluzbeniDijelovi addSluzbeniDijelovi(SluzbeniDijelovi sluzbeniDijelovi) {
		getSluzbeniDijelovis().add(sluzbeniDijelovi);
		sluzbeniDijelovi.setNarodneNovine(this);

		return sluzbeniDijelovi;
	}

	public SluzbeniDijelovi removeSluzbeniDijelovi(SluzbeniDijelovi sluzbeniDijelovi) {
		getSluzbeniDijelovis().remove(sluzbeniDijelovi);
		sluzbeniDijelovi.setNarodneNovine(null);

		return sluzbeniDijelovi;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((nnId == null) ? 0 : nnId.hashCode());
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
		NarodneNovine other = (NarodneNovine) obj;
		if (nnId == null) {
			if (other.nnId != null) {
				return false;
			}
			else {
				return this == obj;
			}
		}
		else if (!nnId.equals(other.nnId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getClass().getSimpleName());
		sb.append(" [");
		sb.append("id=").append(nnId);
		sb.append("]");
		return sb.toString();
	}

}