package parseCorpus;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.junit.Test;

public class verifyEvaluationScores {

	
	private List<Integer> stanfordScores;
	private Map<String,Integer> dictionary;
	private Map<Integer,Double> phraseSentimentValues;
	public verifyEvaluationScores(){
		stanfordScores = new ArrayList<>();
		dictionary = new HashMap<>();
		phraseSentimentValues = new HashMap<>();
	}
	
	public void verify() throws Exception{
		String trainFile = "src/Stanford Sentiment/trees/train.txt";
		String dictionary = "src/Stanford Sentiment/stanfordSentimentTreebank/dictionary.txt";
		parseDictionary(new FileInputStream(dictionary));
		parseStanfordScores(new FileInputStream(trainFile));
		parsePhraseSentiments(new FileInputStream("src/Stanford Sentiment/stanfordSentimentTreebank/sentiment_labels.txt"));


	}
	
	private void parseStanfordScores(FileInputStream in) throws IOException{
		Scanner read = new Scanner(in);
		int count = 0;
		while(read.hasNext() && count < 8504){
			String s = read.nextLine();
			int number = Integer.parseInt(s.substring(1, 2));
			stanfordScores.add(number);
			count++;
		}
		read.close();
	}
	
	private void parseDictionary(FileInputStream in) throws IOException{
		BufferedReader read = new BufferedReader(new InputStreamReader(in));
		String line;
		String phrase;
		int phraseID;
		int separationIndex;
		while((line=read.readLine())!=null){
			separationIndex = line.indexOf("|");
			phrase = line.substring(0,separationIndex);
			phraseID = Integer.parseInt(line.substring(separationIndex+1, line.length()));
			dictionary.put(phrase, phraseID);
		}
	}
	
	private void parsePhraseSentiments(FileInputStream in) throws IOException{
		BufferedReader read = new BufferedReader(new InputStreamReader(in));
		String line;
		int separationIndex;
		int phraseID;
		double sentimentValue;
		while((line=read.readLine())!=null){
			separationIndex = line.indexOf("|");
			phraseID = Integer.parseInt(line.substring(0, separationIndex));
			sentimentValue = Double.parseDouble(line.substring(separationIndex+1,line.length()));
			phraseSentimentValues.put(phraseID, sentimentValue);
			
		}
	}
	
	
	@Test
	public void verifyScores() throws Exception{
		verifyEvaluationScores veri = new verifyEvaluationScores();
		veri.verify();
		String testFile = "trainFile.txt";
		FileInputStream in = new FileInputStream(testFile);
		BufferedReader read = new BufferedReader(new InputStreamReader(in));
		String line;
		int currIndex;
		double sentimentValue;
		int lineIndex =0;
		double treeSentiment;
		while((line=read.readLine())!=null){
			System.out.println(line);
			currIndex = veri.dictionary.get(line);	
			sentimentValue = veri.phraseSentimentValues.get(currIndex);
			treeSentiment = veri.stanfordScores.get(lineIndex++);
			try{
			assertEquals(treeSentiment/5, sentimentValue  , .2);
			System.out.println("Expected: " +treeSentiment +" " +treeSentiment/5 +  " Actual: " +sentimentValue + " Within Delta " + .2);
			}
			catch(AssertionError e){
			System.out.println("Expected: " +treeSentiment/5 +  " Actual: " +sentimentValue + " Within Delta " + .2);
			}
		}
	}
}
