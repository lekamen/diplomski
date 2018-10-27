package hr.lenak.diplomski.core.processing.tfidf;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import hr.lenak.diplomski.core.model.TekstZakona;
import hr.lenak.diplomski.core.model.Token;
import hr.lenak.diplomski.core.processing.KljucnaRijec;


public class TfDokument {

	private HashSet<TfRijec> rijeci = new HashSet<>();
	private TfRijec najcescaRijecUDokumentu;
	private TekstZakona tekstZakona;
	
	public TfDokument(TekstZakona tekstZakona) {
		this.tekstZakona = tekstZakona;
	}

	public HashSet<TfRijec> getRijeci() {
		return rijeci;
	}

	public void setRijeci(HashSet<TfRijec> rijeci) {
		this.rijeci = rijeci;
	}
	
	public boolean dodajRijec(TfRijec rijec) {
		boolean isNovaRijec = rijeci.add(rijec);
		if (isNovaRijec) {
			rijec.povecajPojavuUDokumentu(rijec);
		}
		else {
			pronadjiRijec(rijec).povecajPojavuUDokumentu(rijec);
		}
		return isNovaRijec;
	}
	
	public TfRijec pronadjiRijec(TfRijec rijec) {
		for (TfRijec r : rijeci) {
			if (r.equals(rijec)) {
				return r;
			}
		}
		return null;
	}
	
	private TfRijec nadjiNajcescuRijecUDokumentu() {
		ArrayList<TfRijec> sorted = new ArrayList<TfRijec>(rijeci);
		Collections.sort(sorted, (o1, o2) -> o2.getPojaveUDokumentu().compareTo(o1.getPojaveUDokumentu()));
		najcescaRijecUDokumentu = sorted.get(0);
		return najcescaRijecUDokumentu;
	}
	
	public void calculateTfsZaRijeci() {
		for (TfRijec rijec : rijeci) {
			Double tf = 0.5 + 0.5 * ((double)rijec.getPojaveUDokumentu()/getNajcescaRijecUDokumentu().getPojaveUDokumentu());
			rijec.setTf(tf);
		}
	}

	public boolean sadrziRijec(TfRijec rijec) {
		return rijeci.contains(rijec);
	}
	
	public boolean sadrziToken(Token token) {
		return sadrziRijec(new TfRijec(token));
	}
	
	public TfRijec getNajcescaRijecUDokumentu() {
		if (najcescaRijecUDokumentu == null) {
			return nadjiNajcescuRijecUDokumentu();
		}
		return najcescaRijecUDokumentu;
	}
	
	public void setNajcescaRijecUDokumentu(TfRijec najcescaRijecUDokumentu) {
		this.najcescaRijecUDokumentu = najcescaRijecUDokumentu;
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
		if (!(obj instanceof TfDokument)) {
			return false;
		}
		TfDokument other = (TfDokument) obj;
		return tekstZakona.equals(other.tekstZakona);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Dokument[rijeci=[\n");
		for (TfRijec r : rijeci) {
			sb.append(r).append(",\n");
		}
		sb.append("]]");
		return sb.toString();
	}
}