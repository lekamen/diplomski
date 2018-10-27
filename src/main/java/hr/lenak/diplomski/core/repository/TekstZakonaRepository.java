package hr.lenak.diplomski.core.repository;

import static hr.lenak.diplomski.core.model.generated.QTekstZakona.tekstZakona;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import hr.lenak.diplomski.core.model.TekstZakona;

@Repository
@Transactional
public class TekstZakonaRepository extends QueryDslRepository<TekstZakona, Long> {

	public TekstZakona findByBrojFilea(Integer brojFilea) {
		return select(tekstZakona)
			.from(tekstZakona)
			.where(tekstZakona.brojFilea.eq(brojFilea))
			.fetchOne();
	}
	
	public List<TekstZakona> findAll() {
		return select(tekstZakona)
			.from(tekstZakona)
			.orderBy(tekstZakona.brojFilea.asc())
			.fetch();
	}
}
