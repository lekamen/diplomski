package hr.lenak.diplomski.core.processing;

import java.util.List;

import hr.lenak.diplomski.core.model.TekstZakona;
import hr.lenak.diplomski.core.processing.tfidf.Korpus;

public class SpremiKorpusUBazu {

	private static Korpus korpus;

	public static void spremi(List<TekstZakona> lista) {
		korpus = new Korpus(lista);
		korpus.calculateIdfsZaSveRijeci(lista);
	}
}
