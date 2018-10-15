package hr.lenak.diplomski.core.processing.tfidf;

import static hr.lenak.diplomski.core.processing.tfidf.TfIdfUtils.konstruirajDokument;

import java.util.ArrayList;
import java.util.List;

import hr.lenak.diplomski.core.model.Token;
import hr.lenak.diplomski.web.util.Repositories;

public class Korpus {

	ArrayList<Dokument> dokumenti = new ArrayList<>();
	private int N = 10; /* broj svih zakona */
	private static final double ACCURACY = 1e-6;
	
	public void inicijalizirajDokumente() {
		for (int i = 0; i < N; i++) {
			List<Token> tokeni = Repositories.tokenRepository.findByTekstZakona(Repositories.tekstZakonaRepository.findByBrojFilea(i));
			dokumenti.add(konstruirajDokument(tokeni));
		}
	}
	
	public void calculateIdfsZaDokument(Dokument dokument) {
		for (Rijec rijec : dokument.getRijeci()) {
			int pojaveUKorpusu = 0;
			for (Dokument doc : dokumenti) {
				if (doc.sadrziRijec(rijec)) {
					pojaveUKorpusu++;
				}
			}
			rijec.setPojaveUKorpusu(pojaveUKorpusu);
			
			Double idf = Math.log10((double)N / pojaveUKorpusu);
			//negativni idf se postiže ako je riječ u svakom dokumentu - stopword, ne treba se gledati
			if (idf < 0 || Math.abs(idf) < ACCURACY) {
				idf = ACCURACY;
			}
			
			rijec.setIdf(idf);
		}
	}
	
	public ArrayList<Dokument> getDokumenti() {
		return dokumenti;
	}
}
