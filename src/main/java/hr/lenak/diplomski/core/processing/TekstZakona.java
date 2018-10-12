package hr.lenak.diplomski.core.processing;

import java.util.List;

public class TekstZakona {
	
	private Integer brojFilea;
	private List<Token> naslov;
	private List<Token> story;
	
	public Integer getBrojFilea() {
		return brojFilea;
	}
	public void setBrojFilea(Integer brojFilea) {
		this.brojFilea = brojFilea;
	}
	public List<Token> getNaslov() {
		return naslov;
	}
	public void setNaslov(List<Token> naslov) {
		this.naslov = naslov;
	}
	public List<Token> getStory() {
		return story;
	}
	public void setStory(List<Token> story) {
		this.story = story;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((brojFilea == null) ? 0 : brojFilea.hashCode());
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
		TekstZakona other = (TekstZakona) obj;
		if (brojFilea == null) {
			if (other.brojFilea != null) {
				return false;
			}
			else {
				return this == obj;
			}
		}
		else if (!brojFilea.equals(other.brojFilea))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("TekstZakona[brojFilea=")
			.append(brojFilea).append(",story=[");
		story.stream().forEach(t -> sb.append(t).append(", "));
		sb.append("]");
		return sb.toString();
	}
}
