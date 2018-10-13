package hr.lenak.diplomski.core.processing;

import static hr.lenak.diplomski.core.model.enums.KategorijaTokenaEnum.ADJECTIVE;
import static hr.lenak.diplomski.core.model.enums.KategorijaTokenaEnum.NOUN;

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
	private static final int N = 3;
	
	static {
		lista = Repositories.tekstZakonaRepository.findAll();
	}
	
	public NadjiKljucneRijeci() {
		
	}
	
	public void nadji() {
		for (int i = 0; i < /*lista.size()*/1; i++) {
			String kljucneRijeci = kljucneRijeciZaTekst(Repositories.tokenRepository.findByTekstZakona(lista.get(i)));
		}
	}
	
	private String kljucneRijeciZaTekst(List<Token> tokeni) {
		Graf graf = konstruirajGraf(tokeni);
		log.debug(graf.toString());
		System.out.println(graf.toString());
		
		return null;
	}
	
	private Graf konstruirajGraf(List<Token> tokeni) {
		Graf graf = new Graf();
		for (int i = 0; i < tokeni.size(); i++) {
			Token t = tokeni.get(i);
			//prodji sintakticki filter
			if (!passesSyntacticFilter(t)) {
				continue;
			}
			//ubaci token u graf
			Vrh vrh = new Vrh(t);
			log.debug("konstruirajGraf, dodavanje vrha: " + graf.dodajVrh(vrh));
			napraviBridoveZaVrh(i, t, tokeni, graf);
		}
		
		return graf;
	}
	
	private void napraviBridoveZaVrh(int pozicija, Token trenutni, List<Token> tokeni, Graf graf) {
		for (int i = 1; i <= N; i++) {
			int pos = pozicija - i;
			if (pos < 0) {
				break;
			}
			
			Token susjed = tokeni.get(pos);
			if (!passesSyntacticFilter(susjed)) {
				continue;
			}
			//susjed je u grafu, napravi brid
			log.debug("napraviBridoveZaVrh: " + graf.dodajBrid(new Brid(new Vrh(trenutni), new Vrh(susjed))));
		}
	}
	
	private boolean passesSyntacticFilter(Token t) {
		return t.getKategorija() == NOUN || t.getKategorija() == ADJECTIVE;
	}
}
