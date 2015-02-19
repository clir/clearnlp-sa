package sentimentAnalysis;

import java.io.FileInputStream;
import java.util.List;

import parseCorpus.Parse;

public class Analyze {
	SentimentAnalyzer analyzer;
	Parse parser;
	
	public Analyze() {
		parser = new Parse();
		analyzer = new SentimentAnalyzer();
	}
	public static void main(String[] args) throws Exception {		
		String trainDepTrees = "trainDepTree.txt";
//		String devDepTrees = "devDepTree.txt";
		Analyze a = new Analyze(); 
		a.parser.parse();
		a.analyzer.readDepTree(new FileInputStream(trainDepTrees), a.parser.getWords().getWordBucket());
//		a.analyzer.readDepTree(new FileInputStream(devDepTrees), a.parser.getWords().getWordBucket());
//		a.analyzer.readDepTree(new FileInputStream(oneSentenceDep), a.parser.getWords().getWordBucket());
		
		List<ScoreNode> scores = a.analyzer.getSentences();
		List<Double> stanfordScores = a.parser.getWords().getStanfordScores();
		for (int i = 0; i < scores.size(); i++) {
//			System.out.println(i + " " + scores.get(i));
		}
		
//		System.out.
//			List<ScoreNode> dependents = scores.get(i).getDependents();
//			for (ScoreNode dependent : dependents) {
//				System.out.println(dependent.getWordForm() + " ");
//			}
//			System.out.println();
			//System.out.println(stanfordScores.get(i));
		
	}
}
