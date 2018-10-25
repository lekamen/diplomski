package hr.lenak.diplomski.core.processing.tfidf;

import static hr.lenak.diplomski.core.processing.tfidf.TfIdfUtils.konstruirajDokument;

import java.util.ArrayList;
import java.util.List;

import hr.lenak.diplomski.core.model.TekstZakona;
import hr.lenak.diplomski.core.model.Token;
import hr.lenak.diplomski.core.processing.textrank.Vrh;
import hr.lenak.diplomski.web.util.Repositories;

public class Korpus {

	ArrayList<Dokument> dokumenti = new ArrayList<>();
	private static final double ACCURACY = 1e-6;
	private int N;
	
	public Korpus(List<TekstZakona> lista) {
		N = lista.size();
		inicijalizirajDokumente(lista);
	}
	
	private void inicijalizirajDokumente(List<TekstZakona> lista) {
		for (TekstZakona tekstZakona : lista) {
			List<Token> tokeni = Repositories.tokenRepository.findByTekstZakona(tekstZakona);
			dokumenti.add(konstruirajDokument(tokeni, tekstZakona));
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
	
	public void calculateValueForListuVrhova(List<Vrh> vrhovi) {
		for (Vrh vrh : vrhovi) {
			int pojaveUKorpusu = 0;
			for (Dokument doc : dokumenti) {
				if (doc.sadrziToken(vrh.getToken())) {
					pojaveUKorpusu++;
				}
			}
			Double idf = Math.log10((double)N / pojaveUKorpusu);
			//negativni idf se postiže ako je riječ u svakom dokumentu - stopword, ne treba se gledati
			if (idf < 0 || Math.abs(idf) < ACCURACY) {
				idf = ACCURACY;
			}
			
			vrh.setValue(vrh.getValue() * idf);
		}
	}
	
	public ArrayList<Dokument> getDokumenti() {
		return dokumenti;
	}
}
