package hr.lenak.diplomski.core.repository;

import static hr.lenak.diplomski.core.model.generated.QSvePojaveRijeci.svePojaveRijeci;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import hr.lenak.diplomski.core.model.SvePojaveRijeci;
import hr.lenak.diplomski.core.model.Token;

@Repository
public class SvePojaveRijeciRepository extends QueryDslRepository<SvePojaveRijeci, Long>{

	@Transactional
	public List<SvePojaveRijeci> findAllPojaveRijeciForPrviToken(Long prviTokenId) {
		return select(svePojaveRijeci)
			.from(svePojaveRijeci)
			.where(svePojaveRijeci.tokenIdPrvi.eq(prviTokenId))
			.fetch();
	}
}
