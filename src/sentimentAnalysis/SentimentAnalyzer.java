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
import java.util.Map.Entry;

import parseCorpus.ScoresIntensifiers;
import edu.emory.clir.clearnlp.dependency.DEPLib;
import edu.emory.clir.clearnlp.dependency.DEPLibEn;
import edu.emory.clir.clearnlp.dependency.DEPNode;
import edu.emory.clir.clearnlp.dependency.DEPTree;
import edu.emory.clir.clearnlp.reader.TSVReader;
import edu.emory.clir.clearnlp.dependency.DEPLibEn;


/**
 * called by Analyze
 */
public class SentimentAnalyzer
{
	private List<ScoreNode> sentences;
	private Map<DEPNode, ScoreNode> depScoreMap;
	private Map <String, Double> labelIntensity;
	private int count1;
	private int count2;
	private int count3;
	private int count4;
	private int count5;
	private int count6;
	private int count7;
	private ScoresIntensifiers words;
	private String label;
	
	
	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public SentimentAnalyzer(ScoresIntensifiers words) {
		sentences = new ArrayList<>();
		depScoreMap = new HashMap<>();
		labelIntensity = new HashMap<>();
		this.words = words;
		count1 = 0;
		count2 = 0;
		count3 = 0;
		count4 = 0;
		count5 = 0;
		count6 = 0;
		count7 = 0;
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
		for (ScoreNode child : childrenNodes) {
			child.setParent(scoreNode);
			scoreNode.getLabelCounts().compute(child.getLabel(), (k,v) -> v != null ? v++ : 1);  
			for (Entry<String, Integer> entry : child.getLabelCounts().entrySet()) {
				scoreNode.getLabelCounts().compute(entry.getKey(), (k,v) -> v != null ? v+= entry.getValue() : 1);  
			}
		}
		scoreNode.setDependents(childrenNodes);
		// Find the absolute max score in the children, add it the parentScore (headScore) then find the greatest intensifier of the children and multiply it by the headscore 
		// proceed to build up  - Johnny
		
		if (scoreNode.getLabel().equals(label))
			scoreNode.setScore(scoreNode.getSumScore()*3);
////		

//		if (scoreNode.getLabel().equals(DEPLibEn.DEP_ADVMOD)) // 1. 70.64910630291628 2. 70.64910630291628 
//			scoreNode.setScore(scoreNode.getSumScore()*2);
//		if (scoreNode.getLabel().equals(DEPLibEn.DEP_ADVCL)) // 1. 70.64910630291628 2. 70.64910630291628
//			scoreNode.setScore(scoreNode.getSumScore()*2); // Accuracy: 2. 70.61382878645344

		scoreNode.setScore(scoreNode.getSumScore()*scoreNode.getMaxIntensity()); // 1. 70.61382878645344
//		scoreNode.setScore((scoreNode.getScore() + scoreNode.getMaxScore(depScoreMap, depNode))*scoreNode.getMaxIntensity()); // 68.05032925682032

//		scoreNode.setScore((scoreNode.getScore() + scoreNode.getMaxScore(depScoreMap, depNode))); // 68.03857008466603
		
		depScoreMap.put(depNode, scoreNode);

//		depNode.getSubNodeList()
//		
		return scoreNode;
	}
	
	//For the current node we find the score by getting the sentiment score from the bucket, we return the ScoreNode of the word with an intensifier
	private ScoreNode getNode(DEPNode node, Map<String, Double> map)
	{
		double score = 0;
		double intensity = getIntensity(node);
		if (map.containsKey(node.getLemma())) {
			score = map.get(node.getLemma());
		}
		ScoreNode sNode = new ScoreNode(node.getLemma(), score, intensity, node.getLabel());
		return sNode;
	}
	
	private double getIntensity(DEPNode node) {
		double intensity = 1;
//		if(node.getLabel().equals("neg")) {
//			intensity = labelIntensity.get("neg");
//			count1++;
//		}
//		if(node.getLabel().equals("cc")) {
//			intensity = labelIntensity.get("cc");
//			count2++;
//		}
//		if(node.getLabel().equals("advmod")) {
//			intensity = labelIntensity.get("advmod");
//			count3++;
//		}
//		if(node.getLabel().equals("amod")) {
//			intensity = labelIntensity.get("amod");
//			count4++;
//		}
//		if(node.getLabel().equals("advcl")) {
//			intensity = labelIntensity.get("advcl");
//			count5++;
//		}
//		if(node.getLabel().equals("appos")) {
//			intensity = labelIntensity.get("appos");
//			count6++;
//		}
//		if(node.getLabel().equals("npadvmod")) {
//			intensity = labelIntensity.get("npadvmod");
//			count7++;
//		}
		if (words.getIntensifierWords().get(node.getLemma()) != null) {
			intensity = words.getIntensifierWords().get(node.getLemma());
		}
		return intensity;
	}
	public List<ScoreNode> getSentences() {
		return sentences;
	}
	
	public Map<DEPNode, ScoreNode> getDepScoreMap() {
		return depScoreMap;
	}
	public Map<String, Double> getLabelIntensity() {
		return labelIntensity;
	}
	public void setLabelIntensity(Map<String, Double> labelIntensity) {
		this.labelIntensity = labelIntensity;
	}
	public ScoresIntensifiers getWords() {
		return words;
	}
	public void setWords(ScoresIntensifiers words) {
		this.words = words;
	}
	public void setSentences(List<ScoreNode> sentences) {
		this.sentences = sentences;
	}
	public void setDepScoreMap(Map<DEPNode, ScoreNode> depScoreMap) {
		this.depScoreMap = depScoreMap;
	}
	public void printCounts() {
		System.out.println("neg: " + count1 + " cc: " + count2 + " advmod: " +  count3 + " amod: " + count4 + " advcl " + count5 + " appos " + count6 + " npadvmod " + count7);
	}
}
