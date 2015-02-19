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
	private List<ScoreNode> sentences;
	
	public SentimentAnalyzer() {
		sentences = new ArrayList<>();
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
	
	private ScoreNode analyzeHead(DEPNode head, Map<String, Double> map)
	{
		//Get the ScoreNode of the current DEPNode passed 
		ScoreNode headNode = getNode(head, map);
		List<ScoreNode> childrenNodes = new ArrayList<>();
		
		//For each dependent(child) analyze recursively
		for (DEPNode child : head.getDependentList())
			childrenNodes.add(analyzeHead(child, map));
		for (ScoreNode child : childrenNodes)
			child.setParent(headNode);
		headNode.setDependents(childrenNodes);
		// Find the absolute max score in the children, add it the parentScore (headScore) then find the greatest intensifier of the children and multiply it by the headscore 
		// proceed to build up  - Johnny
		//  head = (head + MaxScore(child))* MaxIntensity(children)
		headNode.setScore(headNode.getScore() + headNode.getMaxScore()*headNode.getMaxIntensity());
		
		return headNode;
	}
	
	//For the current node we find the score by getting the sentiment score from the bucket, we return the ScoreNode of the word with an intensifier
	protected ScoreNode getNode(DEPNode node, Map<String, Double> map)
	{
		double score = 0;
		double intensity = 1;
		if(node.getLabel().equals("neg")) {
			intensity = intensity*-1;
		}
		if (map.containsKey(node.getWordForm())) {
			score = map.get(node.getWordForm());
		}
		return new ScoreNode(node.getWordForm(), score, intensity);
	}
	
	protected List<ScoreNode> getSentences() {
		return sentences;
	}
}
