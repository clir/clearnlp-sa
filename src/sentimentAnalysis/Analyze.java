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
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import parseCorpus.Parse;
import parseCorpus.ScoresIntensifiers;
import edu.emory.clir.clearnlp.collection.map.IncMap1;
import edu.emory.clir.clearnlp.dependency.DEPLibEn;
import edu.emory.clir.clearnlp.dependency.DEPNode;
import edu.emory.clir.clearnlp.util.MathUtils;

public class Analyze {
	private SentimentAnalyzer sentimentAnalyzer;
	private Parse parser;
	private ScoresIntensifiers scoresIntensifiers;
	private static double accuracy = 70.61382878645345;
	private static boolean improved = false;
	private static Writer trainWriter;
	private static int right = 0;
	public Analyze() throws FileNotFoundException {
		parser = new Parse();
		sentimentAnalyzer = new SentimentAnalyzer(parser.getScoresIntensifiers());
		scoresIntensifiers = parser.getScoresIntensifiers();
		trainWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File("results.txt"))));
	}
	public static void main(String[] args) throws Exception {		
		Analyze analyze = new Analyze(); 
		analyze.parser.parse();

//		String trainDepTrees = "trainDepTree.txt";
//		String devDepTrees = "devDepTree.txt";
//		String oneSentenceDep = "oneSentenceDepTest.txt";
//
//		List<String> testLabels = new ArrayList<>();
//		testLabels.add(DEPLibEn.DEP_NEG);
//		//		testLabels.add(DEPLibEn.DEP_ADVMOD);
//		//		testLabels.add(DEPLibEn.DEP_AMOD);
//		//		testLabels.add(DEPLibEn.DEP_CC);
//
//
//		analyze.sentimentAnalyzer.readDepTree(new FileInputStream(trainDepTrees), analyze.scoresIntensifiers.getWordBucket());
//
//				List<ScoreNode> scores = analyze.sentimentAnalyzer.getSentences();
//				List<Double> stanfordScores = analyze.scoresIntensifiers.getStanfordScores();
//				List<ScoreNode> incorrectNodes = new ArrayList<>();
//				int correct = 0;
//				int correctPositive = 0;
//				int incorrectPositive = 0;
//				int correctNeutral = 0;
//				int incorrectNeutral = 0;
//				int correctNegative = 0;
//				int incorrectNegative = 0;
//				int realPositive = 0;
//				int realNeutral = 0;
//				int realNegative = 0;
//				for (int i = 0; i < scores.size(); i++) {
//					if (stanfordScores.get(i) > 0 && scores.get(i).getScore() > 0) {
//						correctPositive++;
//					}
//					else if (stanfordScores.get(i) <= 0 && scores.get(i).getScore() > 0) {
//						incorrectPositive++;
//						incorrectNodes.add(scores.get(i));
//
//					}
//					if (stanfordScores.get(i) == 0 && scores.get(i).getScore() == 0) {
//						correctNeutral++;
//					}
//					else if (stanfordScores.get(i) != 0 && scores.get(i).getScore() == 0) {
//						incorrectNeutral++;
//					}
//					if (stanfordScores.get(i) < 0 && scores.get(i).getScore() < 0) {
//						correctNegative++;
//					}
//					if (stanfordScores.get(i) >= 0 && scores.get(i).getScore() < 0) {
//						incorrectNegative++;
//					}
//					if (stanfordScores.get(i) > 0) {
//						realPositive++;
//					}
//					if (stanfordScores.get(i) == 0) {
//						realNeutral++;
//					}
//					if (stanfordScores.get(i) < 0) {
//						realNegative++;
//					}}
//					System.out.println(incorrectNodes.get(4).getScore()+ " " + incorrectNodes.get(4).getLemma() +" ");
//					analyze.scoresIntensifiers.getWordBucket().put(incorrectNodes.get(4).getLemma(), -1.0 );
//					double newScore = analyze.sentimentAnalyzer.recalculateScore(incorrectNodes.get(4), analyze.scoresIntensifiers.getWordBucket());
//					System.out.println(incorrectNodes.get(4).getScore() + " " +incorrectNodes.get(4).getLemma() +" "+ newScore);
//
////						System.out.println("correctPositive " + correctPositive + " incorrectPositive " + incorrectPositive + " correctNeutral " + correctNeutral + " incorrectNeutral " + incorrectNeutral + " correctNegative " + correctNegative + " incorrectNegative" + incorrectNegative + " realPositive " + realPositive + " realNeutral " + realNeutral + " realNegative " + realNegative);
//				//		System.out.println("Positives" + positives);
//				//		analyze.sentimentAnalyzer.printCounts();
//				//		System.out.println("min " + min + " max " + max);
//				//						System.out.println(correct);
//				//						double newAccuracy = 100d*correct/(correct+incorrect);
//				//						String cbuf = "Right: " + right + " lemma: " + lemma + " test: " + test;
//				//						System.out.println(cbuf);
//				//						if (correct > right) {
//				//							right = correct;
//				//							improved = true;
//				//							accuracy = newAccuracy;
//				//							Analyze.trainWriter.write(cbuf);
//				//							Analyze.trainWriter.flush();
//				//						}
//				//						System.out.println("Accuracy: " + accuracy + " lemma: " + depNode.getLemma() + " test: " + test);
//
//				//				System.out.println(("correct " + correct));
//				//				System.out.println(("incorrect " + incorrect));
//
//				//		negatives = 3278
//				//		total = 8504				
//				//		dumb baseline = 61.45343368
//				//		true baseline = 66.75682032
////			}
////			if (improved == false) {
////				analyze.scoresIntensifiers.getIntensifierWords().remove(lemma);
////			}
////		}
//
////				double precisionPositive  = MathUtils.divide(correctPositive, correctPositive+incorrectPositive);
////				double recallPositive  = MathUtils.divide(correctPositive, realPositive);
////
////				double precisionNeutral  = MathUtils.divide(correctNeutral, correctNeutral+incorrectNeutral);
////				double recallNeutral = MathUtils.divide(correctNeutral, realNeutral);
////				
////				double precisionNegative = MathUtils.divide(correctNegative, correctNegative+incorrectNegative);
////				double recallNegative= MathUtils.divide(correctNegative, realNegative);
////				
////				double f1Positive = (2*precisionPositive*recallPositive)/(precisionPositive+recallPositive);
////				double f1Neutral = (2*precisionNeutral*recallNeutral)/(precisionNeutral+recallNeutral);
////				double f1Negative = (2*precisionNegative*recallNegative)/(precisionNegative+recallNegative);
////				
////				double accuracyTotal = MathUtils.divide(correctPositive+correctNeutral+correctNegative, realPositive+realNeutral+realNegative);
////				System.out.printf("%4.2f (%d/%d)", MathUtils.divide(correctPositive+correctNeutral+correctNegative, realPositive+realNeutral+realNegative), correctPositive+correctNeutral+correctNegative, realPositive+realNeutral+realNegative);
////				System.out.println(precisionPositive);
////				System.out.println(recallPositive);
////				System.out.println(precisionNeutral);
////				System.out.println(recallNeutral);
////				System.out.println(precisionNegative);
////				System.out.println(recallNegative);
////				System.out.println("f1 Positive " + f1Positive + "f1 Neutral " + f1Neutral + "f1 Negative " + f1Negative);
////				System.out.println("accuracy " + accuracyTotal);
////				int negcount =0;
////				int neucount =0;
////				int poscount =0;
////				for (Double score : analyze.scoresIntensifiers.getWordBucket().values()) {
////					if (score < 0)
////						negcount++;
////					if (score == 0)
////						neucount++;
////					if (score > 0)
////						poscount++;
////				}
//
//	}
}
}

