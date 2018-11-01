package hr.lenak.diplomski.core.processing.tfidf;

import static hr.lenak.diplomski.core.processing.tfidf.TfIdfUtils.konstruirajDokument;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hr.lenak.diplomski.core.model.Rijec;
import hr.lenak.diplomski.core.model.TekstZakona;
import hr.lenak.diplomski.core.model.Token;
import hr.lenak.diplomski.core.processing.textrank.Vrh;
import hr.lenak.diplomski.web.util.Repositories;

public class Korpus {

	private static final double ACCURACY = 1e-6;
	private static  Logger log = LoggerFactory.getLogger(Korpus.class);
	private static int N;

	public static void inicijalizirajDokumente(List<TekstZakona> lista) {
		N = lista.size();
		
		for (TekstZakona tekstZakona : lista) {
			List<Token> tokeni = Repositories.tokenRepository.findByTekstZakona(tekstZakona);
			TfDokument dokument = konstruirajDokument(tokeni, tekstZakona);
			dokument.calculateTfsZaRijeci();
			
			Repositories.dokumentRepository.spremiPodatkeUBazu(dokument);
			log.debug("inicijaliziran dokument s brojem filea " + tekstZakona.getBrojFilea());
		}
	}
	
	public static void calculateIdfsZaSveRijeci(List<TekstZakona> lista) {
		for (TekstZakona tz : lista) {
			List<Rijec> rijeci = Repositories.rijecRepository.findAllRijeciForTekstZakona(tz.getTekstZakonaId());
			for (Rijec rijec : rijeci) {
				
				int pojaveUKorpusu = Repositories.tokenRepository.getBrojPojavaUKorpusuZaRijec(rijec).intValue();
				rijec.setPojaveUKorpusu(pojaveUKorpusu);
				
				Double idf = Math.log10((double)N / pojaveUKorpusu);
				//negativni idf se postiže ako je riječ u svakom dokumentu - stopword, ne treba se gledati
				if (idf < 0 || Math.abs(idf) < ACCURACY) {
					idf = ACCURACY;
				}
				
				rijec.setIdf(idf);
				rijec.setResult(rijec.getTf() * rijec.getIdf());
			}
			Repositories.rijecRepository.saveListuRijeci(rijeci);
			log.debug("savean idf za dokument s brojem filea " + tz.getBrojFilea());
		}
	}
	
	public static boolean dokumentSadrziRijec(TekstZakona dokument, Rijec rijec) {
		List<Rijec> rijeci = Repositories.rijecRepository.findAllRijeciForTekstZakona(dokument.getTekstZakonaId());
		return rijeci.contains(rijec);
	}
	
	public static void calculateValueForListuVrhova(List<Vrh> vrhovi) {
		for (Vrh vrh : vrhovi) {
			Double idf = Repositories.rijecRepository.findById(vrh.getToken().getTokenId()).getIdf();
			vrh.setValue(vrh.getValue() * idf);
		}
	}
}
