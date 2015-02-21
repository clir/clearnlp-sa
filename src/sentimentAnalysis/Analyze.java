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
import parseCorpus.Word;

public class Analyze {
	SentimentAnalyzer sentimentAnalyzer;
	Parse parser;
	
	public Analyze() {
		parser = new Parse();
		sentimentAnalyzer = new SentimentAnalyzer(parser.getWords());
	}
	public static void main(String[] args) throws Exception {		
		String trainDepTrees = "trainDepTree.txt";
		String devDepTrees = "devDepTree.txt";
		String oneSentenceDep = "oneSentenceDepTest.txt";
		Analyze analyze = new Analyze(); 
		analyze.parser.parse();
		analyze.sentimentAnalyzer.getLabelIntensity().put("neg", 1d);
		analyze.sentimentAnalyzer.getLabelIntensity().put("cc", 1d);
		analyze.sentimentAnalyzer.getLabelIntensity().put("advmod", 1d);
		analyze.sentimentAnalyzer.getLabelIntensity().put("amod", 1d);
		analyze.sentimentAnalyzer.getLabelIntensity().put("advcl", 1d);
		analyze.sentimentAnalyzer.getLabelIntensity().put("appos", 1d);
		analyze.sentimentAnalyzer.getLabelIntensity().put("npadvmod", 1d);
		analyze.sentimentAnalyzer.readDepTree(new FileInputStream(trainDepTrees), analyze.parser.getWords().getWordBucket());
//		a.analyzer.readDepTree(new FileInputStream(devDepTrees), a.parser.getWords().getWordBucket());
//		a.analyzer.readDepTree(new FileInputStream(oneSentenceDep), a.parser.getWords().getWordBucket());
//		String trainFile = "trainFile.txt";
//		Scanner s = new Scanner(new File(trainFile));
		List<ScoreNode> scores = analyze.sentimentAnalyzer.getSentences();
		List<Double> stanfordScores = analyze.parser.getWords().getStanfordScores();
		Deque<ScoreNode> q = new ArrayDeque<>(); 
		double truePositive = 0;
		double trueNegative= 0;
		double falseNegative = 0;
		double falsePositive = 0;
		double sum = 0;
		double min = 0;
		double max = 0;
		int correct = 0;
		int incorrect = 0;
		int negative = 0;
		
		for (int i = 0; i < scores.size(); i++) {
//			System.out.println(i+1 + " " + s.nextLine());
//			System.out.println(" calculated score: " + scores.get(i).getScore());
//			System.out.println("real score: " + stanfordScores.get(i));
//			System.out.print(scores.get(i).getLemma() + " " + scores.get(i).getMaxIntensity() + " "+ scores.get(i).getScore() + "\n");
//			if (stanfordScores.get(i) > 0 && scores.get(i).getScore() > 0) {
//				truePositive++;
////				s.nextLine();
//			}
//			if (stanfordScores.get(i) > 0 && scores.get(i).getScore() < 0) {
//				falsePositive++;
////				System.out.println(i+1 + " " + s.nextLine());
////				System.out.println("calculated score: " + scores.get(i).getScore());
////				System.out.println("real score: " + stanfordScores.get(i));
////				System.out.print("head: " + scores.get(i).getLemma() + " " + scores.get(i).getMaxIntensity() + " "+ scores.get(i).getScore() + "\n");
//			}
//			
//			if (stanfordScores.get(i) < 0 && scores.get(i).getScore() < 0) {
//				trueNegative++;
////				s.nextLine();
//			}
//			if (stanfordScores.get(i) < 0 && scores.get(i).getScore() > 0) {
//				falseNegative++; 
////				s.nextLine();
//			}
			if (stanfordScores.get(i) < 0)
				negative++;
			if (stanfordScores.get(i) < 0 && scores.get(i).getScore() < 0)
				correct++;
			else if (stanfordScores.get(i) >= 0 && scores.get(i).getScore() >= 0)
				correct++;
			else {
				incorrect++;
			}
			
			List<ScoreNode> dependents = scores.get(i).getDependents();
			q.addAll(dependents);
			
			while (!q.isEmpty()) {
				ScoreNode dependent = q.poll();
				if (stanfordScores.get(i) > 0 && scores.get(i).getScore() <= 0) {
//					System.out.println(dependent.getLemma() + " " + dependent.getMaxIntensity() + " "+ dependent.getScore());
				}	
				
				for (ScoreNode d : dependent.getDependents()) {
					q.addFirst(d);
				}
			}
			sum += Math.abs(stanfordScores.get(i)-scores.get(i).getScore());
			max = Math.max(max, scores.get(i).getScore());
			min = Math.min(min, scores.get(i).getScore());
//			System.out.println();
		}
//		System.out.println((8504d-sum)/(2d*8504d));
//		double negatives = falsePositive + trueNegative;
//		double positives = truePositive + falseNegative;
//		System.out.println("Negatives" + negatives);
//		System.out.println("Positives" + positives);
		analyze.sentimentAnalyzer.printCounts();
		System.out.println("min " + min + " max " + max);
		double accuracy = 100d*correct/(correct+incorrect);
				System.out.println(("correct " + correct));
				System.out.println(("incorrect " + incorrect));

//		negatives = 3278
//		total = 8504				
//		dumb baseline = 61.45343368
//		true baseline = 66.75682032
		double recall = truePositive/(truePositive+falseNegative);
		double precision = truePositive/(truePositive+falsePositive);
		double f1 = (2*precision*recall)/(precision+recall);
		System.out.println("Accuracy: " + accuracy);
		//		System.out.println(a.parser.getWords().getSentimentListMap().get("exhilarate"));
//		System.out.println(a.parser.getWords().get
	}
}
