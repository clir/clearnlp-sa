package sentimentAnalysis;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import edu.emory.clir.clearnlp.dependency.DEPLibEn;
import edu.emory.clir.clearnlp.dependency.DEPNode;
import parseCorpus.Parse;
import parseCorpus.ScoresIntensifiers;

public class Analyze2 {
	private SentimentAnalyzer sentimentAnalyzer;
	private Parse parser;
	private ScoresIntensifiers scoresIntensifiers;
	private static double accuracy = 0;
	private static boolean improved = false;
	public Analyze2() throws FileNotFoundException {
		parser = new Parse();
		sentimentAnalyzer = new SentimentAnalyzer(parser.getScoresIntensifiers());
		scoresIntensifiers = parser.getScoresIntensifiers();
	}
	public static void main(String[] args) throws Exception {		
		Analyze2 analyze = new Analyze2(); 
		analyze.parser.parse();

		String trainDepTrees = "trainDepTree.txt";
		String devDepTrees = "devDepTree.txt";
		String oneSentenceDep = "oneSentenceDepTest.txt";

		List<String> testLabels = new ArrayList<>();
		testLabels.add(DEPLibEn.DEP_NEG);
		testLabels.add(DEPLibEn.DEP_ADVMOD);
		testLabels.add(DEPLibEn.DEP_AMOD);
		testLabels.add(DEPLibEn.DEP_CC);
		analyze.sentimentAnalyzer.readDepTree(new FileInputStream(trainDepTrees), analyze.scoresIntensifiers.getWordBucket());

		for (String label : testLabels) {
			analyze.sentimentAnalyzer.setLabel(label);
		
//				analyze.sentimentAnalyzer.getLabelIntensity().put("neg", 2d);
		//		analyze.sentimentAnalyzer.getLabelIntensity().put("cc", 1d);
		//		analyze.sentimentAnalyzer.getLabelIntensity().put("advmod", 1d);
		//		analyze.sentimentAnalyzer.getLabelIntensity().put("amod", 1d);
		//		analyze.sentimentAnalyzer.getLabelIntensity().put("advcl", 1d);
		//		analyze.sentimentAnalyzer.getLabelIntensity().put("appos", 1d);
		//		analyze.sentimentAnalyzer.getLabelIntensity().put("npadvmod", 1d);



		//		analyze.parser.getWords().getIntensifierWords().put("not", -1.1d);
		//		analyze.parser.getWords().getIntensifierWords().put("but", 0d);
		//		analyze.parser.getWords().getIntensifierWords().put("neither", -5d);
		//		analyze.parser.getWords().getIntensifierWords().put("yet", 3.5);
		//		analyze.parser.getWords().getIntensifierWords().put("very", 1.5);
		//		analyze.parser.getWords().getIntensifierWords().put("really", 1.5);
		//		analyze.parser.getWords().getIntensifierWords().put("super", 1d);
		//		analyze.parser.getWords().getIntensifierWords().put("extremely", 3.5);

		//		a.analyzer.readDepTree(new FileInputStream(devDepTrees), a.parser.getWords().getWordBucket());
		//		a.analyzer.readDepTree(new FileInputStream(oneSentenceDep), a.parser.getWords().getWordBucket());
		String trainFile = "trainFile.txt";
		Scanner s = new Scanner(new File(trainFile));
		List<ScoreNode> scores = analyze.sentimentAnalyzer.getSentences();
		List<Double> stanfordScores = analyze.scoresIntensifiers.getStanfordScores();
		Deque<ScoreNode> q = new ArrayDeque<>(); 

		double min = 0;
		double max = 0;
		int correct = 0;
		int incorrect = 0;

		for (int i = 0; i < scores.size(); i++) {
			//			System.out.println(i+1 + " " + s.nextLine());
			//			System.out.println(" calculated score: " + scores.get(i).getScore());
			//			System.out.println("real score: " + stanfordScores.get(i));
			//			System.out.print(scores.get(i).getLemma() + " " + scores.get(i).getMaxIntensity() + " "+ scores.get(i).getScore() + "\n");
			if (stanfordScores.get(i) > 0 && scores.get(i).getScore() > 0) {
				s.nextLine();
			}
			if (stanfordScores.get(i) > 0 && scores.get(i).getScore() <= 0) {
//								System.out.println(i+1 + " " + s.nextLine());
//								System.out.println("calculated score: " + scores.get(i).getScore());
//								System.out.println("real score: " + stanfordScores.get(i));
//								System.out.print("head: " + scores.get(i).getLemma() + " " + scores.get(i).getMaxIntensity() + " "+ scores.get(i).getScore() + "\n");
			}
			//			
			if (stanfordScores.get(i) <= 0 && scores.get(i).getScore() > 0) {
				s.nextLine();
			}
			if (stanfordScores.get(i) <= 0 && scores.get(i).getScore() <= 0) {
				s.nextLine();
			}

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
//			System.out.println();
//			max = Math.max(max, scores.get(i).getScore());
//			min = Math.min(min, scores.get(i).getScore());
//			System.out.println();
		}
		//		System.out.println((8504d-sum)/(2d*8504d));
		//		double negatives = falsePositive + trueNegative;
		//		double positives = truePositive + falseNegative;
		//		System.out.println("Negatives" + negatives);
		//		System.out.println("Positives" + positives);
		//		analyze.sentimentAnalyzer.printCounts();
		//		System.out.println("min " + min + " max " + max);
		//						System.out.println(correct);
		accuracy = 100d*correct/(correct+incorrect);

		System.out.println("Accuracy: " + accuracy);

		//				System.out.println(("correct " + correct));
		//				System.out.println(("incorrect " + incorrect));

		//		negatives = 3278
		//		total = 8504				
		//		dumb baseline = 61.45343368
		//		true baseline = 66.75682032
		//						System.out.println(analyze.parser.getWords().getSentimentListMap().get("leave"));

	}
	}



}

