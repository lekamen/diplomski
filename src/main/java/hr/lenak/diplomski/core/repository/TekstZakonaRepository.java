package hr.lenak.diplomski.core.repository;

import static hr.lenak.diplomski.core.model.generated.QTekstZakona.tekstZakona;

import org.springframework.stereotype.Repository;

import hr.lenak.diplomski.core.model.TekstZakona;

@Repository
public class TekstZakonaRepository extends QueryDslRepository<TekstZakona, Long> {

	public TekstZakona findByBrojFilea(Integer brojFilea) {
		return select(tekstZakona)
			.from(tekstZakona)
			.where(tekstZakona.brojFilea.eq(brojFilea))
			.fetchOne();
	}
}
