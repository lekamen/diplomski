package hr.lenak.diplomski.core.processing.tfidf;

import java.util.ArrayList;
import java.util.List;

import hr.lenak.diplomski.core.model.Token;

public class TfRijec {
	private Token token;
	private Integer pojaveUDokumentu = 0;
	private Double tf;
	private Double idf;
	private Double result;
	
	/** Lista koja pamti sve pojave ove riječi u dokumentu **/
	private List<Token> svePojaveRijeci = new ArrayList<Token>();
	
	public TfRijec(Token token) {
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
	public void setPojaveUDokumentu(Integer pojaveUDokumentu) {
		this.pojaveUDokumentu = pojaveUDokumentu;
	}
	public void povecajPojavuUDokumentu(TfRijec rijec) {
		pojaveUDokumentu++;
		svePojaveRijeci.add(rijec.getToken());
	}

	public List<Token> getSvePojaveRijeci() {
		return svePojaveRijeci;
	}
	
	@Override
	public int hashCode() {
		return token.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof TfRijec))
			return false;
		TfRijec other = (TfRijec) obj;
		return token.equals(other.getToken());
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Rijec[token=").append(token)
			.append(",pojaveUDokumentu=").append(pojaveUDokumentu)
			.append(",tf=").append(tf)
			.append(",idf=").append(idf).append("]");
		
		return sb.toString();
	}	
	
}