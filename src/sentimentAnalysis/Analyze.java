package sentimentAnalysis;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Scanner;

import edu.emory.clir.clearnlp.dependency.DEPLibEn;
import edu.emory.clir.clearnlp.dependency.DEPNode;
import parseCorpus.Parse;
import parseCorpus.Word;

public class Analyze {
	SentimentAnalyzer sentimentAnalyzer;
	Parse parser;
	static double accuracy;

	public Analyze() {
		parser = new Parse();
		sentimentAnalyzer = new SentimentAnalyzer(parser.getWords());
	}
	public static void main(String[] args) throws Exception {		
		Analyze analyze = new Analyze(); 
		analyze.parser.parse();

		String trainDepTrees = "trainDepTree.txt";
		String devDepTrees = "devDepTree.txt";
		String oneSentenceDep = "oneSentenceDepTest.txt";

		List<String> testLabels = new ArrayList<>();
		testLabels.add(DEPLibEn.DEP_NEG);
		testLabels.add(DEPLibEn.DEP_ADVMOD);
		testLabels.add(DEPLibEn.DEP_AMOD);
		testLabels.add(DEPLibEn.DEP_CC);
//		analyze.sentimentAnalyzer.getLabelIntensity().put("neg", 1d);
//		analyze.sentimentAnalyzer.getLabelIntensity().put("cc", 1d);
//		analyze.sentimentAnalyzer.getLabelIntensity().put("advmod", 1d);
//		analyze.sentimentAnalyzer.getLabelIntensity().put("amod", 1d);
//		analyze.sentimentAnalyzer.getLabelIntensity().put("advcl", 1d);
//		analyze.sentimentAnalyzer.getLabelIntensity().put("appos", 1d);
//		analyze.sentimentAnalyzer.getLabelIntensity().put("npadvmod", 1d);
		System.out.println("part1");
		analyze.sentimentAnalyzer.readDepTree(new FileInputStream(trainDepTrees), analyze.parser.getWords().getWordBucket());
		for (DEPNode depNode : analyze.sentimentAnalyzer.getDepScoreMap().keySet()) {
			for (String label : testLabels) {
				if (depNode.getLabel().equals(label)) {
					for (double test = -5; test <= 5 ; test+=.2) {
						analyze.sentimentAnalyzer.readDepTree(new FileInputStream(trainDepTrees), analyze.parser.getWords().getWordBucket());
						analyze.parser.getWords().getIntensifierWords().put(depNode.getLemma(), test);

//						System.out.println("part1");

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
						List<Double> stanfordScores = analyze.parser.getWords().getStanfordScores();
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
								//				System.out.println(i+1 + " " + s.nextLine());
								//				System.out.println("calculated score: " + scores.get(i).getScore());
								//				System.out.println("real score: " + stanfordScores.get(i));
								//				System.out.print("head: " + scores.get(i).getLemma() + " " + scores.get(i).getMaxIntensity() + " "+ scores.get(i).getScore() + "\n");
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
//									System.out.println(dependent.getLemma() + " " + dependent.getMaxIntensity() + " "+ dependent.getScore());
								}

								for (ScoreNode d : dependent.getDependents()) {
									q.addFirst(d);
								}
							}
//							System.out.println();
							max = Math.max(max, scores.get(i).getScore());
							min = Math.min(min, scores.get(i).getScore());
							//			System.out.println();
						}
						//		System.out.println((8504d-sum)/(2d*8504d));
						//		double negatives = falsePositive + trueNegative;
						//		double positives = truePositive + falseNegative;
						//		System.out.println("Negatives" + negatives);
						//		System.out.println("Positives" + positives);
						//		analyze.sentimentAnalyzer.printCounts();
						//		System.out.println("min " + min + " max " + max);
						double newAccuracy = 100d*correct/(correct+incorrect);
						if (newAccuracy > accuracy) {
							accuracy = newAccuracy;
							System.out.println("Accuracy: " + accuracy + " lemma: " + depNode.getLemma() + " test: " + test);
						}

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
		}
	}
}
