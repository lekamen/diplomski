package hr.lenak.diplomski.core.processing;

import java.util.List;

import hr.lenak.diplomski.core.model.TekstZakona;
import hr.lenak.diplomski.core.processing.tfidf.Korpus;

public class SpremiKorpusUBazu {

	public static void spremi(List<TekstZakona> lista) {
		Korpus.inicijalizirajDokumente(lista);
		Korpus.calculateIdfsZaSveRijeci(lista);
	}
}
