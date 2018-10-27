package hr.lenak.diplomski.core.model;

import java.io.Serializable;
import javax.persistence.*;

import hr.lenak.diplomski.web.util.Repositories;



/**
 * The persistent class for the RIJEC database table.
 * 
 */
@Entity
@Table(name = "RIJEC")
public class Rijec implements Serializable {
	private static final long serialVersionUID = 1L;

	private Double idf;

	@Column(name="POJAVE_U_DOKUMENTU")
	private Integer pojaveUDokumentu = 0;

	@Column(name="POJAVE_U_KORPUSU")
	private Integer pojaveUKorpusu = 0;

	@Column(name="RESULT")
	private Double result;

	@Column(name="TEKST_ZAKONA_ID", nullable = false)
	private Long tekstZakonaId;

	private Double tf;

	@Id
	@Column(name="TOKEN_ID", nullable = false)
	private Long tokenId;
	
	@Transient
	private Token token;

	public Rijec() {
	}

	public Double getIdf() {
		return this.idf;
	}

	public void setIdf(Double idf) {
		this.idf = idf;
	}

	public Integer getPojaveUDokumentu() {
		return this.pojaveUDokumentu;
	}

	public void setPojaveUDokumentu(Integer pojaveUDokumentu) {
		this.pojaveUDokumentu = pojaveUDokumentu;
	}

	public Integer getPojaveUKorpusu() {
		return this.pojaveUKorpusu;
	}

	public void setPojaveUKorpusu(Integer pojaveUKorpusu) {
		this.pojaveUKorpusu = pojaveUKorpusu;
	}

	public Double getResult() {
		return this.result;
	}

	public void setResult(Double result) {
		this.result = result;
	}

	public Long getTekstZakonaId() {
		return this.tekstZakonaId;
	}

	public void setTekstZakonaId(Long tekstZakonaId) {
		this.tekstZakonaId = tekstZakonaId;
	}

	public Double getTf() {
		return this.tf;
	}

	public void setTf(Double tf) {
		this.tf = tf;
	}

	public Long getTokenId() {
		return this.tokenId;
	}

	public void setTokenId(Long tokenId) {
		this.tokenId = tokenId;
	}
	
	@Transient
	public Token getToken() {
		if (token == null) {
			token = Repositories.tokenRepository.findById(tokenId);
		}
		return token;
	}
	
	@Transient
	public Rijec setToken(Token token) {
		this.token = token;
		return this;
	}

	@Override
	public int hashCode() {
		return getToken().hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Rijec))
			return false;
		Rijec other = (Rijec) obj;
		return getToken().equals(other.getToken());
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Rijec[token=").append(getToken())
			.append(",pojaveUDokumentu=").append(pojaveUDokumentu)
			.append(",pojaveUKorpusu=").append(pojaveUKorpusu)
			.append(",tf=").append(tf)
			.append(",idf=").append(idf).append("]");
		
		return sb.toString();
	}
}