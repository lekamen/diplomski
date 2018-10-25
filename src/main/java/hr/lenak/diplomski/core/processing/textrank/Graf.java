package hr.lenak.diplomski.core.processing.textrank;

import java.util.HashSet;
import java.util.stream.Collectors;


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
	
	/** Metoda dodaje i vraća novi vrh, ili vraća već postojeći ukoliko je vrh u grafu */
	public Vrh dodajVrh(Vrh vrh) {
		boolean isNoviVrh = vrhovi.add(vrh);
		Vrh vrhToReturn;
		if (isNoviVrh) {
			vrh.povecajPojavuUDokumentu(vrh);
			vrhToReturn = vrh;
		}
		else {
			vrhToReturn = nadjiVrh(vrh);
			vrhToReturn.povecajPojavuUDokumentu(vrh);
		}
		return vrhToReturn;
	}
	
	public boolean dodajBrid(Brid brid) {
		if (!bridovi.add(brid)) {
			//brid već postoji, dodaj njegovu pojavu
			nadjiBrid(brid).dodajPojavuBrida(brid);
			return false;
		}
		
		return true;
	}
	
	private Brid nadjiBrid(Brid brid) {
		for (Brid b : bridovi) {
			if (b.equals(brid)) {
				return b;
			}
		}
		return null;
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
		HashSet<Vrh> susjedi = new HashSet<>();
		for (Brid brid : bridovi) {
			Vrh susjed = brid.vratiSusjedniVrh(vrh);
			if (susjed != null) {
				susjedi.add(susjed);
			}
		}
		return susjedi;
	}
	
	public Integer getUkupnaSumaBridovaIzVrha(Vrh vrh) {
		Integer suma = 0;
		for (Brid brid : bridovi) {
			if (brid.isVrhNaBridu(vrh)) {
				suma += brid.getUkupanBroj();
			}
		}
		return suma;
	}
	
	public void ukloniVrhIBridove(Vrh vrh) {
		vrhovi.remove(vrh);
		bridovi = bridovi.stream().filter(brid -> !brid.isVrhNaBridu(vrh)).collect(Collectors.toCollection(HashSet::new));
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
