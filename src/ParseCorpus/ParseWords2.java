package ParseCorpus;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ParseWords2 {
	private SentimentExpression SentimentExpression;
	
	public ParseWords2(){
		SentimentExpression = new SentimentExpression();
	}
	
	public static void main(String[] args) throws Exception{
		String rawscores = "rawscores_exp12.txt";
		String sentexp = "sentlex_exp12.txt";
		ParseWords2 PSE = new ParseWords2();
		PSE.parseRawScores(new FileInputStream(rawscores));
		PSE.parseSentimentExpressions(new FileInputStream(sentexp));
	}

	private void parseSentimentExpressions(FileInputStream in) throws IOException {
	Scanner read = new Scanner(in);
	int currentIndex,nextIndex;
	String currentString,nextString;

	currentIndex=read.nextInt();
	currentString=read.nextLine();
	while(read.hasNext()){
		nextIndex=read.nextInt();
		nextString=read.nextLine();
		if(nextString.toLowerCase().contains(currentString.toLowerCase())){
			currentIndex=nextIndex;
			currentString=nextString;
		}
		else{
			System.out.println(""+currentIndex + "" + currentString);
			SentimentExpression.add(currentIndex, currentString);
			currentIndex = nextIndex;
			currentString= nextString;
		}
	}
	
	}
	

	private void parseRawScores(FileInputStream in) throws IOException {
	BufferedReader reader = new BufferedReader(new InputStreamReader(in));
	String line;
	while ((line = reader.readLine()) != null) {
	String[] split = line.split(" ");
	double average = (Integer.parseInt(split[1])+Integer.parseInt(split[2])+Integer.parseInt(split[3]))/3;
	SentimentExpression.addRawScore(Integer.parseInt(split[0]), average);
	}
}
}
