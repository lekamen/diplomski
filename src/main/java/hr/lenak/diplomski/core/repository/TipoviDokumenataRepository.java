package hr.lenak.diplomski.core.repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import hr.lenak.diplomski.core.model.TipoviDokumenata;

@Repository
@Transactional
public class TipoviDokumenataRepository extends QueryDslRepository<TipoviDokumenata, Long>{

}
