package parseCorpus;

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
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parse {
	
	
	
	private Word words;
	public Parse() {
		words = new Word();
	}
	public Word getWords() {
		return words;
	}

	public void parse() throws Exception {
		String devFile = "src/Stanford Sentiment/trees/dev.txt";
		String trainFile = "src/Stanford Sentiment/trees/train.txt";
		String rawscores = "src/Stanford Sentiment/stanfordSentimentTreebankRaw/rawscores_exp12.txt";
		String sentexp = "src/Stanford Sentiment/stanfordSentimentTreebankRaw/sentlex_exp12.txt";
		String intensifierwords = "";
		//trimWords(new FileInputStream(trainFile));
		parseStanfordTrees(words, new FileInputStream(trainFile), "([(][0-9][\\s]([a-zA-Z]|\\W)+\\b[)])");
		parseStanfordTrees(words, new FileInputStream(devFile), "([(][0-9][\\s]([a-zA-Z]|\\W)+\\b[)])");
		parseSubjectivity(words);
		parseStanfordScores(new FileInputStream(trainFile));
		parseStanfordScores(new FileInputStream(devFile));
		parseRawScores(new FileInputStream(rawscores));
		parseSentimentExpressions(new FileInputStream(sentexp));
		//trimKey(new FileInputStream("src/Stanford Sentiment/stanfordSentimentTreebank/datasetSplit.txt"));
		//writeSet(new File("src/Stanford Sentiment/stanfordSentimentTreebank/datasetSentences.txt"));
		//parseIntensifiers(new FileInputStream(intensifierwords));
		words.putInBuckets();
		
	}
	
	
	
	private void parseIntensifiers(FileInputStream in) throws IOException{
	Scanner read = new Scanner(in);
	String s;
	double intensity;
	
	while(read.hasNext()){
		
	}
	}
	
	
	
	
	
	private void parseStanfordScores(FileInputStream in) throws IOException{
		Scanner read = new Scanner(in);
		String s;
		int number;
		double normalized;
		while(read.hasNext()){
			s = read.nextLine();
			//System.out.println(s);
			number = Integer.parseInt(s.substring(1, 2));
			//System.out.println(number);
			normalized = number/4;
			words.addStanfordScore(normalized);
		}
		
	}
	private void parseSentimentExpressions(FileInputStream in) throws IOException {
		Scanner read = new Scanner(in);
		int currentIndex;
		String currentString;

		while(read.hasNext()){
		currentIndex=read.nextInt();
		currentString=read.nextLine();
		words.addExpression(currentIndex, currentString);
		//System.out.println(currentIndex+"" + "" +currentString);		
		}
		}
		

		private void parseRawScores(FileInputStream in) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		String line;
		String[] split;
		while ((line = reader.readLine()) != null) {
		split = line.split(" ");
		double average = (Integer.parseInt(split[1])+Integer.parseInt(split[2])+Integer.parseInt(split[3]))/3;
		//System.out.println(Integer.parseInt(split[0])+" " +average );
		words.addRawScore(Integer.parseInt(split[0]), average);
		}
		}
	
	
	private void parseSubjectivity(Word words) throws Exception {
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
		    	words.addToSentimentList(word.substring(6), score);
		    	lineScanner.close();
		    }
		    else {
		      System.out.println("Empty or invalid line. Unable to process.");
		    }
		  }
		scanner.close();
		
	}
	private void parseStanfordTrees(Word word, InputStream in, String pattern) throws Exception {
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
				//System.out.println(split[1] + "" + split[0]);
				word.addToSentimentList(split[1],Integer.parseInt(split[0]));
			}
		}
	}
	private void trimWords(InputStream file) throws IOException {
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
	private void trimKey(FileInputStream in){
		Scanner read = new Scanner(in);
		int key, set;
		while(read.hasNext()){
			key = read.nextInt();
			set = read.nextInt();
			words.addSentenceKey(key, set);
		}
		}
	
	private void writeSet(File fileInputStream) throws IOException {
	Scanner read = new Scanner(fileInputStream);
	Writer devWriter = new BufferedWriter( new OutputStreamWriter(new FileOutputStream( new File("devFile.txt"))));
	Writer testWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File("testFile.txt"))));
	Writer trainWriter = new BufferedWriter( new OutputStreamWriter(new FileOutputStream(new File("trainFile.txt"))));
	int sentenceNum,sentenceSetNum;
	String sentence;
	while(read.hasNextLine()){
		Scanner lineScanner = new Scanner(read.nextLine());
		sentenceNum = lineScanner.nextInt();
		lineScanner.useDelimiter("\\t");
		sentenceSetNum = words.getSentenceSet(sentenceNum);
		sentence = lineScanner.next();
		if(sentenceSetNum == 1){
			trainWriter.write(sentence+"\n");
		}
		if(sentenceSetNum == 2){
			testWriter.write(sentence+"\n");
		}
		if(sentenceSetNum == 3){
			devWriter.write(sentence+"\n");
		}
		lineScanner.close();
	}
	devWriter.close();
	testWriter.close();
	devWriter.close();
	
	
	}
}
