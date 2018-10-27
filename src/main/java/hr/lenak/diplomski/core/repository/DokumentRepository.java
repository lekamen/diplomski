package hr.lenak.diplomski.core.repository;

import static hr.lenak.diplomski.core.model.generated.QDokument.dokument;

import java.util.HashSet;

import org.springframework.transaction.annotation.Transactional;

import org.springframework.stereotype.Repository;

import hr.lenak.diplomski.core.model.Dokument;
import hr.lenak.diplomski.core.model.Rijec;
import hr.lenak.diplomski.core.model.SvePojaveRijeci;
import hr.lenak.diplomski.core.model.TekstZakona;
import hr.lenak.diplomski.core.model.Token;
import hr.lenak.diplomski.core.processing.tfidf.TfDokument;
import hr.lenak.diplomski.core.processing.tfidf.TfRijec;
import hr.lenak.diplomski.web.util.Repositories;

@Repository
public class DokumentRepository extends QueryDslRepository<Dokument, Long>{

	@Transactional
	public Dokument findByTekstZakona(TekstZakona tekstZakona) {
		return select(dokument)
			.from(dokument)
			.where(dokument.tekstZakonaId.eq(tekstZakona.getTekstZakonaId()))
			.fetchOne();
	}
	
	@Transactional
	public void spremiPodatkeUBazu(TfDokument dokument) {
		Long tekstZakonaId = dokument.getTekstZakona().getTekstZakonaId();
		Dokument doc = new Dokument();
		doc.setTekstZakonaId(tekstZakonaId);
		doc.setNajcescaRijecId(dokument.getNajcescaRijecUDokumentu().getToken().getTokenId());
		Repositories.dokumentRepository.create(doc);
		
		HashSet<TfRijec> rijeci = dokument.getRijeci();

		for (TfRijec rijec : rijeci) {
			Rijec r = new Rijec();
			r.setTekstZakonaId(tekstZakonaId);
			r.setPojaveUDokumentu(rijec.getPojaveUDokumentu());
			r.setTf(rijec.getTf());
			r.setTokenId(rijec.getToken().getTokenId());
			
			Repositories.rijecRepository.create(r);
			
			for (Token token : rijec.getSvePojaveRijeci()) {
				SvePojaveRijeci spr = new SvePojaveRijeci();
				spr.setTokenIdPrvi(rijec.getToken().getTokenId());
				spr.setTokenIdDrugi(token.getTokenId());
				Repositories.svePojaveRijeciRepository.create(spr);
			}
			
		}

	}
}
