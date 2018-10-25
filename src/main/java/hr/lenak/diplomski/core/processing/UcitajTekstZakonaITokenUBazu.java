package hr.lenak.diplomski.core.processing;
import static hr.lenak.diplomski.core.processing.PretProcesiranjeZakona.checkIfContainsStoryTag;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hr.lenak.diplomski.core.model.TekstZakona;
import hr.lenak.diplomski.core.model.TekstoviSluzbeni;
import hr.lenak.diplomski.core.model.Token;
import hr.lenak.diplomski.core.model.enums.KategorijaTokenaEnum;
import hr.lenak.diplomski.web.util.Repositories;

public class UcitajTekstZakonaITokenUBazu {

	private static Logger log = LoggerFactory.getLogger("UcitajTekstZakonaUBazu");
	private static List<TekstoviSluzbeni> lista;
	
	private static final Set<String> rucnoObradjeneDatoteke = new HashSet<String>(Arrays.asList("1103-2.txt",
			"2342-7.txt", "2468-0.txt", "2586-0.txt", "4090-0.txt"));
	private static final String PATH_TO_TOKENS = "C:\\Users\\mabel\\tekstoviTagILema";
	
	static {
		lista = Repositories.tekstoviSluzbeniRepository.findAll();
	}
	
	@Transactional
	public static void ucitajTekstZakona() {
		for(int i = 0; i < lista.size(); i++) {
			if (!checkIfContainsStoryTag(new String(lista.get(i).getTekst(), Charset.forName("UTF-8")))) {
				log.debug("Id: " + lista.get(i).getTsiId() + " nema tag story");
				continue;
			}
			TekstZakona tekst = new TekstZakona();
			tekst.setBrojFilea(i);
			tekst.setTsiId(lista.get(i).getTsiId());
			tekst.setWordCount(0);
			
			Repositories.tekstZakonaRepository.create(tekst);
		}
	}
	
	@Transactional
	public static void ucitajTokene() {
		try (Stream<Path> paths = Files.walk(Paths.get(new File(PATH_TO_TOKENS).getPath()))) {
		    paths.filter(Files::isRegularFile).forEach(UcitajTekstZakonaITokenUBazu::obradiFile);
		} catch (IOException e) {
			log.error("ucitajTokene", e);
		}
	}
	
	private static void obradiFile(Path path) {
		try (BufferedReader br = new BufferedReader(new FileReader(path.toFile()))){
			
			String filename = path.getFileName().toString();
			Integer brojFilea = Integer.valueOf(filename.substring(0, filename.indexOf("-")));
			
			TekstZakona tekst = Repositories.tekstZakonaRepository.findByBrojFilea(brojFilea);
			
			boolean isRucnoObradjena = rucnoObradjeneDatoteke.contains(path.getFileName().toString());
			Integer newWordCount = obradiFile(br, tekst, isRucnoObradjena);
			tekst.setWordCount(newWordCount);
			Repositories.tekstZakonaRepository.update(tekst);
		} catch (IOException e) {
			log.error("obradiFile", e);
		}
	}
	
	private static Integer obradiFile(BufferedReader br, TekstZakona tekst, boolean isRucnoObradjena) throws IOException {
		String sCurrentLine;
		int valueIndex;
		int lemmaIndex;
		int tagIndex;
		if (isRucnoObradjena) {
			valueIndex = 1;
			lemmaIndex = 3;
			tagIndex = 2;
		}
		else {
			valueIndex = 0;
			lemmaIndex = 1;
			tagIndex = 2;
		}
		
		Integer currentWordCount = tekst.getWordCount();
		List<Token> tokeni = new ArrayList<Token>();
		while ((sCurrentLine = br.readLine()) != null) {
			if (sCurrentLine.isEmpty()) {
				continue;
			}
			String[] fields = sCurrentLine.split("\t");
			
			if (fields[valueIndex].equals("»") || fields[valueIndex].equals("«")) {
				System.out.println("ovdje");
				continue;
			}
			
			Token token = new Token();
			token.setTekstZakona(tekst);
			token.setValue(fields[valueIndex]);
			token.setLemma(fields[lemmaIndex]);
			token.setTag(fields[tagIndex]);
			token.setKategorija(KategorijaTokenaEnum.fromValue(token.getTag().substring(0, 1)));
			currentWordCount++;
			token.setPosition(currentWordCount);
			
			tokeni.add(token);
		}
		Repositories.tokenRepository.createListaTokena(tokeni);
		return currentWordCount;
	}
}
