package hr.lenak.diplomski.core.repository;

import static hr.lenak.diplomski.core.model.generated.QNarodneNovine.narodneNovine;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import hr.lenak.diplomski.core.model.NarodneNovine;

@Repository
@Transactional
public class NarodneNovineRepository extends QueryDslRepository<NarodneNovine, Long>{

	public List<NarodneNovine> findAll() {
		return select(narodneNovine)
			.from(narodneNovine)
			.orderBy(narodneNovine.broj.asc())
			.fetch();
	}
	
	public List<NarodneNovine> findByKriterij(BigDecimal broj, LocalDateTime odDate, LocalDateTime doDate) {
		return select(narodneNovine)
			.from(narodneNovine)
			.where(
				broj != null ? narodneNovine.broj.eq(broj) : null,
				odDate != null ? narodneNovine.datumIzdanja.goe(odDate) : null,
				doDate != null ? narodneNovine.datumIzdanja.loe(doDate) : null
			)
			.fetch();
	}
}
