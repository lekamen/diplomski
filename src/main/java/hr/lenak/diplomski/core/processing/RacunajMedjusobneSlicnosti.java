package hr.lenak.diplomski.core.processing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hr.lenak.diplomski.core.model.KljucneRijeci;
import hr.lenak.diplomski.core.model.TekstZakona;
import hr.lenak.diplomski.web.util.Repositories;

public class RacunajMedjusobneSlicnosti {

	private static List<TekstZakona> lista;
	private static Logger log = LoggerFactory.getLogger(RacunajMedjusobneSlicnosti.class);
	private static int ukupanBroj = 0;
	private static final String DELIMITERS = ", | ";
	
	public static void setLista(List<TekstZakona> lista) {
		RacunajMedjusobneSlicnosti.lista = lista;
	}
	
	public static void racunajSlicnosti() {
		int brojIstihPrvihKljucnihRijeci = 0;
		int N = lista.size();
		
		double slicnostSvihKljucnihRijeci = 0;
		double slicnostTextRankITextRankMulWin = 0;
		double slicnostTextRankITfidf = 0;
		double slicnostTextRankMulwinITfidf = 0;
		double slicnostTfidfITextrankIdf = 0;
		double slicnostTextrankITextrankidf = 0;
		double slicnostTextrankITextrankmulwinidf = 0;
		double slicnostTfidfITextrankmulwinidf = 0;
		double slicnostTextrankmulwinITextrankmulwinidf = 0;
		for (TekstZakona tz : lista) {
			KljucneRijeci kr = Repositories.kljucneRijeciRepository.findKljucneRijeci(tz.getTsiId(), tz.getBrojFilea(), tz.getTekstZakonaId());

			String textrank = kr.getKwTextrank();
			String textrankMulWinSize = kr.getKwTextrankMulWinSize();
			String tfidf = kr.getKwTfidf();
			String textrankIdf = kr.getKwTextrankIdf();
			String textrankMulWinIdf = kr.getKwTextrankMulWinIdf();
			
			if (isPrvaKljucnaRijecIsta(textrank, textrankMulWinSize, tfidf, textrankIdf, textrankMulWinIdf)) {
				brojIstihPrvihKljucnihRijeci++;
			}
			
			slicnostSvihKljucnihRijeci += racunajSlicnost(textrank, textrankMulWinSize, tfidf, textrankIdf, textrankMulWinIdf);
			slicnostTextRankITextRankMulWin += racunajSlicnost(textrank, textrankMulWinSize);
			slicnostTextRankITfidf += racunajSlicnost(textrank, tfidf);
			slicnostTextRankMulwinITfidf += racunajSlicnost(textrankMulWinSize, tfidf);
			slicnostTfidfITextrankIdf += racunajSlicnost(tfidf, textrankIdf);
			slicnostTextrankITextrankidf += racunajSlicnost(textrank, textrankIdf);
			slicnostTextrankITextrankmulwinidf += racunajSlicnost(textrank, textrankMulWinIdf);
			slicnostTfidfITextrankmulwinidf += racunajSlicnost(tfidf, textrankMulWinIdf);
			slicnostTextrankmulwinITextrankmulwinidf += racunajSlicnost(textrankMulWinSize, textrankMulWinIdf);
			
		}
		
		log.debug("Prve ključne riječi iste: " + brojIstihPrvihKljucnihRijeci + "/" + N + " = " + (double)brojIstihPrvihKljucnihRijeci/N);
		log.debug("");
		log.debug("Sličnost svih ključnih riječi: " + slicnostSvihKljucnihRijeci + "/" + N + " = " + (double)slicnostSvihKljucnihRijeci/N);
		log.debug("Sličnost textrank i textrankmulwin: " + slicnostTextRankITextRankMulWin + "/" + N + " = " + (double)slicnostTextRankITextRankMulWin/N);
		log.debug("Sličnost textrank i tfidf: " + slicnostTextRankITfidf + "/" + N + " = " + (double)slicnostTextRankITfidf/N);
		log.debug("Sličnost textrankmulwin i tfidf: " + slicnostTextRankMulwinITfidf + "/" + N + " = " + (double)slicnostTextRankMulwinITfidf/N);
		log.debug("Sličnost tfidf i textrankidf: " + slicnostTfidfITextrankIdf + "/" + N + " = " + (double)slicnostTfidfITextrankIdf/N);
		log.debug("Sličnost textrank i textrankidf: " + slicnostTextrankITextrankidf + "/" + N + " = " + (double)slicnostTextrankITextrankidf/N);
		log.debug("Sličnost textrank i textrankmulwinidf: " + slicnostTextrankITextrankmulwinidf + "/" + N + " = " + (double)slicnostTextrankITextrankmulwinidf/N);
		log.debug("Sličnost tfidf i textrankmulwinidf: " + slicnostTfidfITextrankmulwinidf + "/" + N + " = " + (double)slicnostTfidfITextrankmulwinidf/N);
		log.debug("Sličnost textrankmulwin i textrankmulwinidf: " + slicnostTextrankmulwinITextrankmulwinidf + "/" + N + " = " + (double)slicnostTextrankmulwinITextrankmulwinidf/N);
	}
	
	
	@SafeVarargs
	private static double racunajSlicnost(String... kljucneRijeci) {
		
		List<String> prvaLista = new ArrayList<>(Arrays.asList(kljucneRijeci[0].split(DELIMITERS)));
		List<List<String>> liste = new ArrayList<>();
		for (int i = 1; i < kljucneRijeci.length; i++) {
			liste.add(new ArrayList<String>(Arrays.asList(kljucneRijeci[i].split(DELIMITERS))));
		}

		ukupanBroj = prvaLista.size();
		liste.forEach(l -> ukupanBroj += l.size());
		
		int brojIstihKljucnihRijeci = 0;
		
		brojIstihKljucnihRijeci += vratiBrojIstihKljucnihRijeci(prvaLista, liste);		
		brojIstihKljucnihRijeci *= (liste.size() + 1);
		
		return (double)brojIstihKljucnihRijeci/ukupanBroj;
	}
	
	private static int vratiBrojIstihKljucnihRijeci(List<String> keywords, List<List<String>> liste) {
		int brojIstih = 0;
		for (String s : keywords) {
			if (liste.stream().allMatch(l -> l.contains(s))) {
				brojIstih++;
			}
			
			liste.forEach(l -> l.remove(s));
		}
		return brojIstih;
	}
	
	private static boolean isPrvaKljucnaRijecIsta(String textrank, String textrankMulwin, String tfidf, String textrankidf, String textrankmulwinIdf) {
		List<String> strings = Arrays.asList(textrank.split(", ")[0], textrankMulwin.split(", ")[0], tfidf.split(", ")[0],
				textrankidf.split(", ")[0], textrankmulwinIdf.split(", ")[0]);
		
		return strings.stream().allMatch(s -> s.equals(strings.get(0)));
	}
}
