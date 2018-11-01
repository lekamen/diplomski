package hr.lenak.diplomski.core.processing;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hr.lenak.diplomski.core.model.Rijec;
import hr.lenak.diplomski.core.model.TekstZakona;
import hr.lenak.diplomski.core.model.Token;
import hr.lenak.diplomski.core.processing.textrank.GrafUtils;
import hr.lenak.diplomski.core.processing.textrank.Vrh;
import hr.lenak.diplomski.core.processing.tfidf.Korpus;
import hr.lenak.diplomski.core.processing.tfidf.TfIdfUtils;
import hr.lenak.diplomski.web.util.Repositories;

public class NadjiKljucneRijeci {

	private static Logger log = LoggerFactory.getLogger(NadjiKljucneRijeci.class);
	private static List<TekstZakona> lista;
	private static final int DEFAULT_TEXTRANK_WINDOW_SIZE = 2;
	private static final int DEFAULT_TEXTRANK_MAX_WINDOW_SIZE = 5;
	private static final int DEFAULT_TEXTRANK_BROJ_KLJUCNIH_RIJECI = 8;
	private static final int DEFAULT_TFIDF_BROJ_KLJUCNIH_RIJECI = 8;
	
	public static void setLista(List<TekstZakona> lista) {
		NadjiKljucneRijeci.lista = lista;
	}

	/**
	 * Metoda s defaultnim parametrima
	 */
	public static void nadjiTextRank() {
		nadjiTextRank(DEFAULT_TEXTRANK_WINDOW_SIZE, DEFAULT_TEXTRANK_BROJ_KLJUCNIH_RIJECI);
	}
	
	/**
	 * Metoda koja vrti algoritam na više različitih vrijednosti N s varijabilnim parametrima
	 */
	public static void nadjiTextRank(int windowSize, int brojKljucnihRijeci) {
		for (int i = 0; i < lista.size(); i++) {
			TekstZakona tekstZakona = lista.get(i);
			
			List<Token> tokeni = Repositories.tokenRepository.findByTekstZakona(tekstZakona);
			List<Vrh> vrhoviKandidati = GrafUtils.konstruirajGrafIPrimijeniAlgoritam(tokeni, windowSize, brojKljucnihRijeci * 2);
			List<KljucnaRijec> kljucneRijeci = GrafUtils.spojiSusjedneKljucneRijeci(vrhoviKandidati, brojKljucnihRijeci);
			PostProcesiranje.spremiKljucneRijeciUBazu(VrstaAlgoritmaEnum.TEXTRANK, 
				tekstZakona.getTsiId(), tekstZakona.getBrojFilea(), tekstZakona.getTekstZakonaId(), kljucneRijeci);
			log.debug("gotov textrank, za " + tekstZakona.getBrojFilea());
		}
	}
	
	/**
	 * Metoda koja vrti algoritam na više različitih vrijednosti N s defaultnim parametrima
	 */
	public static void nadjiTextRankMultipleWindowSize(int minN, int maxN) {
		nadjiTextRankMultipleWindowSize(DEFAULT_TEXTRANK_BROJ_KLJUCNIH_RIJECI, minN, maxN);
	}
	
	/**
	 * Metoda koja vrti algoritam na više različitih vrijednosti N s varijabilnim parametrima
	 */
	public static void nadjiTextRankMultipleWindowSize(int brojKljucnihRijeci, int minN, int maxN) {
		for (int i = 0; i < lista.size(); i++) {
			TekstZakona tekstZakona = lista.get(i);
			
			List<Token> tokeni = Repositories.tokenRepository.findByTekstZakona(tekstZakona);
			List<Vrh> vrhovi = GrafUtils.konstruirajGrafIPrimijeniAlgoritamMultipleWindowSize(tokeni, brojKljucnihRijeci, minN, maxN);
			List<KljucnaRijec> kljucneRijeci = GrafUtils.spojiSusjedneKljucneRijeci(vrhovi, brojKljucnihRijeci);
			
			PostProcesiranje.spremiKljucneRijeciUBazu(VrstaAlgoritmaEnum.TEXTRANKMULWIN, 
					tekstZakona.getTsiId(), tekstZakona.getBrojFilea(), tekstZakona.getTekstZakonaId(), kljucneRijeci);
			
			log.debug("gotov textrank mul win size, za " + tekstZakona.getBrojFilea());
		}
	}

	/**
	 * Poziv tf-idf algoritma s varijabilnim parametrom
	 */
	public static void nadjiTfIdf(int brojKljucnihRijeci) {	
		for(TekstZakona tekstZakona : lista) {
			List<Rijec> rijeci = Repositories.rijecRepository.findAllRijeciForTekstZakona(tekstZakona.getTekstZakonaId());
			List<KljucnaRijec> kljucneRijeci = TfIdfUtils.findKeywords(rijeci, brojKljucnihRijeci);
			
			PostProcesiranje.spremiKljucneRijeciUBazu(VrstaAlgoritmaEnum.TFIDF, 
				tekstZakona.getTsiId(), tekstZakona.getBrojFilea(), tekstZakona.getTekstZakonaId(), kljucneRijeci);
			
			log.debug("gotov tfidf, za " + tekstZakona.getBrojFilea());
		}
	}
	
	/**
	 * Poziv tf-idf algoritma s defaultnim parametrom
	 */
	public static void nadjiTfIdf() {
		nadjiTfIdf(DEFAULT_TFIDF_BROJ_KLJUCNIH_RIJECI);
	}

	/**
	 * Poziv textrank-idf algoritma s varijabilnim parametrima
	 */
	public static void nadjiTextrankIdf(int windowSize, int brojKljucnihRijeci) {
		for (int i = 0; i < lista.size(); i++) {
			TekstZakona tekstZakona = lista.get(i);
	
			List<Token> tokeni = Repositories.tokenRepository.findByTekstZakona(tekstZakona);
			List<Vrh> vrhoviKandidati = GrafUtils.konstruirajGrafIPrimijeniAlgoritam(tokeni, windowSize, brojKljucnihRijeci * 4);
			Korpus.calculateValueForListuVrhova(vrhoviKandidati);

			Collections.sort(vrhoviKandidati, (o1, o2) -> o2.getValue().compareTo(o1.getValue())); 
			int size = brojKljucnihRijeci * 2 > vrhoviKandidati.size() ? vrhoviKandidati.size() : brojKljucnihRijeci * 2;
			List<KljucnaRijec> kljucneRijeci = GrafUtils.spojiSusjedneKljucneRijeci(vrhoviKandidati.subList(0, size), brojKljucnihRijeci);
			
			PostProcesiranje.spremiKljucneRijeciUBazu(VrstaAlgoritmaEnum.TEXTRANKIDF, 
					tekstZakona.getTsiId(), tekstZakona.getBrojFilea(), tekstZakona.getTekstZakonaId(), kljucneRijeci);
			
			log.debug("gotov textrankidf, za " + tekstZakona.getBrojFilea());
		}
	}
	
	/**
	 * Poziv textrank-idf algoritma s defaultnim parametrima
	 */
	public static void nadjiTextrankIdf() {
		nadjiTextrankIdf(DEFAULT_TEXTRANK_WINDOW_SIZE, DEFAULT_TEXTRANK_BROJ_KLJUCNIH_RIJECI);
	}
	
	/**
	 * Poziv textrank mul win - idf s varijabilnim parametrima
	 */
	public static void nadjiTextrankMulWinIdf(int minWindowSize, int maxWindowSize, int brojKljucnihRijeci) {
		for (int i = 0; i < lista.size(); i++) {
			TekstZakona tekstZakona = lista.get(i);
			
			List<Token> tokeni = Repositories.tokenRepository.findByTekstZakona(tekstZakona);
			List<Vrh> vrhoviKandidati = GrafUtils.konstruirajGrafIPrimijeniAlgoritamMultipleWindowSize(tokeni, brojKljucnihRijeci, minWindowSize, maxWindowSize);
			Korpus.calculateValueForListuVrhova(vrhoviKandidati);

			Collections.sort(vrhoviKandidati, (o1, o2) -> o2.getValue().compareTo(o1.getValue())); 
			int size = brojKljucnihRijeci * 2 > vrhoviKandidati.size() ? vrhoviKandidati.size() : brojKljucnihRijeci * 2;
			List<KljucnaRijec> kljucneRijeci = GrafUtils.spojiSusjedneKljucneRijeci(vrhoviKandidati.subList(0, size), brojKljucnihRijeci);

			PostProcesiranje.spremiKljucneRijeciUBazu(VrstaAlgoritmaEnum.TEXTRANKMULWINIDF, 
					tekstZakona.getTsiId(), tekstZakona.getBrojFilea(), tekstZakona.getTekstZakonaId(), kljucneRijeci);
			
			log.debug("gotov textrank mul win idf, za " + tekstZakona.getBrojFilea());
		}
	}
	
	/**
	 * Poziv textrank mul win - idf s defaultnim parametrima
	 */
	public static void nadjiTextrankMulWinIdf() {
		nadjiTextrankMulWinIdf(DEFAULT_TEXTRANK_WINDOW_SIZE, DEFAULT_TEXTRANK_MAX_WINDOW_SIZE, DEFAULT_TEXTRANK_BROJ_KLJUCNIH_RIJECI);
	}
}
