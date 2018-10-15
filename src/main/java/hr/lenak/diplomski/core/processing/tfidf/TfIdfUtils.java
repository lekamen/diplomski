package hr.lenak.diplomski.core.processing.tfidf;

import static hr.lenak.diplomski.core.model.enums.KategorijaTokenaEnum.ADJECTIVE;
import static hr.lenak.diplomski.core.model.enums.KategorijaTokenaEnum.NOUN;

import java.util.List;

import hr.lenak.diplomski.core.model.Token;

public class TfIdfUtils {
	public static Dokument konstruirajDokument(List<Token> tokeni) {
		Dokument dokument = new Dokument();
		for (int i = 0; i < tokeni.size(); i++) {
			Token t = tokeni.get(i);
			//prodji sintakticki filter
			if (!passesSyntacticFilter(t)) {
				continue;
			}
//			//zbog specifičnosti zakona izbacuju se neke riječi iz dokumenta
//			if (isInForbiddenWords(t)) {
//				continue;
//			}
			Rijec rijec = new Rijec(t);
			dokument.dodajRijec(rijec);
		}
		
		return dokument;
	}
	
	private static boolean passesSyntacticFilter(Token t) {
		//dopuštamo imenice koje nisu vlastite i pridjeve
		return (t.getKategorija() == NOUN && !t.getTag().startsWith("Np"))  || t.getKategorija() == ADJECTIVE;
	}
}
