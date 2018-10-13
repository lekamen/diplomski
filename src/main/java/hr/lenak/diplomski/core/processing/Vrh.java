package hr.lenak.diplomski.core.processing;

import hr.lenak.diplomski.core.model.Token;

public class Vrh {

	private Token token;
	private Double value = 1D;
	
	public Vrh() {}
	
	public Vrh(Token token) {
		this.token = token;
	}
	
	public Token getToken() {
		return token;
	}
	public void setToken(Token token) {
		this.token = token;
	}
	public Double getValue() {
		return value;
	}
	public void setValue(Double value) {
		this.value = value;
	}
	
	@Override
	public int hashCode() {
		return token.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		return token.equals(obj);
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Vrh[token=").append(token)
			.append(",vrijednost=").append(value).append("]");
		
		return sb.toString();
	}
}
