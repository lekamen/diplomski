package hr.lenak.diplomski.core.repository;

import static hr.lenak.diplomski.core.model.generated.QKljucneRijeci.kljucneRijeci;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import hr.lenak.diplomski.core.model.KljucneRijeci;
import hr.lenak.diplomski.core.processing.VrstaAlgoritmaEnum;


@Repository
@Transactional
public class KljucneRijeciRepository extends QueryDslRepository<KljucneRijeci, Long> {

	public KljucneRijeci findKljucneRijeci(Long tsiId, Integer brojFilea, Long tekstZakonaId) {
		return select(kljucneRijeci)
			.from(kljucneRijeci)
			.where(
				kljucneRijeci.tsiId.eq(tsiId),
				kljucneRijeci.brojFilea.eq(brojFilea),
				kljucneRijeci.tekstZakonaId.eq(tekstZakonaId)
			)
			.fetchOne();
	}
	public void updateKljucneRijeci(VrstaAlgoritmaEnum vrstaAlgoritma, Long tsiId, Integer brojFilea, Long tekstZakonaId, String kljucneRijeci) {
		KljucneRijeci kr = findKljucneRijeci(tsiId, brojFilea, tekstZakonaId);
		switch (vrstaAlgoritma) {
		case TEXTRANK:
			kr.setKwTextrank(kljucneRijeci);
			break;
		case TFIDF:
			kr.setKwTfidf(kljucneRijeci);
			break;
		case TEXTRANKMULWIN:
			kr.setKwTextrankMulWinSize(kljucneRijeci);
			break;
		case TEXTRANKIDF:
			kr.setKwTextrankIdf(kljucneRijeci);
			break;
		case TEXTRANKMULWINIDF:
			kr.setKwTextrankMulWinIdf(kljucneRijeci);
			break;
		}
		update(kr);
	}
	
	@Cacheable(cacheNames = "kljucneRijeci")
	public List<KljucneRijeci> findAllKljucneRijeci() {
		return select(kljucneRijeci)
			.from(kljucneRijeci)
			.fetch();
	}

	public List<KljucneRijeci> findByKljucneRijeciAndVrstaAlgoritma(String kljucneRijeci, String[] keywords, VrstaAlgoritmaEnum vrstaAlgoritma) {
		List<KljucneRijeci> sveKljucneRijeci = findAllKljucneRijeci();
		
		List<KljucneRijeci> matches = new ArrayList<>();
		
		//najviše se vrednuju oni koji u potpunosti sadrže traženu frazu
		matches.addAll(sveKljucneRijeci.stream().filter(kr -> odrediDohvat(kr, vrstaAlgoritma).contains(kljucneRijeci)).collect(Collectors.toList()));
		
		//zatim samo sve riječi
		matches.addAll(sveKljucneRijeci.stream().filter(kr -> matchesAllFromArray(kr, keywords, vrstaAlgoritma)).collect(Collectors.toList()));
		
		//zatim neke riječi
		matches.addAll(sveKljucneRijeci.stream().filter(kr -> matchesAnyFromArray(kr, keywords, vrstaAlgoritma)).collect(Collectors.toList()));
		
		return new ArrayList<KljucneRijeci>(new LinkedHashSet<KljucneRijeci>(matches));
	}
	
	private boolean matchesAllFromArray(KljucneRijeci kr, String[] array, VrstaAlgoritmaEnum vrstaAlgoritma) {
		return Arrays.asList(array).stream().allMatch(s -> odrediDohvat(kr, vrstaAlgoritma).contains(s));
	}
	
	private boolean matchesAnyFromArray(KljucneRijeci kr, String[] array, VrstaAlgoritmaEnum vrstaAlgoritma) {
		return Arrays.asList(array).stream().anyMatch(s -> odrediDohvat(kr, vrstaAlgoritma).contains(s));
	}
	
	private String odrediDohvat(KljucneRijeci kr, VrstaAlgoritmaEnum vrstaAlgoritma) {
		switch (vrstaAlgoritma) {
		case TEXTRANK:
			return kr.getKwTextrank();
		case TFIDF:
			return kr.getKwTfidf();
		case TEXTRANKMULWIN:
			return kr.getKwTextrankMulWinSize();
		case TEXTRANKIDF:
			return kr.getKwTextrankIdf();
		case TEXTRANKMULWINIDF:
			return kr.getKwTextrankMulWinIdf();
		}
		return null;
	}
}