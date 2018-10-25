package hr.lenak.diplomski.core.repository;

import static hr.lenak.diplomski.core.model.generated.QKljucneRijeci.kljucneRijeci;

import java.util.List;

import org.springframework.stereotype.Repository;

import hr.lenak.diplomski.core.model.KljucneRijeci;
import hr.lenak.diplomski.core.processing.VrstaAlgoritmaEnum;


@Repository
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
}