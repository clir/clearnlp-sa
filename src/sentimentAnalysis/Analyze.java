package sentimentAnalysis;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Scanner;

import parseCorpus.Parse;

public class Analyze {
	SentimentAnalyzer analyzer;
	Parse parser;
	
	public Analyze() {
		parser = new Parse();
		analyzer = new SentimentAnalyzer(parser.getWords());
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
		Deque<ScoreNode> q = new ArrayDeque<>(); 
		for (int i = 0; i < scores.size(); i++) {
			System.out.println(i+1 + " " + s.nextLine());
			System.out.println(" calculated score: " + scores.get(i).getScore());
			
			System.out.println("real score: " + stanfordScores.get(i));
			System.out.print(scores.get(i).getWordForm() + " " + scores.get(i).getMaxIntensity() + " "+ scores.get(i).getScore() + "\n");
			List<ScoreNode> dependents = scores.get(i).getDependents();
			q.addAll(dependents);
			
			while (!q.isEmpty()) {
				ScoreNode dependent = q.poll();
				
				System.out.print(dependent.getWordForm() + " " + dependent.getMaxIntensity() + " "+ dependent.getScore() + "\n");
				for (ScoreNode d : dependent.getDependents()) {
					q.addFirst(d);
				}
			}
			System.out.println();
		}
	}
}
