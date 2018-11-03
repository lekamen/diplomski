package hr.lenak.diplomski.web.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PythonModule {

	private static Logger log = LoggerFactory.getLogger(PythonModule.class);
	private static final String PYTHON_PATH = "C:\\Python27\\python.exe";
	private static final String PYTHON_FILE_PATH = "C:\\Users\\mabel\\reldi-lib-master\\lemmatizeWords.py";
	private static final String PYTHON_INPUT_FILE_PATH = "C:\\Users\\mabel\\reldi-lib-master\\inputFile.txt";
	private static final String PYTHON_RESULT_PATH = "C:\\Users\\mabel\\reldi-lib-master\\lemmatizedWords.txt";

	public static String lemmatizeWords(String words) {
		
		try (BufferedWriter bw = new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream(PYTHON_INPUT_FILE_PATH),
					     Charset.forName("UTF-8").newEncoder()))) {
			bw.write(words);
		} catch (IOException e) {
			
		}
		
		int exitValue = -1;
		try {
			String[] cmd = new String[3];
			cmd[0] = "cmd.exe";
			cmd[1] = "/C";
			cmd[2] = PYTHON_PATH + " " + PYTHON_FILE_PATH;

			Runtime rt = Runtime.getRuntime();
			Process proc = rt.exec(cmd);
			// any error message?
			StreamGobbler errorGobbler = new StreamGobbler(proc.getErrorStream(), "ERROR");

			// any output?
			StreamGobbler outputGobbler = new StreamGobbler(proc.getInputStream(), "OUTPUT");

			// kick them off
			errorGobbler.start();
			outputGobbler.start();

			// any error???
			exitValue = proc.waitFor();
			log.debug("ExitValue: " + exitValue);
		} catch (Throwable t) {
			t.printStackTrace();
		}
		if (exitValue == 0) {
			String rijeci = "";
			try (BufferedReader br = new BufferedReader(new FileReader(PYTHON_RESULT_PATH))) {
				String sCurrentLine;
				while ((sCurrentLine = br.readLine()) != null) {
					if (sCurrentLine.isEmpty()) {
						continue;
					}
					
					String[] fields = sCurrentLine.split("\t");
					String lemma = fields[1];
					String tag = fields[2];
					if (!tag.startsWith("A") && !tag.startsWith("Nc")) {
						continue;
					}
					
					rijeci += lemma + " ";
				}
				return rijeci;
			} catch (IOException e) {
				log.error("tokeniziranje", e);
			}
		}
		return null;
	}

	public static class StreamGobbler extends Thread {
		InputStream is;
		String type;

		StreamGobbler(InputStream is, String type) {
			this.is = is;
			this.type = type;
		}

		public void run() {
			try {
				InputStreamReader isr = new InputStreamReader(is);
				BufferedReader br = new BufferedReader(isr);
				String line = null;
				while ((line = br.readLine()) != null)
					log.debug(type + ">" + line);
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
	}
}
