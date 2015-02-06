package parseCorpus;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parse {
	
	private Word words;
	public Parse() {
		words = new Word();
	}

//	public static void main(String[] args) throws Exception {
	public void parse() throws Exception {
		String devFile = "src/Stanford Sentiment/trees/dev.txt";
		String trainFile = "src/Stanford Sentiment/trees/train.txt";
		
		parseStanfordTrees(words, new FileInputStream(trainFile), "([(][0-9][\\s]([a-zA-Z]|\\W)+\\b[)])");
		parseStanfordTrees(words, new FileInputStream(devFile), "([(][0-9][\\s]([a-zA-Z]|\\W)+\\b[)])");
		parseSubjectivity(words);
		words.putInBuckets();
		
//		List<Map<String,Double>> wordBuckets = words.getWordBucket();
	}
	
	public Word getWords() {
		return words;
	}
	public static void parseSubjectivity(Word words) throws Exception {
		File file = new File("subjectivity.txt");
		Scanner scanner =  new Scanner(file);
		while (scanner.hasNextLine()){
			Scanner lineScanner = new Scanner(scanner.nextLine());
		    lineScanner.useDelimiter(" ");
		    if (lineScanner.hasNext()){
		    	lineScanner.next();
		    	lineScanner.next();
		    	String word = lineScanner.next();
		    	lineScanner.next();
		    	lineScanner.next();
		    	String polarity = lineScanner.next();
		    	int score = 2;
		    	if (polarity.substring(14).equals("positive"))
		    		score = 0;
		    	if (polarity.substring(14).equals("negative"))
			    	score = 4;
		    	words.add(word.substring(6), score);
		    	lineScanner.close();
		    }
		    else {
		      System.out.println("Empty or invalid line. Unable to process.");
		    }
		  }
		scanner.close();
		
	}
	public static void parseStanfordTrees(Word word, InputStream in, String pattern) throws Exception {
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
}
