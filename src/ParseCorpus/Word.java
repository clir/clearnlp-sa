package ParseCorpus;

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
	
	public Word() {
		sentimentListMap = new HashMap<>();
		wordBuckets = new ArrayList<>();
		wordBuckets.add(new HashMap<String, Double>());
		wordBuckets.add(new HashMap<String, Double>());
	}
	
	public void add(String word, int sentiment) {
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
	
	public void putInBuckets() {
		for (Entry<String, List<Integer>> entry : sentimentListMap.entrySet()) {
			double average = getAverageSentiment(entry.getValue());
			List<Double> temp = new ArrayList<>();
			for (int sentiment : entry.getValue()) {
				temp.add(Math.pow(sentiment-average,2));
			}
			double stdDev = Math.sqrt(temp.stream().mapToDouble(p->p).average().getAsDouble());
//			double stdDev = 0;
			if (average >= 0 && average <= 1) {
				Map<String, Double> bucket = wordBuckets.get(0);
				bucket.put(entry.getKey(), stdDev);
			} 
			else if (average >= 3 && average <= 4) {
				Map<String, Double> bucket = wordBuckets.get(1);
				bucket.put(entry.getKey(), stdDev);
			}
			
		}
	}
}

