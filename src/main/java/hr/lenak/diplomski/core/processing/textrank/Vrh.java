package hr.lenak.diplomski.core.processing.textrank;

import java.util.ArrayList;
import java.util.List;

import hr.lenak.diplomski.core.model.Token;

public class Vrh {

	private Token token;
	private Double value = 1D;
	
	/** Lista koja pamti sve pojave ovog vrha u dokumentu **/
	private List<Token> svePojaveVrha = new ArrayList<>();

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
	
	public List<Token> getSvePojaveVrha() {
		return svePojaveVrha;
	}
	
	public void povecajPojavuUDokumentu(Vrh vrh) {
		svePojaveVrha.add(vrh.getToken());
	}

	@Override
	public int hashCode() {
		return token.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Vrh))
			return false;
		Vrh other = (Vrh) obj;
		return token.equals(other.getToken());
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Vrh[token=").append(token)
			.append(",vrijednost=").append(value).append("]");
		
		return sb.toString();
	}
}
