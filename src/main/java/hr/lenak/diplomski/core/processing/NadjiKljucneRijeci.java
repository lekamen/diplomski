package hr.lenak.diplomski.core.processing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hr.lenak.diplomski.core.model.TekstZakona;
import hr.lenak.diplomski.core.model.Token;
import hr.lenak.diplomski.core.processing.textrank.GrafUtils;
import hr.lenak.diplomski.core.processing.textrank.Vrh;
import hr.lenak.diplomski.core.processing.tfidf.Dokument;
import hr.lenak.diplomski.core.processing.tfidf.Korpus;
import hr.lenak.diplomski.web.util.Repositories;

public class NadjiKljucneRijeci {

	private static Logger log = LoggerFactory.getLogger(NadjiKljucneRijeci.class);
	private static List<TekstZakona> lista;
	//private static TekstZakona jedan;
	private static Korpus korpus;
	private static final int DEFAULT_TEXTRANK_WINDOW_SIZE = 2;
	private static final int DEFAULT_TEXTRANK_BROJ_KLJUCNIH_RIJECI = 8;
	private static final int DEFAULT_TFIDF_BROJ_KLJUCNIH_RIJECI = 8;
	
	static {
		lista = Repositories.tekstZakonaRepository.findAll().subList(0, 100); //TODO
		//jedan = Repositories.tekstZakonaRepository.findByBrojFilea(1);
		korpus = new Korpus(lista);
	}

	/**
	 * Metoda s defaultnim parametrima
	 */
	public static void nadjiTextRank() {
		for (int i = 0; i < lista.size(); i++) {
			TekstZakona tekstZakona = lista.get(i);
			List<Token> tokeni = Repositories.tokenRepository.findByTekstZakona(tekstZakona);
			List<KljucnaRijec> kljucneRijeci = kljucneRijeciTextRank(tokeni, DEFAULT_TEXTRANK_WINDOW_SIZE, DEFAULT_TEXTRANK_BROJ_KLJUCNIH_RIJECI);
			PostProcesiranje.spremiKljucneRijeciUBazu(VrstaAlgoritmaEnum.TEXTRANK, 
				tekstZakona.getTsiId(), tekstZakona.getBrojFilea(), tekstZakona.getTekstZakonaId(), kljucneRijeci);
		}
	}
	
	/**
	 * Metoda koja vrti algoritam na više različitih vrijednosti N s varijabilnim parametrima
	 */
	public static void nadjiTextRank(int windowSize, int brojKljucnihRijeci) {
		for (int i = 0; i < lista.size(); i++) {
			TekstZakona tekstZakona = lista.get(i);
			List<Token> tokeni = Repositories.tokenRepository.findByTekstZakona(tekstZakona);
			List<KljucnaRijec> kljucneRijeci = kljucneRijeciTextRank(tokeni, windowSize, brojKljucnihRijeci);
			PostProcesiranje.spremiKljucneRijeciUBazu(VrstaAlgoritmaEnum.TEXTRANK, 
					tekstZakona.getTsiId(), tekstZakona.getBrojFilea(), tekstZakona.getTekstZakonaId(), kljucneRijeci);
		}
	}
	
	/**
	 * Metoda koja vrti algoritam na više različitih vrijednosti N s defaultnim parametrima
	 */
	public static void nadjiTextRankMultipleWindowSize(int minN, int maxN) {
		for (int i = 0; i < lista.size(); i++) {
			TekstZakona tekstZakona = lista.get(i);
			List<Token> tokeni = Repositories.tokenRepository.findByTekstZakona(tekstZakona);
			List<KljucnaRijec> kljucneRijeci = kljucneRijeciTextRankMultipleWindowSize(tokeni, DEFAULT_TEXTRANK_BROJ_KLJUCNIH_RIJECI, minN, maxN);
			PostProcesiranje.spremiKljucneRijeciUBazu(VrstaAlgoritmaEnum.TEXTRANKMULWIN, 
					tekstZakona.getTsiId(), tekstZakona.getBrojFilea(), tekstZakona.getTekstZakonaId(), kljucneRijeci);
		}
	}
	
	/**
	 * Metoda koja vrti algoritam na više različitih vrijednosti N s varijabilnim parametrima
	 */
	public static void nadjiTextRankMultipleWindowSize(int brojKljucnihRijeci, int minN, int maxN) {
		for (int i = 0; i < lista.size(); i++) {
			TekstZakona tekstZakona = lista.get(i);
			List<Token> tokeni = Repositories.tokenRepository.findByTekstZakona(tekstZakona);
			List<KljucnaRijec> kljucneRijeci = kljucneRijeciTextRankMultipleWindowSize(tokeni, brojKljucnihRijeci, minN, maxN);
			PostProcesiranje.spremiKljucneRijeciUBazu(VrstaAlgoritmaEnum.TEXTRANKMULWIN, 
					tekstZakona.getTsiId(), tekstZakona.getBrojFilea(), tekstZakona.getTekstZakonaId(), kljucneRijeci);
		}
	}

	private static List<KljucnaRijec> kljucneRijeciTextRank(List<Token> tokeni, int windowSize, int brojKljucnihRijeci) {

		List<Vrh> vrhoviKandidati = GrafUtils.konstruirajGrafIPrimijeniAlgoritam(tokeni, windowSize, brojKljucnihRijeci * 2);
		List<KljucnaRijec> kljucneRijeci = GrafUtils.spojiSusjedneKljucneRijeci(vrhoviKandidati, brojKljucnihRijeci);
		return kljucneRijeci;
	}
	
	private static List<KljucnaRijec> kljucneRijeciTextRankMultipleWindowSize(List<Token> tokeni, int brojKljucnihRijeci, int minN, int maxN) {
		HashMap<KljucnaRijec, Double> finalneKljucneRijeci = new HashMap<>();
		for (int i = minN; i <= maxN; i++) {
			List<KljucnaRijec> kljucneRijeci = kljucneRijeciTextRank(tokeni, i, brojKljucnihRijeci);
			for(KljucnaRijec rijec : kljucneRijeci) {
				Double value = rijec.getVrijednost();

				if (finalneKljucneRijeci.containsKey(rijec)) {
					finalneKljucneRijeci.put(rijec, finalneKljucneRijeci.get(rijec) + value);
				}
				else {
					finalneKljucneRijeci.put(rijec,  value);
				}
			}
		}
		
		List<Map.Entry<KljucnaRijec, Double>> finals = new ArrayList<>(finalneKljucneRijeci.entrySet());
		Collections.sort(finals, (o1, o2) -> o2.getValue().compareTo(o1.getValue()));
		return finals.stream().map(e -> e.getKey()).collect(Collectors.toList()).subList(0, brojKljucnihRijeci);
	}
	
	/**
	 * Poziv tf-idf algoritma s varijabilnim parametrom
	 */
	public static void nadjiTfIdf(int brojKljucnihRijeci) {	
		List<Dokument> dokumenti = korpus.getDokumenti();
		for (int i = 0; i < dokumenti.size(); i++) {
			Dokument doc = dokumenti.get(i);
			doc.calculateTfsZaRijeci();
			korpus.calculateIdfsZaDokument(doc);
			//log.debug(doc.toString());
			List<KljucnaRijec> kljucneRijeci = doc.returnKeywords(brojKljucnihRijeci);
//			for (KljucnaRijec s : kljucneRijeci) {
//				log.debug("kljucne rijeci: " + s);
//			}
			
			TekstZakona tekstZakona = doc.getTekstZakona();
			
			PostProcesiranje.spremiKljucneRijeciUBazu(VrstaAlgoritmaEnum.TFIDF, 
				tekstZakona.getTsiId(), tekstZakona.getBrojFilea(), tekstZakona.getTekstZakonaId(), kljucneRijeci);
		}
	}
	
	/**
	 * Poziv tf-idf algoritma s defaultnim parametrom
	 */
	public static void nadjiTfIdf() {
		nadjiTfIdf(DEFAULT_TFIDF_BROJ_KLJUCNIH_RIJECI);
	}
	
	
	/**
	 * Poziv textrank-idf algoritma s defaultnim parametrima
	 */
	public static void nadjiTextrankIdf() {
		nadjiTextrankIdf(DEFAULT_TEXTRANK_WINDOW_SIZE, DEFAULT_TEXTRANK_BROJ_KLJUCNIH_RIJECI);
	}
	/**
	 * Poziv textrank-idf algoritma s varijabilnim parametrima
	 */
	public static void nadjiTextrankIdf(int windowSize, int brojKljucnihRijeci) {
		for (int i = 0; i < lista.size(); i++) {
			TekstZakona tekstZakona = lista.get(i);
			List<Token> tokeni = Repositories.tokenRepository.findByTekstZakona(tekstZakona);
			
			List<Vrh> vrhoviKandidati = GrafUtils.konstruirajGrafIPrimijeniAlgoritam(tokeni, windowSize, brojKljucnihRijeci * 4);

			korpus.calculateValueForListuVrhova(vrhoviKandidati);

			Collections.sort(vrhoviKandidati, (o1, o2) -> o2.getValue().compareTo(o1.getValue())); 
			int size = brojKljucnihRijeci * 2 > vrhoviKandidati.size() ? vrhoviKandidati.size() : brojKljucnihRijeci * 2;
			List<KljucnaRijec> kljucneRijeci = GrafUtils.spojiSusjedneKljucneRijeci(vrhoviKandidati.subList(0, size), brojKljucnihRijeci);
			
			PostProcesiranje.spremiKljucneRijeciUBazu(VrstaAlgoritmaEnum.TEXTRANKIDF, 
					tekstZakona.getTsiId(), tekstZakona.getBrojFilea(), tekstZakona.getTekstZakonaId(), kljucneRijeci);
		}
	}
}
