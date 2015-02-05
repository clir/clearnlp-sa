/**
 * Copyright 2015, Emory University
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package sentimentAnalysis;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import edu.emory.clir.clearnlp.dependency.DEPNode;
import edu.emory.clir.clearnlp.dependency.DEPTree;
import edu.emory.clir.clearnlp.reader.TSVReader;

/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class SentimentAnalyzer
{
	private List<SentimentScore> scores;
	
	public SentimentAnalyzer() {
		scores = new ArrayList<>();
	}
	public void read(InputStream in, List<Map<String, Double>> buckets) throws Exception
	{
		TSVReader reader = new TSVReader(0, 1, 2, 3, 4, 5, 6, 7);
		DEPTree tree;
		
		reader.open(in);
		while ((tree = reader.next()) != null)
		{
			analyze(tree, buckets);
		}
	}
	
	public void analyze(DEPTree tree, List<Map<String, Double>> buckets)
	{
		List<DEPNode> roots = tree.getRoots();
		SentimentScore score = analyze(roots.get(0), buckets);
		scores.add(score);
	}
	
	private SentimentScore analyze(DEPNode head, List<Map<String, Double>> buckets)
	{
		SentimentScore headScore = getScore(head, buckets);
		List<SentimentScore> childrenScores = new ArrayList<>();
		
		for (DEPNode child : head.getDependentList())
			childrenScores.add(analyze(child, buckets));

		if (childrenScores.size() > 0) {
			SentimentScore maxScore = Collections.max(childrenScores);
			headScore.addScore(maxScore.getScore());
			maxScore.setIntensity(0);
		
			for (SentimentScore childScore : childrenScores)
			{
				if (Math.abs(childScore.getIntensity()) > Math.abs(maxScore.getIntensity()))
					maxScore.set(childScore);
			}
			headScore.setScore(headScore.getScore() * maxScore.getScore());
		}
		return headScore;
	}
	
	protected SentimentScore getScore(DEPNode node, List<Map<String, Double>> buckets)
	{
		double score = 2;
		for (int i = 0; i < buckets.size(); i++) {
			Map<String, Double> bucket = buckets.get(i);
			if (bucket.containsKey(node.getWordForm())) {
				score = bucket.get(node.getWordForm());
				break;
			}
		}
		return new SentimentScore(score, 1);
		
	}
	
	protected List<SentimentScore> getScores() {
		return scores;
	}
}
