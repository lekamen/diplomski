package hr.lenak.diplomski.core.processing.tfidf;

import static hr.lenak.diplomski.core.model.enums.KategorijaTokenaEnum.ADJECTIVE;
import static hr.lenak.diplomski.core.model.enums.KategorijaTokenaEnum.NOUN;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import hr.lenak.diplomski.core.model.Rijec;
import hr.lenak.diplomski.core.model.TekstZakona;
import hr.lenak.diplomski.core.model.Token;
import hr.lenak.diplomski.core.processing.KljucnaRijec;
import hr.lenak.diplomski.web.util.Repositories;

public class TfIdfUtils {
	public static TfDokument konstruirajDokument(List<Token> tokeni, TekstZakona tekstZakona) {
		TfDokument dokument = new TfDokument(tekstZakona);
		for (int i = 0; i < tokeni.size(); i++) {
			Token t = tokeni.get(i);
			//prodji sintakticki filter
			if (!passesSyntacticFilter(t)) {
				continue;
			}

			TfRijec rijec = new TfRijec(t);
			dokument.dodajRijec(rijec);
		}
		
		return dokument;
	}
	
	private static boolean passesSyntacticFilter(Token t) {
		//dopuštamo imenice koje nisu vlastite i pridjeve
		//lemma mora biti != null
		return t.getLemma() != null &&
			((t.getKategorija() == NOUN && !t.getTag().startsWith("Np"))  || t.getKategorija() == ADJECTIVE);
	}
	
	
	public static List<KljucnaRijec> spojiSusjedneKljucneRijeci(List<Rijec> rijeciKandidati, int brojKljucnihRijeci) {
		//sortiraj ih po poziciji u tekstu - ako su neki susjedni, spoji u jednu kljucnu rijec
		List<Token> svePojaveSvihKljucnihRijeci = new ArrayList<Token>();
		rijeciKandidati.stream().forEach(rijec -> svePojaveSvihKljucnihRijeci.addAll(
				Repositories.svePojaveRijeciRepository.findAllPojaveRijeciForPrviToken(rijec.getTokenId())
					.stream().map(spr -> spr.getTokenDrugi()).collect(Collectors.toList())));
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
			
			Double vrijednost = rijeciKandidati.get(rijeciKandidati.indexOf(new Rijec().setToken(trenutniToken))).getResult();
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
					vrijednost += rijeciKandidati.get(rijeciKandidati.indexOf(new Rijec().setToken(iduciToken))).getResult();
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

		Collections.sort(kljucneRijeci, (o1, o2) -> o2.getVrijednost().compareTo(o1.getVrijednost())); 
		return kljucneRijeci.subList(0, brojKljucnihRijeci > kljucneRijeci.size() ? kljucneRijeci.size() : brojKljucnihRijeci);
	}
	
	public static List<KljucnaRijec> findKeywords(List<Rijec> rijeci, int brojKljucnihRijeci) {
		Collections.sort(rijeci, (o1, o2) -> o2.getResult().compareTo(o1.getResult()));
		return TfIdfUtils.spojiSusjedneKljucneRijeci(rijeci.subList(0, brojKljucnihRijeci * 2 > rijeci.size() ? rijeci.size() : brojKljucnihRijeci * 2), brojKljucnihRijeci);
	}
	
}
