package hr.lenak.diplomski.web.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import hr.lenak.diplomski.core.repository.NarodneNovineRepository;
import hr.lenak.diplomski.core.repository.SluzbeniDijeloviRepository;
import hr.lenak.diplomski.core.repository.TekstoviSluzbeniRepository;
import hr.lenak.diplomski.core.repository.TipoviDokumenataRepository;

@Component
public class Repositories {

	public static NarodneNovineRepository narodneNovineRepository;
	public static SluzbeniDijeloviRepository sluzbeniDijeloviRepository;
	public static TekstoviSluzbeniRepository tekstoviSluzbeniRepository;
	public static TipoviDokumenataRepository tipoviDokumenataRepository;
	
	@Autowired
	public Repositories(
		NarodneNovineRepository narodneNovineRepository,
		SluzbeniDijeloviRepository sluzbeniDijeloviRepository,
		TekstoviSluzbeniRepository tekstoviSluzbeniRepository,
		TipoviDokumenataRepository tipoviDokumenataRepository
	) {
		Repositories.narodneNovineRepository = narodneNovineRepository;
		Repositories.sluzbeniDijeloviRepository = sluzbeniDijeloviRepository;
		Repositories.tekstoviSluzbeniRepository = tekstoviSluzbeniRepository;
		Repositories.tipoviDokumenataRepository = tipoviDokumenataRepository;
	}
}
