package hr.lenak.diplomski.core.processing;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.transaction.annotation.Transactional;

import hr.lenak.diplomski.web.util.Repositories;

@Transactional
public class PostProcesiranje {

	public static void spremiKljucneRijeciUBazu(VrstaAlgoritmaEnum vrstaAlgoritma, Long tsiId, Integer brojFilea, Long tekstZakonaId, List<KljucnaRijec> kljucneRijeci) {
		String keywords = String.join(", ", kljucneRijeci.stream().map(kw -> kw.getKljucnaRijec()).collect(Collectors.toList()));
		Repositories.kljucneRijeciRepository.updateKljucneRijeci(vrstaAlgoritma, tsiId, brojFilea, tekstZakonaId, keywords);
	}
}
