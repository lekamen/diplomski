package hr.lenak.diplomski.core.processing.textrank;

import static hr.lenak.diplomski.core.model.enums.KategorijaTokenaEnum.ADJECTIVE;
import static hr.lenak.diplomski.core.model.enums.KategorijaTokenaEnum.NOUN;
import static hr.lenak.diplomski.core.model.enums.KategorijaTokenaEnum.PUNCTUATION;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hr.lenak.diplomski.core.model.Token;
import hr.lenak.diplomski.core.processing.KljucnaRijec;

public class GrafUtils {
	
	private static Logger log = LoggerFactory.getLogger(GrafUtils.class);
	private static final int ITERATIONS = 30;
	private static final double THRESHOLD = 0.0001;
	private static final double D = 0.85;
	private static final HashSet<String> forbiddenWords = new HashSet<>(Arrays.asList(
			"urbroj", /* pojavljuje se u svakom zakonu, nebitna za sadržaj */
			"klasa", /* pojavljuje se u svakom zakonu, nebitna za sadržaj */
			"republika", /* pojavljuje se u svakom zakonu, nebitna za sadržaj */
			"vlada", /* pojavljuje se u svakom zakonu, nebitna za sadržaj */
			"zakon", /* pojavljuje se u svakom zakonu, nebitna za sadržaj */
			"članak" /* pojavljuje se u svakom zakonu, nebitna za sadržaj */
	));
	
	public static List<Vrh> konstruirajGrafIPrimijeniAlgoritam(List<Token> tokeni, int windowSize, int brojKljucnihRijeci) {
		Graf graf = GrafUtils.konstruirajGraf(tokeni, windowSize);
		GrafUtils.makniNepovezaneVrhove(graf);
		GrafUtils.primijeniAlgoritamNaGraf(graf);
		
		return GrafUtils.evaluirajRjesenje(graf, brojKljucnihRijeci);
	}
	
	private static void makniNepovezaneVrhove(Graf graf) {
		HashMap<Vrh, Boolean> visited = new HashMap<>();
		HashSet<Vrh> vrhovi = graf.getVrhovi();
		for (Vrh vrh : vrhovi) {
			visited.put(vrh, false);
		}
		dfs(vrhovi.iterator().next(), visited, graf); //FIXME
		List<Vrh> nepovezani = nadjiNepovezaneVrhove(graf, visited);

		makniNepovezaneVrhove(graf, nepovezani);
	}
	
	private static void dfs(Vrh vrh, HashMap<Vrh, Boolean> visited, Graf graf){
		if (visited.get(vrh)) {
			return;
		}
		visited.put(vrh, true);
		for (Vrh v : graf.nadjiSusjedeZaVrh(vrh)) {
			if (visited.get(v).equals(false)) {
				dfs(v, visited, graf);
			}
		}
	}
	
	private static void makniNepovezaneVrhove(Graf graf, List<Vrh> nepovezani) {
		for (Vrh v : nepovezani) {
			graf.ukloniVrhIBridove(v);
		}
	}
	 
	private static List<Vrh> nadjiNepovezaneVrhove(Graf graf, HashMap<Vrh, Boolean> visited){
		List<Vrh> nepovezaniVrhovi = new ArrayList<>();
		for(Vrh v : graf.getVrhovi()) {
			if (visited.get(v) == false) {
				nepovezaniVrhovi.add(v);
			}
		}
		return nepovezaniVrhovi;
	}
	
	private static Graf konstruirajGraf(List<Token> tokeni, int windowSize) {
		Graf graf = new Graf();
		for (int i = 0; i < tokeni.size(); i++) {
			Token t = tokeni.get(i);
			
			//prodji sintakticki filter
			if (!passesSyntacticFilter(t)) {
				continue;
			}
			//zbog specifičnosti zakona izbacuju se neke riječi iz grafa
			if (isInForbiddenWords(t)) {
				continue;
			}
			//ubaci token u graf
			Vrh vrh = new Vrh(t);
			vrh = graf.dodajVrh(vrh);
			napraviBridoveZaVrh(i, vrh, tokeni, graf, windowSize);
		}
		
		return graf;
	}
	
	private static void napraviBridoveZaVrh(int pozicija, Vrh trenutni, List<Token> tokeni, Graf graf, int window) {
		for (int i = 1; i <= window; i++) {
			int pos = pozicija - i;
			if (pos < 0) {
				break;
			}
			
			Token susjed = tokeni.get(pos);
			//riječi su povezane unutar iste rečenice
			if (tokenEndsSentence(susjed)) {
				break;
			}
			
			if (!passesSyntacticFilter(susjed)) {
				continue;
			}
			//ne dopuštamo da je čvor povezan sam sa sobom u grafu
			if (trenutni.getToken().equals(susjed)) {
				continue;
			}
			//provjera se mora izvršiti zbog dodavanja zabranjenih riječi
			if (graf.nadjiVrh(new Vrh(susjed)) == null) {
				continue;
			}
			//susjed je u grafu, napravi brid
			graf.dodajBrid(new Brid(trenutni, graf.nadjiVrh(new Vrh(susjed))));
		}
	}
	
	private static boolean tokenEndsSentence(Token t) {
		return t.getKategorija() == PUNCTUATION && (t.getLemma().equals(".") || t.getLemma().equals("?") || t.getLemma().equals("!"));
	}
	
	private static boolean passesSyntacticFilter(Token t) {
		//dopuštamo imenice koje nisu vlastite i pridjeve
		//lemma mora biti != null
		return t.getLemma() != null &&
			((t.getKategorija() == NOUN && !t.getTag().startsWith("Np"))  || t.getKategorija() == ADJECTIVE);
	}
	
	private static boolean isInForbiddenWords(Token t) {
		return forbiddenWords.contains(t.getLemma());
	}
	
	private static Graf primijeniAlgoritamNaGraf(Graf graf) {
		HashSet<Vrh> vrhovi = graf.getVrhovi();
		for (int i = 0; i < ITERATIONS; i++) {
			for (Vrh vrh : vrhovi) {
				Double oldValue = vrh.getValue();
				Double sum = izracunajSumuSusjeda(graf.nadjiSusjedeZaVrh(vrh), graf);
				Double newValue = (1 - D) + D * sum;
				vrh.setValue(newValue);
				
				if (i == 0) {
					//zabrani konvergenciju u prvoj iteraciji
					continue;
				}
				
				Double errorRate = Math.abs(newValue - oldValue);
				if (errorRate < THRESHOLD) {
					//konvergencija postignuta, prekini računanje
					log.debug("KONVERGENCIJA POSTIGNUTA u iteraciji " + i + " za riječ " + vrh);
					return graf;
				}
			}
		}
		return graf;
	}
	
	private static Double izracunajSumuSusjeda(HashSet<Vrh> susjedi, Graf graf) {
		Double sum = 0D;
		for (Vrh susjed : susjedi) {
			sum += (1D / graf.nadjiSusjedeZaVrh(susjed).size()) * susjed.getValue();
		}
		return sum;
	}
	
	private static List<Vrh> evaluirajRjesenje(Graf graf, int brojKljucnihRijeci) {
		ArrayList<Vrh> vrhovi = new ArrayList<Vrh>(graf.getVrhovi());
		Collections.sort(vrhovi, (o1, o2) -> o2.getValue().compareTo(o1.getValue()));
		ArrayList<Vrh> kandidati = new ArrayList<>();
		for (int i = 0; i < Math.min(vrhovi.size(), brojKljucnihRijeci); i++) {
			kandidati.add(vrhovi.get(i));
		}
		return kandidati;
	}
	
	public static List<KljucnaRijec> spojiSusjedneKljucneRijeci(List<Vrh> vrhoviKandidati, int brojKljucnihRijeci) {
		//sortiraj ih po poziciji u tekstu - ako su neki susjedni, spoji u jednu kljucnu rijec
		List<Token> svePojaveSvihKljucnihRijeci = new ArrayList<Token>();
		vrhoviKandidati.stream().forEach(vrh -> svePojaveSvihKljucnihRijeci.addAll(vrh.getSvePojaveVrha()));
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
			Double vrijednost = vrhoviKandidati.get(vrhoviKandidati.indexOf(new Vrh(trenutniToken))).getValue();
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
					vrijednost += vrhoviKandidati.get(vrhoviKandidati.indexOf(new Vrh(iduciToken))).getValue();
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
	
	public static List<Vrh> konstruirajGrafIPrimijeniAlgoritamMultipleWindowSize(List<Token> tokeni, int brojKljucnihRijeci, int minN, int maxN) {
		HashMap<Vrh, Double> finalneKljucneRijeci = new HashMap<>();
		for (int i = minN; i <= maxN; i++) {
			List<Vrh> vrhoviKandidati = GrafUtils.konstruirajGrafIPrimijeniAlgoritam(tokeni, i, brojKljucnihRijeci * 2);
			for (Vrh vrh : vrhoviKandidati) {
				Double value = vrh.getValue();
				
				if (finalneKljucneRijeci.containsKey(vrh)) {
					finalneKljucneRijeci.put(vrh, finalneKljucneRijeci.get(vrh) + value);
				}
				else {
					finalneKljucneRijeci.put(vrh, value);
				}
			}
			
		}
		
		finalneKljucneRijeci.forEach((vrh, value) -> vrh.setValue(value));
		return new ArrayList<>(finalneKljucneRijeci.keySet());
	}
}


