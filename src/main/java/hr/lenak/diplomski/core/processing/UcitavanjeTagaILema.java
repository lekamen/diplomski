package hr.lenak.diplomski.core.processing;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UcitavanjeTagaILema {
	
	private Logger log = LoggerFactory.getLogger(this.getClass());
	private List<TekstZakona> tekstovi = new ArrayList<>();
	private final Set<String> rucnoObradjeneDatoteke = new HashSet<String>(Arrays.asList("1103-2.txt",
		"2342-7.txt", "2468-0.txt", "2586-0.txt", "4090-0.txt"));
	
	public UcitavanjeTagaILema() {
		ucitajTagoveILeme();
	}
	
	private void ucitajTagoveILeme() {
		final String path = this.getClass().getClassLoader().getResource("static/tekstoviTest/").getFile();
		try (Stream<Path> paths = Files.walk(Paths.get(new File(path).getPath()))) {
		    paths.filter(Files::isRegularFile)
		    	.forEach(this::obradiFile);
		} catch (IOException e) {
			log.error("ucitajTagoveILeme", e);
		}
	}
	
	private void obradiFile(Path path) {
		try (BufferedReader br = new BufferedReader(new FileReader(path.toFile()))){
			
			String filename = path.getFileName().toString();
			Integer brojFilea = Integer.valueOf(filename.substring(0, filename.indexOf("-")));
			
			TekstZakona tekst = new TekstZakona();
			tekst.setBrojFilea(brojFilea);
			boolean fileVecPostoji = tekstovi.contains(tekst);
			if (fileVecPostoji) {
				tekst = tekstovi.get(tekstovi.indexOf(tekst));
			}
			else {
				tekst.setStory(new ArrayList<Token>());
			}
			
			boolean isRucnoObradjena = rucnoObradjeneDatoteke.contains(path.getFileName().toString());
			obradiFile(br, tekst, isRucnoObradjena);

			if (!fileVecPostoji) {
				tekstovi.add(tekst);
			}
		} catch (IOException e) {
			log.error("obradiFile", e);
		}
	}
	
	private TekstZakona obradiFile(BufferedReader br, TekstZakona tekst, boolean isRucnoObradjena) throws IOException {
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
		
		List<Token> tokeni = new ArrayList<Token>();
		while ((sCurrentLine = br.readLine()) != null) {
			if (sCurrentLine.isEmpty()) {
				continue;
			}
			String[] fields = sCurrentLine.split("\t");
			
			Token token = new Token();
			token.setValue(fields[valueIndex]);
			token.setLemma(fields[lemmaIndex]);
			token.setTag(fields[tagIndex]);
			token.setKategorija(KategorijaTokenaEnum.fromValue(token.getLemma().substring(0, 1)));
			
			tokeni.add(token);
		}
		tekst.getStory().addAll(tokeni);
		return tekst;
	}
	
	public void provjeri() {
		log.debug("veličina liste: " + tekstovi.size());
		for (TekstZakona t : tekstovi) {
			log.debug(t.toString());
		}
	}
}
