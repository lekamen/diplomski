package hr.lenak.diplomski.core.model;

import java.io.Serializable;
import javax.persistence.*;

import hr.lenak.diplomski.web.util.Repositories;


/**
 * The persistent class for the SVE_POJAVE_RIJECI database table.
 * 
 */
@Entity
@Table(name="SVE_POJAVE_RIJECI")
public class SvePojaveRijeci implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SVE_POJAVE_RIJECI_SPRID_GENERATOR", sequenceName="SPR_SEQUENCEGENERATOR", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SVE_POJAVE_RIJECI_SPRID_GENERATOR")
	@Column(name="SPR_ID")
	private Long sprId;

	@Column(name="TOKEN_ID_DRUGI")
	private Long tokenIdDrugi;

	@Column(name="TOKEN_ID_PRVI")
	private Long tokenIdPrvi;
	
	@Transient
	private Token tokenPrvi;
	
	@Transient
	private Token tokenDrugi;

	public SvePojaveRijeci() {
	}

	public Long getSprId() {
		return this.sprId;
	}

	public void setSprId(Long sprId) {
		this.sprId = sprId;
	}

	public Long getTokenIdDrugi() {
		return this.tokenIdDrugi;
	}

	public void setTokenIdDrugi(Long tokenIdDrugi) {
		this.tokenIdDrugi = tokenIdDrugi;
	}

	public Long getTokenIdPrvi() {
		return this.tokenIdPrvi;
	}

	public void setTokenIdPrvi(Long tokenIdPrvi) {
		this.tokenIdPrvi = tokenIdPrvi;
	}
	
	@Transient
	public Token getTokenPrvi() {
		if (tokenPrvi == null) {
			tokenPrvi = Repositories.tokenRepository.findById(tokenIdPrvi);
		}
		return tokenPrvi;
	}
	
	@Transient
	public Token getTokenDrugi() {
		if (tokenDrugi == null) {
			tokenDrugi = Repositories.tokenRepository.findById(tokenIdDrugi);
		}
		return tokenDrugi;
	}

}