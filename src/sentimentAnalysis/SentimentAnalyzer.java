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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import parseCorpus.Word;
import edu.emory.clir.clearnlp.dependency.DEPLib;
import edu.emory.clir.clearnlp.dependency.DEPLibEn;
import edu.emory.clir.clearnlp.dependency.DEPNode;
import edu.emory.clir.clearnlp.dependency.DEPTree;
import edu.emory.clir.clearnlp.reader.TSVReader;

/**
 * called by Analyze
 */
public class SentimentAnalyzer
{
	private List<ScoreNode> sentences;
	private Map<DEPNode, ScoreNode> depScoreMap;
	private Word words;
	
	public SentimentAnalyzer(Word words) {
		sentences = new ArrayList<>();
		depScoreMap = new HashMap<>();
		this.words = words;
	}
	public void readDepTree(InputStream in, Map<String, Double> map) throws Exception
	{
		// 7 Column Tree
		TSVReader reader = new TSVReader(0, 1, 2, 3, 4, 5, 6, 7);
		DEPTree tree;
		
		//Each sentence is a tree. Call analyze on the tree (sentence)
		reader.open(in);
		while ((tree = reader.next()) != null)
		{
			analyze(tree, map);
		}
	}

	public void analyze(DEPTree tree, Map<String, Double> map)
	{
		//Get the list of root nodes for the current tree(sentence)
		//Call the recursive analyze method on the first root 
		//Add score of each sentence to the list
		List<DEPNode> roots = tree.getRoots();
		ScoreNode headNode = analyzeHead(roots.get(0), map);
		sentences.add(headNode);
	}
	
	private ScoreNode analyzeHead(DEPNode depNode, Map<String, Double> map)
	{
		//Get the ScoreNode of the current DEPNode passed 
		ScoreNode scoreNode = getNode(depNode, map);
		List<ScoreNode> childrenNodes = new ArrayList<>();
		
		//For each dependent(child) analyze recursively
		for (DEPNode child : depNode.getDependentList()) {
			ScoreNode newNode = analyzeHead(child, map);
			childrenNodes.add(newNode);
		}
		for (ScoreNode child : childrenNodes)
			child.setParent(scoreNode);
		scoreNode.setDependents(childrenNodes);
		// Find the absolute max score in the children, add it the parentScore (headScore) then find the greatest intensifier of the children and multiply it by the headscore 
		// proceed to build up  - Johnny
		//  head = (head + MaxScore(child))* MaxIntensity(children)
//		if (!sentences.contains(scoreNode))
//		scoreNode.setScore(scoreNode.getSumScore()*scoreNode.getMaxIntensity()); // 70.66086547507055
//		scoreNode.setScore(scoreNode.getSumScore()); // Accuracy: 70.61382878645344
//		scoreNode.setScore((scoreNode.getScore() + scoreNode.getMaxScore(depScoreMap, depNode))*scoreNode.getMaxIntensity()); // 68.05032925682032
		int s = scoreNode.getDependents().size();
		if (s == 0) s++;
//		scoreNode.setScore(scoreNode.getSumScore()/s*scoreNode.getMaxIntensity());  // 69.4379115710254
//		scoreNode.setScore((scoreNode.getScore() + scoreNode.getMaxScore(depScoreMap, depNode))); // 68.03857008466603
		depScoreMap.put(depNode, scoreNode);

//		depNode.getSubNodeList()
//		
		return scoreNode;
	}
	
	//For the current node we find the score by getting the sentiment score from the bucket, we return the ScoreNode of the word with an intensifier
	protected ScoreNode getNode(DEPNode node, Map<String, Double> map)
	{
		double score = 0;
		double intensity = getIntensity(node);
		if (map.containsKey(node.getLemma())) {
			score = map.get(node.getLemma());
		}
		ScoreNode sNode = new ScoreNode(node.getLemma(), score, intensity);
		return sNode;
	}
	
	private double getIntensity(DEPNode node) {
		double intensity = 1;
//		if(node.getLabel().equals("neg")) {
//			intensity = intensity*-1;
//			System.out.println("neg");
//		}
		if (words.getIntensifierWords().get(node.getLemma()) != null) {
			intensity = words.getIntensifierWords().get(node.getLemma());
		}
			
		return intensity;
	}
	protected List<ScoreNode> getSentences() {
		return sentences;
	}
	
	protected Map<DEPNode, ScoreNode> getDepScoreMap() {
		return depScoreMap;
	}
}
