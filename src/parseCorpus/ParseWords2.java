package parseCorpus;

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
		String rawscores = "src/Stanford Sentiment/stanfordSentimentTreebankRaw/rawscores_exp12.txt";
		String sentexp = "src/Stanford Sentiment/stanfordSentimentTreebankRaw/sentlex_exp12.txt";
		ParseWords2 PSE = new ParseWords2();
		PSE.parseRawScores(new FileInputStream(rawscores));
		PSE.parseSentimentExpressions(new FileInputStream(sentexp));
		System.out.println("done");
	}

	private void parseSentimentExpressions(FileInputStream in) throws IOException {
	Scanner read = new Scanner(in);
	int currentIndex;
	String currentString;

	while(read.hasNext()){
	currentIndex=read.nextInt();
	currentString=read.nextLine();
	SentimentExpression.add(currentIndex, currentString);
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
	SentimentExpression.addRawScore(Integer.parseInt(split[0]), average);
	}
}
}
