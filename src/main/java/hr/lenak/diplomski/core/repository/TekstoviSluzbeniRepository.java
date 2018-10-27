package hr.lenak.diplomski.core.repository;

import static hr.lenak.diplomski.core.model.generated.QTekstoviSluzbeni.tekstoviSluzbeni;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import hr.lenak.diplomski.core.model.SluzbeniDijelovi;
import hr.lenak.diplomski.core.model.TekstoviSluzbeni;

@Repository
@Transactional
public class TekstoviSluzbeniRepository extends QueryDslRepository<TekstoviSluzbeni, Long>{
	
	public List<TekstoviSluzbeni> findBySluzbeniDio(SluzbeniDijelovi sd) {
		return select(tekstoviSluzbeni)
			.from(tekstoviSluzbeni)
			.where(tekstoviSluzbeni.sluzbeniDijelovi.eq(sd))
			.fetch();
	}
	
	public List<TekstoviSluzbeni> findAll() {
		return select(tekstoviSluzbeni)
			.from(tekstoviSluzbeni)
			.orderBy(tekstoviSluzbeni.tsiId.asc())
			.fetch();
	}
}