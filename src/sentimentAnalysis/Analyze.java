package sentimentAnalysis;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

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
		String trainFile = "trainFile.txt";
		Scanner s = new Scanner(new File(trainFile));
		List<ScoreNode> scores = a.analyzer.getSentences();
		List<Double> stanfordScores = a.parser.getWords().getStanfordScores();
		for (int i = 0; i < scores.size(); i++) {
			System.out.println(s.nextLine());
			System.out.println(i+1 + " calculated score: " + scores.get(i));
			System.out.println("real score: " + stanfordScores.get(i));
//		}
		
			List<ScoreNode> dependents = scores.get(i).getDependents();
			for (ScoreNode dependent : dependents) {
				System.out.print(dependent.getWordForm() + " " + dependent.getMaxIntensity() + " "+ dependent.getScore() + "\n");
			}
			System.out.println();
//			Map<String, Double> wordBucket = a.parser.getWords().getWordBucket();
			
		}
	}
}
