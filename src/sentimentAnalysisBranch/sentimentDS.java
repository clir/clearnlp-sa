package sentimentAnalysisBranch;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class sentimentDS implements Serializable {
	private static final long serialVersionUID = 844801659782414882L;
	protected List<Double> stanfordResults;
	protected Map<String,Double> intensifiers;
	protected Map<String,Double> wordSentiments;
	protected Map<String, Double> labelIntensifiers;
	protected String devFile = "src/Stanford Sentiment/trees/dev.txt";
	protected String trainFile = "src/Stanford Sentiment/trees/train.txt";
	protected String rawscores = "src/Stanford Sentiment/stanfordSentimentTreebankRaw/rawscores_exp12.txt";
	protected String sentexp = "src/Stanford Sentiment/stanfordSentimentTreebankRaw/sentlex_exp12.txt";
	protected String intensifierwords = "intensifiers.txt";
	
	public sentimentDS(){
		stanfordResults = new ArrayList<>();
		intensifiers = new HashMap<>();
		wordSentiments = new HashMap<>();
		labelIntensifiers = new HashMap<>();
		initLabelIntensifiers();
	}
	
	
	public void initLabelIntensifiers() {
		labelIntensifiers.put("neg", 1d);
		labelIntensifiers.put("cc", 1d);
		labelIntensifiers.put("advmod", 1d);
		labelIntensifiers.put("amod", 1d);
		labelIntensifiers.put("advcl", 1d);
		labelIntensifiers.put("appos", 1d);
		labelIntensifiers.put("npadvmod", 1d);
	}
	public void addLabelIntensifier(String word, double intensity){
		labelIntensifiers.put(word, intensity);
	}

	public void readStanfordResults() throws FileNotFoundException, IOException{
		parseStanfordScores.parseStanfordScores((new FileInputStream(trainFile)),stanfordResults);
	}
	public void readIntensifiers() throws FileNotFoundException, IOException{
		parseStanfordScores.parseIntensifiers((new FileInputStream(intensifierwords)), intensifiers);
	}
	public void readWordSentiments() throws Exception{
		parseStanfordScores.parseWordSentiments(wordSentiments, new FileInputStream(trainFile), "([(][0-9][\\s]([a-zA-Z]|\\W)+\\b[)])");
	}


	public void addSentiment(String word, double sentiment){
		wordSentiments.put(word,sentiment);
	}
	public void addIntensifier(String word, double intensity){
		intensifiers.put(word,intensity);
	}
	public void addLabelIntensifier1(String word, double intensity){
		labelIntensifiers.put(word, intensity);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public List<Double> getStanfordResults() {
		return stanfordResults;
	}



	public Map<String, Double> getIntensifiers() {
		return intensifiers;
	}


	public Map<String,Double> getLabelIntensifier(){
		return labelIntensifiers;
	}

	public Map<String, Double> getWordSentiments() {
		return wordSentiments;
	}

	
	
	
	
}
