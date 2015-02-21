package parseCorpus;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Word implements Serializable {
	private static final long serialVersionUID = 844801659782414882L;
	private Map<String,List<Double>> sentimentListMap;
	private Map<String,Double> wordBucket;
	private Map<Integer,Double> rawScores;
	private Map<String,Double> sentimentExpression;
	private List<Double> stanfordScores;
	private Map<String,Double> intensifierWords;
	private Map<Integer,Integer> sentenceKeys;
	
	public Word() {
		sentimentListMap = new HashMap<>();
		wordBucket = new HashMap<>();
		rawScores = new HashMap<>();
		sentimentExpression = new HashMap<>();
		stanfordScores = new ArrayList<>();
		intensifierWords = new HashMap<>();
		sentenceKeys = new HashMap<>();
	}
	
	public void addStanfordScore(double score){
		stanfordScores.add(score);
	}
	public void addRawScore(int index, double average){
		rawScores.put(index, average);
	}
	public void addExpression(int Index, String expression){
		double average = rawScores.get(Index);
		average = (average-1)/25;
		sentimentExpression.put(expression,average);
	}
	//1/2x-1 --> normalize to -1 and 1
	public void addToSentimentList(String word, double score) {
		double s = (.5*score)-1;
		if(!sentimentListMap.containsKey(word))
		{
			List<Double> tmp = new ArrayList<>();
			tmp.add(s);
			sentimentListMap.put(word, tmp);
		}
		else {
			List<Double> answer = sentimentListMap.get(word);
			answer.add(s);
			sentimentListMap.put(word, answer);
		}
	}
	
	public boolean isWord(String word){
		if(sentimentListMap.containsKey(word)){
			return true;
		}
		return false;
	}
	
	public double getAverageSentiment(List<Double> sentiments) {
		double average = 0d;
		for (double sentiment : sentiments) {
			average += sentiment;
		}
		return average/sentiments.size();
	}
	
	public Map<String, List<Double>> getSentimentListMap() {
		return sentimentListMap;
	}
	
	public Map<String,Double> getWordBucket() {
		return wordBucket;
	}
	
	//For every list of sentiments for each word we put the average sentiment in wordBucket
	public void putInBuckets() {
		for (Entry<String, List<Double>> entry : sentimentListMap.entrySet()) {
			double average = getAverageSentiment(entry.getValue());
//			if (average < 0)
//				average = -1;
//			else if (average > 0) {
//				average = 1;
//			}
			wordBucket.put(entry.getKey(), average);
		}
	}

	public List<Double> getStanfordScores() {
		return stanfordScores;
	}
	
	public void addSentenceKey(int key, int set){
		sentenceKeys.put(key, set);
	}
	public int getSentenceSet(int key){
		return sentenceKeys.get(key);
	}

	public Map<Integer, Double> getRawScores() {
		return rawScores;
	}

	public void setRawScores(Map<Integer, Double> rawScores) {
		this.rawScores = rawScores;
	}

	public Map<String, Double> getSentimentExpression() {
		return sentimentExpression;
	}

	public void setSentimentExpression(Map<String, Double> sentimentExpression) {
		this.sentimentExpression = sentimentExpression;
	}

	public Map<String, Double> getIntensifierWords() {
		return intensifierWords;
	}

	public void setIntensifierwords(Map<String, Double> intensifierwords) {
		this.intensifierWords = intensifierwords;
	}

	public Map<Integer, Integer> getSentenceKeys() {
		return sentenceKeys;
	}

	public void setSentenceKeys(Map<Integer, Integer> sentenceKeys) {
		this.sentenceKeys = sentenceKeys;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public void setSentimentListMap(Map<String, List<Double>> sentimentListMap) {
		this.sentimentListMap = sentimentListMap;
	}

	public void setWordBucket(Map<String, Double> wordBucket) {
		this.wordBucket = wordBucket;
	}

	public void setStanfordScores(List<Double> stanfordScores) {
		this.stanfordScores = stanfordScores;
	}
}

