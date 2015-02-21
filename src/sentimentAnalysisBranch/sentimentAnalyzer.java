package sentimentAnalysisBranch;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import parseCorpus.ScoresIntensifiers;
import edu.emory.clir.clearnlp.dependency.DEPNode;
import edu.emory.clir.clearnlp.dependency.DEPTree;
import edu.emory.clir.clearnlp.reader.TSVReader;

public class sentimentAnalyzer {

	private Map<DEPNode, ScoreNode> depScoreMap;

	public sentimentAnalyzer(){
		depScoreMap = new HashMap<>();
	}



	public List<ScoreNode> calculateScores(InputStream depTree, Map<String,Double> wordSentiments, Map<String,Double> labelIntensifiers, Map<String,Double> wordIntensifiers){

		// 7 Column Tree
		TSVReader reader = new TSVReader(0, 1, 2, 3, 4, 5, 6, 7);
		DEPTree tree;
		//Each sentence is a tree. Call analyze on the tree (sentence)
		reader.open(depTree);
		List<ScoreNode> temp = new ArrayList<>();
		while ((tree = reader.next()) != null)
		{
			List<DEPNode> roots = tree.getRoots();
			ScoreNode headNode = analyzeHead(roots.get(0), wordSentiments,labelIntensifiers,wordIntensifiers);
			temp.add(headNode);
		}
		return temp;
	}



	private ScoreNode analyzeHead(DEPNode depNode,Map<String, Double> wordSentiments,Map<String, Double> labelIntensifiers, Map<String, Double> wordIntensifiers) {
		ScoreNode scoreNode = getNode(depNode, wordSentiments,labelIntensifiers, wordIntensifiers);
		List<ScoreNode> childrenNodes = new ArrayList<>();

		for (DEPNode child : depNode.getDependentList()) {
			ScoreNode newNode = analyzeHead(child, wordSentiments,labelIntensifiers,wordIntensifiers);
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
		scoreNode.setScore(scoreNode.getSumScore()*scoreNode.getMaxIntensity()); // 70.66086547507055
		//scoreNode.setScore(scoreNode.getSumScore()); // Accuracy: 70.61382878645344

		//scoreNode.setScore((scoreNode.getScore() + scoreNode.getMaxScore(depScoreMap, depNode))*scoreNode.getMaxIntensity()); // 69.81420507996238

		//scoreNode.setScore((scoreNode.getScore() + scoreNode.getMaxScore(depScoreMap, depNode))); // 69.81420507996238
		depScoreMap.put(depNode, scoreNode);

		return scoreNode;
	}
	
	
	private ScoreNode getNode(DEPNode node, Map<String, Double> wordSentiments,Map<String,Double> labelIntensifiers, Map<String,Double> wordIntensifiers)
	{
		double score = 0;
		double intensity = getWordIntensity(node,wordIntensifiers);
		if (wordSentiments.containsKey(node.getLemma())) {
			score = wordSentiments.get(node.getLemma());
		}
		ScoreNode sNode = new ScoreNode(node.getLemma(), score, intensity, node.getLabel());
		return sNode;
	}

	

	private double getWordIntensity(DEPNode node,Map<String,Double> wordIntensifiers) {
		double intensity = 1;
		if (wordIntensifiers.get(node.getLemma()) != null) {
			intensity = wordIntensifiers.get(node.getLemma());
		}
		return intensity;
	}
	
	public Map<DEPNode, ScoreNode> getDepScoreMap() {
		return depScoreMap;
	}
	
	public void setDepScoreMap(Map<DEPNode, ScoreNode> depScoreMap) {
		this.depScoreMap = depScoreMap;
	}
}
