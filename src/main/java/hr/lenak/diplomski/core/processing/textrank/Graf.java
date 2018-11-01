package hr.lenak.diplomski.core.processing.textrank;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.stream.Collectors;


public class Graf {

	ArrayList<Vrh> vrhovi = new ArrayList<Vrh>();
	HashSet<Brid> bridovi = new HashSet<Brid>();
	
	public ArrayList<Vrh> getVrhovi() {
		return vrhovi;
	}
	public void setVrhovi(ArrayList<Vrh> vrhovi) {
		this.vrhovi = vrhovi;
	}
	public HashSet<Brid> getBridovi() {
		return bridovi;
	}
	public void setBridovi(HashSet<Brid> bridovi) {
		this.bridovi = bridovi;
	}
	
	/** Metoda dodaje i vraća novi vrh, ili vraća već postojeći ukoliko je vrh u grafu */
	public Vrh dodajVrh(Vrh vrh) {
		boolean postojiVrh = vrhovi.contains(vrh);

		Vrh vrhToReturn;
		if (postojiVrh) {
			vrhToReturn = nadjiVrh(vrh);
			vrhToReturn.povecajPojavuUDokumentu(vrh);
		}
		else {
			vrhovi.add(vrh);
			vrh.povecajPojavuUDokumentu(vrh);
			vrhToReturn = vrh;
		}
		
		return vrhToReturn;
	}
	
	public boolean dodajBrid(Brid brid) {
		return bridovi.add(brid);
	}

	public Vrh nadjiVrh(Vrh vrh) {
		for (Vrh v : vrhovi) {
			if (v.equals(vrh)) {
				return v;
			}
		}
		return null;
	}
	
	public HashSet<Vrh> nadjiSusjedeZaVrh(Vrh vrh) {
		HashSet<Vrh> susjedi = new HashSet<Vrh>();
		for (Brid brid : bridovi) {
			Vrh susjed = brid.vratiSusjedniVrh(vrh);
			if (susjed != null) {
				susjedi.add(susjed);
			}
		}
		return susjedi;
	}

	public void ukloniVrhIBridove(Vrh vrh) {
		vrhovi.remove(vrh);
		bridovi = bridovi.stream().filter(brid -> !brid.isVrhNaBridu(vrh)).collect(Collectors.toCollection(HashSet<Brid>::new));
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Graf[vrhovi=[\n");
		for (Vrh v : vrhovi) {
			sb.append(v).append(",\n");
		}
		sb.append("],bridovi=[\n");
		for(Brid b : bridovi) {
			sb.append(b).append(",\n");
		}
		sb.append("]]");
		return sb.toString();
	}
}
