package parseCorpus;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Word implements Serializable {
	private static final long serialVersionUID = 844801659782414882L;
	private Map<String,List<Integer>> sentimentListMap;
	private List<Map<String,Double>> wordBuckets;
	private Map<Integer,Double> rawScores;
	private Map<String,Double> sentimentExpression;
	private List<Double> stanfordScores;
	private Map<String,Double> intensifierwords;
	
	public Word() {
		sentimentListMap = new HashMap<>();
		wordBuckets = new ArrayList<>();
		wordBuckets.add(new HashMap<String, Double>());
		wordBuckets.add(new HashMap<String, Double>());
		rawScores = new HashMap<>();
		sentimentExpression = new HashMap<>();
		stanfordScores = new ArrayList<>();
		intensifierwords = new HashMap<>();
	}
	
	public void addIntensifierWords(String word, double intensity){
		intensifierwords.put(word, intensity);
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
	
	public void add(String word, int sentiment) {
		sentiment /= 4;
		if(!sentimentListMap.containsKey(word))
		{
			List<Integer> tmp = new ArrayList<>();
			tmp.add(sentiment);
			sentimentListMap.put(word, tmp);
		}
		else {
			List<Integer> answer = sentimentListMap.get(word);
			answer.add(sentiment);
			sentimentListMap.put(word, answer);
		}
	}
	
	public boolean isWord(String word){
		if(sentimentListMap.containsKey(word)){
			return true;
		}
		return false;
	}
	
	public double getAverageSentiment(List<Integer> sentiments) {
		return sentiments.stream().mapToDouble(p->p).average().getAsDouble();
	}
	
	public Map<String, List<Integer>> getSentimentListMap() {
		return sentimentListMap;
	}
	
	public List<Map<String,Double>> getWordBucket() {
		return wordBuckets;
	}
	
	//For every list of sentiments we find the stdDev and put it in bucket
	public void putInBuckets() {
		for (Entry<String, List<Integer>> entry : sentimentListMap.entrySet()) {
			double average = getAverageSentiment(entry.getValue());
			List<Double> temp = new ArrayList<>();
			for (int sentiment : entry.getValue()) {
				temp.add(Math.pow(sentiment-average,2));
			}
			
			double stdDev = Math.sqrt(temp.stream().mapToDouble(p->p).average().getAsDouble());
			if (average >= 0 && average <= .25) {
				Map<String, Double> bucket = wordBuckets.get(0);
				bucket.put(entry.getKey(), stdDev);
			}
			else if (average >= .75 && average <= 1) {
				Map<String, Double> bucket = wordBuckets.get(1);
				bucket.put(entry.getKey(), stdDev);
			}
			
		}
	}
}

