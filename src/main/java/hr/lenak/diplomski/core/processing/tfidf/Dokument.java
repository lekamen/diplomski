package hr.lenak.diplomski.core.processing.tfidf;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;


public class Dokument {

	private HashSet<Rijec> rijeci = new HashSet<>();
	private Rijec najcescaRijecUDokumentu;
	private static final int BROJ_KLJUCNIH_RIJECI = 8;

	public HashSet<Rijec> getRijeci() {
		return rijeci;
	}

	public void setRijeci(HashSet<Rijec> rijeci) {
		this.rijeci = rijeci;
	}
	
	public boolean dodajRijec(Rijec rijec) {
		boolean isNovaRijec = rijeci.add(rijec);
		if (isNovaRijec) {
			rijec.povecajPojavuUDokumentu();
		}
		else {
			pronadjiRijec(rijec).povecajPojavuUDokumentu();
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
		Collections.sort(sorted, new Comparator<Rijec>() {
			@Override
			public int compare(Rijec o1, Rijec o2) {
				return o2.getPojaveUDokumentu().compareTo(o1.getPojaveUDokumentu());
			}
		});
		najcescaRijecUDokumentu = sorted.get(0);
		return najcescaRijecUDokumentu;
	}
	
	public void calculateTfsZaRijeci() {
		for (Rijec rijec : rijeci) {
			Double tf = 0.5 + 0.5 * ((double)rijec.getPojaveUDokumentu()/getNajcescaRijecUDokumentu().getPojaveUDokumentu());
			rijec.setTf(tf);
		}
	}

	public List<String> returnKeywords() {
		for (Rijec rijec : rijeci) {
			rijec.setResult(rijec.getTf() * rijec.getIdf());
		}
		List<Rijec> sorted = new ArrayList<>(rijeci);
		Collections.sort(sorted, new Comparator<Rijec>() {
			@Override
			public int compare(Rijec o1, Rijec o2) {
				return o2.getResult().compareTo(o1.getResult());
			}
		});
		return sorted.subList(0, BROJ_KLJUCNIH_RIJECI).stream()
			.map(rijec -> rijec.getToken().getLemma()).collect(Collectors.toList());
	}
	
	public boolean sadrziRijec(Rijec rijec) {
		return rijeci.contains(rijec);
	}
	
	public Rijec getNajcescaRijecUDokumentu() {
		if (najcescaRijecUDokumentu == null) {
			return nadjiNajcescuRijecUDokumentu();
		}
		return najcescaRijecUDokumentu;
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
