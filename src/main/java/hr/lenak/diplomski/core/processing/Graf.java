package hr.lenak.diplomski.core.processing;

import java.util.HashSet;
import java.util.Set;


public class Graf {

	HashSet<Vrh> vrhovi = new HashSet<>();
	HashSet<Brid> bridovi = new HashSet<>();
	
	public HashSet<Vrh> getVrhovi() {
		return vrhovi;
	}
	public void setVrhovi(HashSet<Vrh> vrhovi) {
		this.vrhovi = vrhovi;
	}
	public HashSet<Brid> getBridovi() {
		return bridovi;
	}
	public void setBridovi(HashSet<Brid> bridovi) {
		this.bridovi = bridovi;
	}
	
	public boolean dodajVrh(Vrh vrh) {
		return vrhovi.add(vrh);
	}
	
	public boolean dodajBrid(Brid brid) {
		return bridovi.add(brid);
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Graf[vrhovi=[");
		for (Vrh v : vrhovi) {
			sb.append(v).append(",");
		}
		sb.append("],bridovi=[");
		for(Brid b : bridovi) {
			sb.append(b).append(",");
		}
		sb.append("]]");
		return sb.toString();
	}
}
