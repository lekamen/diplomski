package hr.lenak.diplomski.core.repository;

import static hr.lenak.diplomski.core.model.generated.QRijec.rijec;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import hr.lenak.diplomski.core.model.Rijec;

@Repository
public class RijecRepository extends QueryDslRepository<Rijec, Long> {
	
	@Transactional
	public List<Rijec> findAllRijeciForTekstZakona(Long tekstZakonaId) {
		return select(rijec)
			.from(rijec)
			.where(rijec.tekstZakonaId.eq(tekstZakonaId))
			.fetch();
	}
	
	@Transactional
	public void saveListuRijeci(List<Rijec> rijeci) {
		for (Rijec rijec : rijeci) {
			this.update(rijec);
		}
	}
}
