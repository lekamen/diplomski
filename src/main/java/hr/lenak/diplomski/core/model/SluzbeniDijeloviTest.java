package hr.lenak.diplomski.core.model;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;


/**
 * The persistent class for the SLUZBENI_DIJELOVI_TEST database table.
 * 
 */
@Entity
@Table(name="SLUZBENI_DIJELOVI_TEST")
public class SluzbeniDijeloviTest implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long sdoId;
	private String donositelj;
	private String fileName;
	private String kljucneRijeci;
	private String naslov;
	private String potpisnik;
	private String sortIndex;
	private BigDecimal stranica;
	private TipoviDokumenata tipDokumenta;
	private NarodneNovine narodneNovine;

	@Id
	@SequenceGenerator(name="SLUZBENI_DIJELOVI_TEST_SDOID_GENERATOR", sequenceName="SEQUENCEGENERATOR")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SLUZBENI_DIJELOVI_TEST_SDOID_GENERATOR")
	@Column(name="SDO_ID", nullable = false)
	public Long getSdoId() {
		return this.sdoId;
	}

	public void setSdoId(Long sdoId) {
		this.sdoId = sdoId;
	}

	@Column(name = "DONOSITELJ", length = 1024)
	public String getDonositelj() {
		return this.donositelj;
	}

	public void setDonositelj(String donositelj) {
		this.donositelj = donositelj;
	}

	@Column(name="FILE_NAME", length = 124)
	public String getFileName() {
		return this.fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	@Column(name="KLJUCNE_RIJECI", length = 2048)
	public String getKljucneRijeci() {
		return this.kljucneRijeci;
	}

	public void setKljucneRijeci(String kljucneRijeci) {
		this.kljucneRijeci = kljucneRijeci;
	}

	@Column(name = "NASLOV", nullable = false, length = 1024)
	public String getNaslov() {
		return this.naslov;
	}

	public void setNaslov(String naslov) {
		this.naslov = naslov;
	}

	@Column(name = "POTPISNIK", length = 512)
	public String getPotpisnik() {
		return this.potpisnik;
	}

	public void setPotpisnik(String potpisnik) {
		this.potpisnik = potpisnik;
	}

	@Column(name="SORT_INDEX", length = 10)
	public String getSortIndex() {
		return this.sortIndex;
	}

	public void setSortIndex(String sortIndex) {
		this.sortIndex = sortIndex;
	}

	@Column(name = "STRANICA")
	public BigDecimal getStranica() {
		return this.stranica;
	}

	public void setStranica(BigDecimal stranica) {
		this.stranica = stranica;
	}

	//bi-directional many-to-one association to TipoviDokumenata
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="TDA_TDA_ID", nullable = false)
	public TipoviDokumenata getTipDokumenta() {
		return this.tipDokumenta;
	}

	public void setTipDokumenta(TipoviDokumenata tipDokumenta) {
		this.tipDokumenta = tipDokumenta;
	}

	//bi-directional many-to-one association to NarodneNovine
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="NN_NN_ID", nullable = false)
	public NarodneNovine getNarodneNovine() {
		return this.narodneNovine;
	}

	public void setNarodneNovine(NarodneNovine narodneNovine) {
		this.narodneNovine = narodneNovine;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((sdoId == null) ? 0 : sdoId.hashCode());
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
		SluzbeniDijeloviTest other = (SluzbeniDijeloviTest) obj;
		if (sdoId == null) {
			if (other.sdoId != null) {
				return false;
			}
			else {
				return this == obj;
			}
		}
		else if (!sdoId.equals(other.sdoId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getClass().getSimpleName());
		sb.append(" [");
		sb.append("id=").append(sdoId);
		sb.append("]");
		return sb.toString();
	}

}