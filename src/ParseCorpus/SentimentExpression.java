package ParseCorpus;

import java.util.HashMap;
import java.util.Map;

public class SentimentExpression {
	private Map<Integer,Double> rawScores = new HashMap<Integer,Double>();
	private Map<String,Double> sentimentExpression = new HashMap<String,Double>();
	
	public void addRawScore(int index, double average){
		rawScores.put(index, average);
	}
	public void add(int Index, String expression){
		double average = rawScores.get(Index);
		sentimentExpression.put(expression,average);
	}
}
