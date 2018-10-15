package hr.lenak.diplomski.core.processing;

import static hr.lenak.diplomski.core.model.enums.KategorijaTokenaEnum.ADJECTIVE;
import static hr.lenak.diplomski.core.model.enums.KategorijaTokenaEnum.NOUN;
import static hr.lenak.diplomski.core.model.enums.KategorijaTokenaEnum.PUNCTUATION;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hr.lenak.diplomski.core.model.TekstZakona;
import hr.lenak.diplomski.core.model.Token;
import hr.lenak.diplomski.core.processing.textrank.Graf;
import hr.lenak.diplomski.core.processing.textrank.GrafUtils;
import hr.lenak.diplomski.core.processing.textrank.Vrh;
import hr.lenak.diplomski.core.processing.tfidf.Dokument;
import hr.lenak.diplomski.core.processing.tfidf.Korpus;
import hr.lenak.diplomski.core.processing.tfidf.Rijec;
import hr.lenak.diplomski.web.util.Repositories;

public class NadjiKljucneRijeci {

	private Logger log = LoggerFactory.getLogger(this.getClass());
	private static List<TekstZakona> lista;
	private TekstZakona jedan;
	
	static {
		//lista = Repositories.tekstZakonaRepository.findAll();
	}
	
	public NadjiKljucneRijeci() {
		jedan = Repositories.tekstZakonaRepository.findByBrojFilea(0);
	}
	
	public void nadjiTextRank() {
		for (int i = 0; i < /*lista.size()*/1; i++) {
			List<Token> tokeni = Repositories.tokenRepository.findByTekstZakona(/*lista.get(i)*/jedan);
			String kljucneRijeci = kljucneRijeciTextRank(tokeni);
		}
	}
	
	public void nadjiTfIdf() {
		
		Korpus korpus = new Korpus();
		korpus.inicijalizirajDokumente();
		
		List<Dokument> dokumenti = korpus.getDokumenti();
		Dokument doc = dokumenti.get(1);
		doc.calculateTfsZaRijeci();
		korpus.calculateIdfsZaDokument(doc);
		log.debug(doc.toString());
		List<String> kljucneRijeci = doc.returnKeywords();
		for (String s : kljucneRijeci) {
			log.debug("kljucne rijeci: " + s);
		}
	}

	
	private String kljucneRijeciTextRank(List<Token> tokeni) {
		GrafUtils gu = new GrafUtils();
		Graf graf = gu.konstruirajGraf(tokeni);
		//log.debug(graf.toString());
		
		log.debug("uklanjanje nepovezanih");
		
		gu.makniNepovezaneVrhove(graf);
		log.debug(graf.toString());
		
		gu.primijeniAlgoritamNaGraf(graf);
		log.debug(graf.toString());
		
		List<Vrh> vrhoviKandidati = gu.evaluirajRjesenje(graf);
		for (Vrh v : vrhoviKandidati) {
			log.debug("kandidat: " + v);
		}
		
		List<String> kljucneRijeci = gu.spojiSusjedneKljucneRijeci(vrhoviKandidati);
		for (String s : kljucneRijeci) {
			log.debug("kljucna rijec: " + s);
		}
		log.debug("broj vrhova " + graf.getVrhovi().size());
		log.debug("broj bridova " + graf.getBridovi().size());
		
		return null;
	}
	

	

}
