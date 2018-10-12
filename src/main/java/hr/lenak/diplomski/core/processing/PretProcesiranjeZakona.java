package hr.lenak.diplomski.core.processing;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hr.lenak.diplomski.core.model.TekstoviSluzbeni;
import hr.lenak.diplomski.web.util.Repositories;

/**
 * Klasa dohvaća sve članke u HTML-obliku, extracta naslov i njihov sadržaj te ih sprema u datoteke (koje imaju 
 * max 4000 znakova).
 * Svi tekstovi su u resources/static/tekstoviSluzbeni
 */
public class PretProcesiranjeZakona {

	private static Logger log = LoggerFactory.getLogger("PretProcesiranjeZakona");
	private static final String DIRECTORY_PATH = "C:\\Users\\mabel\\tekstoviSluzbeni";
	private static List<TekstoviSluzbeni> lista;
	
	static {
		lista = Repositories.tekstoviSluzbeniRepository.findAll();
	}
	
	/**
	 * Iz HTML dokumenta vraća sadržaj elementa title i sadržaj elementa čija je klasa "story"
	 */
	private static String parseText(String text) {
		StringBuilder sb = new StringBuilder();
		Document doc = Jsoup.parse(text);
		String title = doc.title();
		sb.append(title).append("\n");
		Element story = doc.getElementsByClass("story").get(0);
		for (Element element : story.children()) {
			sb.append(element.text()).append("\n");
		}
		return sb.toString();
	}
	
	/**
	 * Provjerava sadrži li HTML dokument element s klasom "story"
	 */
	private static boolean checkIfContainsStoryTag(String text) {
		Document doc = Jsoup.parse(text);
		try {
			doc.getElementsByClass("story").get(0);
		} catch (IndexOutOfBoundsException e) {
			return false;
		}
		return true;
	}
	
	/**
	 * Metoda koja sve članke sprema u datoteke
	 */
	public static void saveAllElementsInFiles() {
		for(int i = 0; i < lista.size(); i++) {
			String tekst = new String(lista.get(i).getTekst(), Charset.forName("UTF-8"));
			if (!checkIfContainsStoryTag(tekst)) {
				log.debug("Id: " + lista.get(i).getTsiId() + " nema tag story"); //TODO: 434638L JE PRESKOČEN
				continue;
			}
			writeInFile(parseText(tekst), String.valueOf(i));
		}
	}
	
	/**
	 * Metoda koja jedan dokument sprema u datoteke od po max 4000 znakova
	 */
	private static void writeInFile(String text, String fileName) {
		List<String> chunks = new ArrayList<String>();
		int index = 0;
		final int CHUNK_SIZE = 4000;
		while (index < text.length()) {
			int min = Math.min(index + CHUNK_SIZE,text.length());
			//split se radi samo na prazninama
			if (min != text.length() && !Character.isWhitespace(text.charAt(min - 1)) && !Character.isWhitespace(text.charAt(min))) {
				int korekcija = min;
				while (!Character.isWhitespace(text.charAt(korekcija))) {
					korekcija--;
				}
				min = korekcija;
			}
		    chunks.add(text.substring(index, min));
		    index = min;
		}
		
		for (int i = 0; i < chunks.size(); i++) {
			try (BufferedWriter bw = new BufferedWriter(
					new OutputStreamWriter(
						     new FileOutputStream(DIRECTORY_PATH + fileName + "-" + i + ".txt"),
						     Charset.forName("UTF-8").newEncoder()))) {
				bw.write(chunks.get(i));
			} catch (IOException e) {
				
			}
		}
	}
}
