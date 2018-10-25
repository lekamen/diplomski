package hr.lenak.diplomski.core.processing.tfidf;

import static hr.lenak.diplomski.core.model.enums.KategorijaTokenaEnum.ADJECTIVE;
import static hr.lenak.diplomski.core.model.enums.KategorijaTokenaEnum.NOUN;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import hr.lenak.diplomski.core.model.TekstZakona;
import hr.lenak.diplomski.core.model.Token;
import hr.lenak.diplomski.core.processing.KljucnaRijec;

public class TfIdfUtils {
	public static Dokument konstruirajDokument(List<Token> tokeni, TekstZakona tekstZakona) {
		Dokument dokument = new Dokument(tekstZakona);
		for (int i = 0; i < tokeni.size(); i++) {
			Token t = tokeni.get(i);
			//prodji sintakticki filter
			if (!passesSyntacticFilter(t)) {
				continue;
			}

			Rijec rijec = new Rijec(t);
			dokument.dodajRijec(rijec);
		}
		
		return dokument;
	}
	
	private static boolean passesSyntacticFilter(Token t) {
		//dopuštamo imenice koje nisu vlastite i pridjeve
		return (t.getKategorija() == NOUN && !t.getTag().startsWith("Np"))  || t.getKategorija() == ADJECTIVE;
	}
	
	
	public static List<KljucnaRijec> spojiSusjedneKljucneRijeci(List<Rijec> rijeciKandidati, int brojKljucnihRijeci) {
		//sortiraj ih po poziciji u tekstu - ako su neki susjedni, spoji u jednu kljucnu rijec
		List<Token> svePojaveSvihKljucnihRijeci = new ArrayList<Token>();
		rijeciKandidati.stream().forEach(rijec -> svePojaveSvihKljucnihRijeci.addAll(rijec.getSvePojaveRijeci()));
		Collections.sort(svePojaveSvihKljucnihRijeci, (o1, o2) -> o1.getPosition().compareTo(o2.getPosition()));

		int i = 0;
		int n = svePojaveSvihKljucnihRijeci.size();
		Set<Token> posjeceneKljucneRijeci = new HashSet<>();
		List<KljucnaRijec> kljucneRijeci = new ArrayList<>();
		while (i < n) {
			Token trenutniToken = svePojaveSvihKljucnihRijeci.get(i);
			
			if (posjeceneKljucneRijeci.contains(trenutniToken)) {
				i++;
				continue;
			}
			
			posjeceneKljucneRijeci.add(trenutniToken);
			
			int position = trenutniToken.getPosition();
			String keyword = svePojaveSvihKljucnihRijeci.get(i).getLemma();
			Double vrijednost = rijeciKandidati.get(rijeciKandidati.indexOf(new Rijec(trenutniToken))).getResult();
			if (i < n - 1) {
				int next = svePojaveSvihKljucnihRijeci.get(i + 1).getPosition();
				while (next == position + 1) {
					Token iduciToken = svePojaveSvihKljucnihRijeci.get(i + 1);
					
					if (posjeceneKljucneRijeci.contains(iduciToken)) {
						i++;
						if (i >= n - 1) {
							break;
						}
						next = svePojaveSvihKljucnihRijeci.get(i + 1).getPosition();
						continue;
					}
					posjeceneKljucneRijeci.add(iduciToken);
					
					keyword += " " + iduciToken.getLemma();
					vrijednost += rijeciKandidati.get(rijeciKandidati.indexOf(new Rijec(iduciToken))).getResult();
					position = next;
					i++;
					if (i >= n - 1) {
						break;
					}
					next = svePojaveSvihKljucnihRijeci.get(i + 1).getPosition();
				}
			}
			kljucneRijeci.add(new KljucnaRijec(keyword, vrijednost));
			i++;
		}
		
		for (KljucnaRijec s : kljucneRijeci) {
			System.out.println("kljucne rijeci: " + s);
		}
		Collections.sort(kljucneRijeci, (o1, o2) -> o2.getVrijednost().compareTo(o1.getVrijednost())); 
		return kljucneRijeci.subList(0, brojKljucnihRijeci > kljucneRijeci.size() ? kljucneRijeci.size() : brojKljucnihRijeci);
	}
}
