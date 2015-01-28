package ParseCorpus;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;

public class ParseSentence {

	
	
	
	
	public static void main(String[] args) throws IOException{
		String devFile = "dev.txt";
		String testFile = "test.txt";
		String trainFile = "train.txt";
		trimWords(new FileInputStream(trainFile));
	}

	private static void trimWords(InputStream file) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(file));
		String line;
		File sentText = new File("Sentences.txt");
        FileOutputStream Output = new FileOutputStream(sentText);
        OutputStreamWriter osw = new OutputStreamWriter(Output);
        Writer w = new BufferedWriter(osw);
		while ((line = reader.readLine()) != null) {
			line = line.replaceAll("[(][0-9]", "").replaceAll("\\)","");
			line.trim();
			line = line.replaceAll("\\s+", " ");
			line = line.replaceAll("[\\s]['][s]", "'s");
			line = line.replaceAll("[\\s]['][m]", "'m");
			line = line.replaceAll("[\\s]['][t]", "'t");
			line = line.replaceAll("[\\s]['][l]", "'l");
			line = line.replaceAll("[\\s]['][r]", "'r");
			line = line.replaceAll("[\\s][,]", ",");
			line = line.replaceAll("[\\s][.]", ".");
			w.write(line);
		}
		w.close();
		
	}
}
