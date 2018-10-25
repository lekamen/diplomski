package hr.lenak.diplomski.core.processing.tfidf;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import hr.lenak.diplomski.core.model.TekstZakona;
import hr.lenak.diplomski.core.model.Token;
import hr.lenak.diplomski.core.processing.KljucnaRijec;


public class Dokument {

	private HashSet<Rijec> rijeci = new HashSet<>();
	private Rijec najcescaRijecUDokumentu;
	private TekstZakona tekstZakona;
	
	public Dokument(TekstZakona tekstZakona) {
		this.tekstZakona = tekstZakona;
	}

	public HashSet<Rijec> getRijeci() {
		return rijeci;
	}

	public void setRijeci(HashSet<Rijec> rijeci) {
		this.rijeci = rijeci;
	}
	
	public boolean dodajRijec(Rijec rijec) {
		boolean isNovaRijec = rijeci.add(rijec);
		if (isNovaRijec) {
			rijec.povecajPojavuUDokumentu(rijec);
		}
		else {
			pronadjiRijec(rijec).povecajPojavuUDokumentu(rijec);
		}
		return isNovaRijec;
	}
	
	public Rijec pronadjiRijec(Rijec rijec) {
		for (Rijec r : rijeci) {
			if (r.equals(rijec)) {
				return r;
			}
		}
		return null;
	}
	
	private Rijec nadjiNajcescuRijecUDokumentu() {
		ArrayList<Rijec> sorted = new ArrayList<Rijec>(rijeci);
		Collections.sort(sorted, (o1, o2) -> o2.getPojaveUDokumentu().compareTo(o1.getPojaveUDokumentu()));
		najcescaRijecUDokumentu = sorted.get(0);
		return najcescaRijecUDokumentu;
	}
	
	public void calculateTfsZaRijeci() {
		for (Rijec rijec : rijeci) {
			Double tf = 0.5 + 0.5 * ((double)rijec.getPojaveUDokumentu()/getNajcescaRijecUDokumentu().getPojaveUDokumentu());
			rijec.setTf(tf);
		}
	}

	public List<KljucnaRijec> returnKeywords(int brojKljucnihRijeci) {
		for (Rijec rijec : rijeci) {
			rijec.setResult(rijec.getTf() * rijec.getIdf());
		}
		List<Rijec> sorted = new ArrayList<>(rijeci);
		Collections.sort(sorted, (o1, o2) -> o2.getResult().compareTo(o1.getResult()));
			
		return TfIdfUtils.spojiSusjedneKljucneRijeci(sorted.subList(0, brojKljucnihRijeci * 2 > sorted.size() ? sorted.size() : brojKljucnihRijeci * 2), brojKljucnihRijeci);
	}
	
	public boolean sadrziRijec(Rijec rijec) {
		return rijeci.contains(rijec);
	}
	
	public boolean sadrziToken(Token token) {
		return sadrziRijec(new Rijec(token));
	}
	
	public Rijec getNajcescaRijecUDokumentu() {
		if (najcescaRijecUDokumentu == null) {
			return nadjiNajcescuRijecUDokumentu();
		}
		return najcescaRijecUDokumentu;
	}
	
	public TekstZakona getTekstZakona() {
		return tekstZakona;
	}

	public void setTekstZakona(TekstZakona tekstZakona) {
		this.tekstZakona = tekstZakona;
	}
	
	@Override
	public int hashCode() {
		return tekstZakona.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Dokument)) {
			return false;
		}
		Dokument other = (Dokument) obj;
		return tekstZakona.equals(other.tekstZakona);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Dokument[rijeci=[\n");
		for (Rijec r : rijeci) {
			sb.append(r).append(",\n");
		}
		sb.append("]]");
		return sb.toString();
	}
}
