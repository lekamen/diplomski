package hr.lenak.diplomski.core.processing;

import static hr.lenak.diplomski.core.model.enums.KategorijaTokenaEnum.ADJECTIVE;
import static hr.lenak.diplomski.core.model.enums.KategorijaTokenaEnum.NOUN;
import static hr.lenak.diplomski.web.util.HelperMethods.formatDate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hr.lenak.diplomski.core.model.TekstZakona;
import hr.lenak.diplomski.core.model.Token;
import hr.lenak.diplomski.web.util.Repositories;

public class NadjiKljucneRijeci {

	private Logger log = LoggerFactory.getLogger(this.getClass());
	private static List<TekstZakona> lista;
	private TekstZakona jedan;
	private static final int N = 2;
	private static final int ITERATIONS = 1;
	private static final double THRESHOLD = 0.0001;
	private static final double D = 0.85;
	private static final HashSet<String> forbiddenWords = new HashSet<>(Arrays.asList(
			"urbroj", /* pojavljuje se u svakom zakonu, nebitna za sadržaj */
			"klasa" /* pojavljuje se u svakom zakonu, nebitna za sadržaj */ //TODO: možda članak izbacit?
	));
	
	static {
		//lista = Repositories.tekstZakonaRepository.findAll();
	}
	
	public NadjiKljucneRijeci() {
		jedan = Repositories.tekstZakonaRepository.findByBrojFilea(1);
	}
	
	public void nadji() {
		for (int i = 0; i < /*lista.size()*/1; i++) {
			String kljucneRijeci = kljucneRijeciZaTekst(Repositories.tokenRepository.findByTekstZakona(/*lista.get(i)*/jedan));
		}
	}
	
	private String kljucneRijeciZaTekst(List<Token> tokeni) {
		Graf graf = konstruirajGraf(tokeni);
		log.debug(graf.toString());
		
		log.debug("uklanjanje nepovezanih");
		GrafUtils gu = new GrafUtils();
		gu.makniNepovezaneVrhove(graf);
		//log.debug(graf.toString());
		
		primijeniAlgoritamNaGraf(graf);
		log.debug(graf.toString());
		
		List<Vrh> vrhoviKandidati = evaluirajRjesenje(graf);
		for (Vrh v : vrhoviKandidati) {
			log.debug("kandidat: " + v);
		}
		
		List<String> kljucneRijeci = spojiSusjedneKljucneRijeci(vrhoviKandidati);
		for (String s : kljucneRijeci) {
			log.debug("kljucna rijec: " + s);
		}
		log.debug("broj vrhova " + graf.getVrhovi().size());
		log.debug("broj bridova " + graf.getBridovi().size());
		
		return null;
	}
	
	private Graf konstruirajGraf(List<Token> tokeni) {
		Graf graf = new Graf();
		boolean pass = false;
		for (int i = 0; i < tokeni.size(); i++) {
			Token t = tokeni.get(i);
			
			//TODO makni ovo
			//if (t.getValue().equals("donijela")) pass = true;
			//if (!pass) continue;
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
			log.debug("konstruirajGraf, dodavanje vrha: " + vrh + " " + String.valueOf(isDodan));
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
			if (!passesSyntacticFilter(susjed)) {
				continue;
			}
			//ne dopuštamo da je čvor povezan sam sa sobom u grafu
			if (trenutni.getToken().equals(susjed)) {
				continue;
			}
			//susjed je u grafu, napravi brid
			//TODO makni ovo
			if (graf.nadjiVrh(new Vrh(susjed)) == null) continue;
			log.debug("napraviBridoveZaVrh: " + graf.dodajBrid(new Brid(trenutni, graf.nadjiVrh(new Vrh(susjed)))));
		}
	}
	
	private boolean passesSyntacticFilter(Token t) {
		return (t.getKategorija() == NOUN && !t.getTag().startsWith("Np"))  || t.getKategorija() == ADJECTIVE;
	}
	
	private boolean isInForbiddenWords(Token t) {
		return forbiddenWords.contains(t.getLemma());
	}
	
	private Graf primijeniAlgoritamNaGraf(Graf graf) {
		HashSet<Vrh> vrhovi = graf.getVrhovi();
		for (int i = 0; i < 30/*ITERATIONS*/; i++) {
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
			sum += (1D / graf.nadjiSusjedeZaVrh(susjed).size()) * susjed.getValue(); 
		}
		return sum;
	}
	
	private List<Vrh> evaluirajRjesenje(Graf graf) {
		ArrayList<Vrh> vrhovi = new ArrayList<Vrh>(graf.getVrhovi());
		final int T = 20; //TODO
		Collections.sort(vrhovi, new Comparator<Vrh>() {
			@Override
			public int compare(Vrh o1, Vrh o2) {
				return o1.getValue().compareTo(o2.getValue());
			}
		});
		ArrayList<Vrh> kandidati = new ArrayList<>();
		for (int i = vrhovi.size() - 1; i >= vrhovi.size() - T; i--) {
			kandidati.add(vrhovi.get(i));
		}
		return kandidati;
	}
	
	private List<String> spojiSusjedneKljucneRijeci(List<Vrh> vrhoviKandidati) {
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
