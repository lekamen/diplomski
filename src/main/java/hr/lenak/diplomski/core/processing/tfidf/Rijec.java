package hr.lenak.diplomski.core.processing.tfidf;

import hr.lenak.diplomski.core.model.Token;

public class Rijec {
	private Token token;
	private Integer pojaveUDokumentu = 0;
	private Integer pojaveUKorpusu = 0;
	private Double tf;
	private Double idf;
	private Double result;
	
	public Rijec(Token token) {
		this.token = token;
	}
	public Token getToken() {
		return token;
	}
	public Double getTf() {
		return tf;
	}
	public void setTf(Double tf) {
		this.tf = tf;
	}
	public Double getIdf() {
		return idf;
	}
	public void setIdf(Double idf) {
		this.idf = idf;
	}
	public Double getResult() {
		return result;
	}
	public void setResult(Double result) {
		this.result = result;
	}
	public Integer getPojaveUDokumentu() {
		return pojaveUDokumentu;
	}
	public void povecajPojavuUDokumentu() {
		pojaveUDokumentu++;
	}
	public Integer getPojaveUKorpusu() {
		return pojaveUKorpusu;
	}
	public void setPojaveUKorpusu(Integer pojaveUKorpusu) {
		this.pojaveUKorpusu = pojaveUKorpusu;
	}
	@Override
	public int hashCode() {
		return token.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Rijec))
			return false;
		Rijec other = (Rijec) obj;
		return token.equals(other.getToken());
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Rijec[token=").append(token)
			.append(",pojaveUDokumentu=").append(pojaveUDokumentu)
			.append(",pojaveUKorpusu=").append(pojaveUKorpusu)
			.append(",tf=").append(tf)
			.append(",idf=").append(idf).append("]");
		
		return sb.toString();
	}	
	
}
