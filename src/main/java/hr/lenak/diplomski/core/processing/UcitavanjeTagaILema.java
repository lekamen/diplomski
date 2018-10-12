package hr.lenak.diplomski.core.processing;

import java.io.BufferedReader;
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
	//private static final String DIRECTORY_PATH = UcitavanjeTagaILema.class.getClassLoader().getResource("static/tekstoviTagILema").getFile();
	private static final String DIRECTORY_PATH = "static/tekstoviTest";
	private List<TekstZakona> tekstovi = new ArrayList<>();
	private final Set<String> rucnoObradjeneDatoteke = new HashSet<String>(Arrays.asList("1103-2.txt",
		"2342-7.txt", "2468-0.txt", "2586-0.txt", "4090-0.txt"));
	
	public UcitavanjeTagaILema() {
		
	}
	
	public void ucitajTagoveILeme() {
		try (Stream<Path> paths = Files.walk(Paths.get(DIRECTORY_PATH))) {
		    paths.forEach(this::obradiFile);
		} catch (IOException e) {
			log.debug(e.getMessage());
		}
	}
	
	private void obradiFile(Path path) {
		TekstZakona tekst = new TekstZakona();
		try (BufferedReader br = new BufferedReader(new FileReader(path.toFile()))){
			
			System.out.println(path.getFileName());
			
			String sCurrentLine;
			while ((sCurrentLine = br.readLine()) != null) {
				System.out.println(sCurrentLine);
			}
			
			tekstovi.add(tekst);
		} catch (IOException e) {
			log.debug(e.getMessage());
		}
	}
}
