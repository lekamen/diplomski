package hr.lenak.diplomski.core.repository;

import static hr.lenak.diplomski.core.model.generated.QSluzbeniDijelovi.sluzbeniDijelovi;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import hr.lenak.diplomski.core.model.NarodneNovine;
import hr.lenak.diplomski.core.model.SluzbeniDijelovi;

@Repository
@Transactional
public class SluzbeniDijeloviRepository extends QueryDslRepository<SluzbeniDijelovi, Long>{

	public List<SluzbeniDijelovi> findByKriterij(String naslov, String donositelj, NarodneNovine novine) {
		return select(sluzbeniDijelovi)
			.from(sluzbeniDijelovi)
			.where(
				naslov != null ? sluzbeniDijelovi.naslov.containsIgnoreCase(naslov) : null,
				donositelj != null ? sluzbeniDijelovi.donositelj.containsIgnoreCase(donositelj) : null,
				novine != null ? sluzbeniDijelovi.narodneNovine.eq(novine) : null
			)
			.fetch();
	}
	
	public List<SluzbeniDijelovi> findByNarodneNovine(NarodneNovine novine) {
		return select(sluzbeniDijelovi)
			.from(sluzbeniDijelovi)
			.where(sluzbeniDijelovi.narodneNovine.eq(novine))
			.fetch();
	}
}