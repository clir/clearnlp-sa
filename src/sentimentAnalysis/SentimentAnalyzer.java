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
		// 7 Column Tree
		TSVReader reader = new TSVReader(0, 1, 2, 3, 4, 5, 6, 7);
		DEPTree tree;
		
		//Each sentence is a tree. Call analyze on the tree (sentence)
		reader.open(in);
		while ((tree = reader.next()) != null)
		{
			analyze(tree, buckets);
		}
	}
	
	public void analyze(DEPTree tree, List<Map<String, Double>> buckets)
	{
		//Get the list of root nodes for the current tree(sentence)
		//Call the recursive analyze method on the first root 
		//Add score of each sentence to the list
		List<DEPNode> roots = tree.getRoots();
		SentimentScore score = analyze(roots.get(0), buckets);
		scores.add(score);
	}
	
	private SentimentScore analyze(DEPNode head, List<Map<String, Double>> buckets)
	{
		//Get the score of the current Node passed 
		SentimentScore headScore = getScore(head, buckets);
		List<SentimentScore> childrenScores = new ArrayList<>();
		
		//For each dependent(child) analyze recursively
		for (DEPNode child : head.getDependentList())
			childrenScores.add(analyze(child, buckets));

		// Find the absolute max score in the children, add it the parentScore (headScore) then find the greatest intensifier of the children and multiply it by the headscore 
		// proceed to build up  - Johnny
		SentimentScore maxScore = new SentimentScore(0,0);
		SentimentScore maxIntensity = new SentimentScore(0,0);
		for( int i = 0; i < childrenScores.size(); i++ ) {
			SentimentScore childScore = childrenScores.get(i);
			if (Math.abs(childScore.getScore()-.5) > Math.abs(maxScore.getScore()-.5)) 
				maxScore =  childScore;
			if (Math.abs(childScore.getIntensity()-.5) > Math.abs(maxIntensity.getIntensity()-.5))
				maxIntensity = childScore;
		}
		//  head = (head + MaxScore(child))* MaxIntensity(children)
		headScore.setScore((headScore.getScore() + maxScore.getScore())*maxIntensity.getIntensity());
		
		return headScore;
	}
	
	//For the current node we find the score by getting the sentiment score from the bucket, we return the sentimentScore of the word with an intensifier
	protected SentimentScore getScore(DEPNode node, List<Map<String, Double>> buckets)
	{
		double score = 0;
		int intensifier = 1;
		if(node.getLabel().equals("neg")) intensifier = -1;
		for (int i = 0; i < buckets.size(); i++) {
			Map<String, Double> bucket = buckets.get(i);
			if (bucket.containsKey(node.getWordForm())) {
				score = bucket.get(node.getWordForm())-2;
				break;
			}
		}
		return new SentimentScore(score, intensifier);
	}
	
	protected List<SentimentScore> getScores() {
		return scores;
	}
}
