package ParseCorpus;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParseWords {
	
	private Word word;
	
	public ParseWords() {
		word = new Word();
	}
	
	public void putWords(InputStream in, String pattern) throws Exception {
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		String line;
		Pattern regexpattern = Pattern.compile(pattern);
		Matcher matches;
		while ((line = reader.readLine()) != null) {
			matches = regexpattern.matcher(line);
			while(matches.find()) {
				String StringPair = matches.group();
				StringPair = StringPair.replaceAll("\\(", "").replaceAll("\\)","");
				String[] split = StringPair.split(" ");
				word.add(split[1],Integer.parseInt(split[0]));
			}
		}
	}
	
	public static void main(String[] args) throws Exception {
//		String devFile = "dev.txt";
//		String testFile = "test.txt";
		String trainFile = "train.txt";
		ParseWords PW = new ParseWords();
		PW.putWords(new FileInputStream(trainFile), "([(][0-9][\\s]([a-zA-Z]|\\W)+\\b[)])");
		PW.word.putInBuckets();
		List<Map<String,Double>> wordBuckets = PW.word.getWordBucket();
		System.out.println("done");
	}

}
