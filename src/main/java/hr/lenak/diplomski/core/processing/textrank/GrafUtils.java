package hr.lenak.diplomski.core.processing.textrank;

import static hr.lenak.diplomski.core.model.enums.KategorijaTokenaEnum.ADJECTIVE;
import static hr.lenak.diplomski.core.model.enums.KategorijaTokenaEnum.NOUN;
import static hr.lenak.diplomski.core.model.enums.KategorijaTokenaEnum.PUNCTUATION;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hr.lenak.diplomski.core.model.Token;

public class GrafUtils {
	
	private Logger log = LoggerFactory.getLogger(this.getClass());
	private static final int N = 3;
	private static final int T = 20;
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
	
	public void makniNepovezaneVrhove(Graf graf) {
		HashMap<Vrh, Boolean> visited = new HashMap<>();
		HashSet<Vrh> vrhovi = graf.getVrhovi();
		for (Vrh vrh : vrhovi) {
			visited.put(vrh, false);
		}
		dfs(vrhovi.iterator().next(), visited, graf); //FIXME
		List<Vrh> nepovezani = nadjiNepovezaneVrhove(graf, visited);
		for (Vrh v : nepovezani) {
			log.debug("nepovezani vrhovi " + v);
		}
		makniNepovezaneVrhove(graf, nepovezani);
	}
	
	private void dfs(Vrh vrh, HashMap<Vrh, Boolean> visited, Graf graf){
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
	
	private void makniNepovezaneVrhove(Graf graf, List<Vrh> nepovezani) {
		for (Vrh v : nepovezani) {
			graf.ukloniVrhIBridove(v);
		}
	}
	 
	private List<Vrh> nadjiNepovezaneVrhove(Graf graf, HashMap<Vrh, Boolean> visited){
		List<Vrh> nepovezaniVrhovi = new ArrayList<>();
		for(Vrh v : graf.getVrhovi()) {
			if (visited.get(v) == false) {
				nepovezaniVrhovi.add(v);
			}
		}
		return nepovezaniVrhovi;
	}
	
	public Graf konstruirajGraf(List<Token> tokeni) {
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
			boolean isDodan = graf.dodajVrh(vrh);
			if (!isDodan) {
				//zbog brida treba popraviti referencu na vrh koji je već unutra
				vrh = graf.nadjiVrh(vrh);
			}
			napraviBridoveZaVrh(i, vrh, tokeni, graf);
		}
		
		return graf;
	}
	
	private void napraviBridoveZaVrh(int pozicija, Vrh trenutni, List<Token> tokeni, Graf graf) {
		for (int i = 1; i <= N; i++) {
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
	
	private boolean tokenEndsSentence(Token t) {
		return t.getKategorija() == PUNCTUATION && (t.getLemma().equals(".") || t.getLemma().equals("?") || t.getLemma().equals("!"));
	}
	
	private boolean passesSyntacticFilter(Token t) {
		//dopuštamo imenice koje nisu vlastite i pridjeve
		return (t.getKategorija() == NOUN && !t.getTag().startsWith("Np"))  || t.getKategorija() == ADJECTIVE;
	}
	
	private boolean isInForbiddenWords(Token t) {
		return forbiddenWords.contains(t.getLemma());
	}
	
	public Graf primijeniAlgoritamNaGraf(Graf graf) {
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
					log.debug("value vrh " + vrh.getValue() + " error rate " + errorRate + " suma susjeda " + sum);
					HashSet<Vrh> susjedi = graf.nadjiSusjedeZaVrh(vrh);
					for(Vrh v : susjedi) {
						log.debug("susjed: " + v.getToken().getLemma() + " vrijednost " + v.getValue() + " size " + graf.nadjiSusjedeZaVrh(v).size());
					}
					return graf;
				}
			}
		}
		return graf;
	}
	
	private Double izracunajSumuSusjeda(HashSet<Vrh> susjedi, Graf graf) {
		Double sum = 0D;
		for (Vrh susjed : susjedi) {
			//sum += (1D / graf.getUkupnaSumaBridovaIzVrha(susjed)) * susjed.getValue();
			sum += (1D / graf.nadjiSusjedeZaVrh(susjed).size()) * susjed.getValue();
		}
		return sum;
	}
	
	public List<Vrh> evaluirajRjesenje(Graf graf) {
		ArrayList<Vrh> vrhovi = new ArrayList<Vrh>(graf.getVrhovi());
		Collections.sort(vrhovi, new Comparator<Vrh>() {
			@Override
			public int compare(Vrh o1, Vrh o2) {
				return o1.getValue().compareTo(o2.getValue());
			}
		});
		ArrayList<Vrh> kandidati = new ArrayList<>();
		for (int i = vrhovi.size() - 1; i >= Math.max(0, vrhovi.size() - T); i--) {
			kandidati.add(vrhovi.get(i));
		}
		return kandidati;
	}
	
	public List<String> spojiSusjedneKljucneRijeci(List<Vrh> vrhoviKandidati) {
		//sortiraj ih po poziciji u tekstu - ako su neki susjedni, spoji u jednu kljucnu rijec
		Collections.sort(vrhoviKandidati, new Comparator<Vrh>() {
			@Override
			public int compare(Vrh o1, Vrh o2) {
				return o1.getToken().getPosition().compareTo(o2.getToken().getPosition());
			}
		});
		
		int i = 0;
		int n = vrhoviKandidati.size();
		List<String> kljucneRijeci = new ArrayList<>();
		while (i < n) {
			int position = vrhoviKandidati.get(i).getToken().getPosition();
			String kljucnaRijec = vrhoviKandidati.get(i).getToken().getLemma();
			
			if (i < n -1) {
				int next = vrhoviKandidati.get(i + 1).getToken().getPosition();
				while (next == position + 1) {
					kljucnaRijec += " " + vrhoviKandidati.get(i + 1).getToken().getLemma();
					position = next;
					i++;
					if (i >= n - 1) {
						break;
					}
					next = vrhoviKandidati.get(i + 1).getToken().getPosition();
				}
			}
			
			kljucneRijeci.add(kljucnaRijec);
			i++;
		}
		return kljucneRijeci;
	}
}


