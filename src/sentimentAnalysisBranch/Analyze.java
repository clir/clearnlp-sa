package sentimentAnalysisBranch;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.Scanner;


public class Analyze {

	private static sentimentAnalyzer sAnalyzer;
	private static sentimentDS sentDS;
	private static double accuracy = 70.61382878645345;
	private static boolean improved = false;
	private static Writer trainWriter;
	private static int totalCorrect = 6005;
	private static String trainDepTrees = "trainDepTree.txt";
	private static String devDepTrees = "devDepTree.txt";
	private static String oneSentenceDep = "oneSentenceDepTest.txt";
	private static String trainFile = "trainFile.txt";

	public Analyze(){
		sAnalyzer = new sentimentAnalyzer();
		sentDS = new sentimentDS();
	}
	public static void main(String[] args) throws Exception{
		//Construct Analyze and read from all files and store in sentimentDS
		Analyze analyze = new Analyze();
		analyze.getSentDS().readIntensifiers();
		analyze.getSentDS().readStanfordResults();
		analyze.getSentDS().readWordSentiments();

		List<ScoreNode> temp;
		temp = analyze.getAnalyzer().calculateScores(new FileInputStream(trainDepTrees),analyze.getSentDS().getWordSentiments(),analyze.getSentDS().getLabelIntensifier(),analyze.getSentDS().getIntensifiers());		
		analyze.printResults(new File(trainFile), temp);



	}



	public boolean printResults(File in, List<ScoreNode> results) throws FileNotFoundException{
//		Scanner read = new Scanner(in);
		Deque<ScoreNode> depQueue = new ArrayDeque<>();
		List<Double> stanfordResults = getSentDS().getStanfordResults();
		//double min = 0;
		//double max = 0;
		int correct = 0;
		int incorrect = 0;
		boolean check = false;
		for( int i = 0; i<results.size(); i++){
			if (stanfordResults.get(i) > 0 && results.get(i).getScore() <= 0) {
			check = true;
			System.out.println();
			System.out.println(i+1);
			System.out.print("Head: " + results.get(i).getLemma() + " Final Intensity " + results.get(i).getMaxIntensity() + " Calculated Score "+ results.get(i).getScore() + " Stanford core: " + stanfordResults.get(i)+ "\n");
			}
			
			if (stanfordResults.get(i) < 0 && results.get(i).getScore() < 0)
				correct++;
			else if (stanfordResults.get(i) >= 0 && results.get(i).getScore() >= 0)
				correct++;
			else {
			incorrect++;
			}
			if(check){
			System.out.println("Root: " + results.get(i).getLemma()+ " Intensity " + getSentDS().getIntensifiers().get(results.get(i)) +" Sentiment Score: "  +getSentDS().getWordSentiments().get(results.get(i).getLemma()));
			List<ScoreNode> dependents = results.get(i).getDependents();
			depQueue.addAll(dependents);

			while (!depQueue.isEmpty()) {
				ScoreNode dependent = depQueue.poll();
				System.out.println("Word: "+dependent.getLemma() + " Intensity: " + dependent.getMaxIntensity() + " Sentiment Score: "+ dependent.getScore());

				for (ScoreNode dNode : dependent.getDependents()) {
					depQueue.addFirst(dNode);
				}
			}
			
			check = false;
		}
		}
		
		//System.out.println();
		//max = Math.max(max, results.get(i).getScore());
		//min = Math.min(min, results.get(i).getScore());
		System.out.println();
		//System.out.println((8504d-sum)/(2d*8504d));
		//double negatives = falsePositive + trueNegative;
		//double positives = truePositive + falseNegative;
		//System.out.println("Negatives" + negatives);
		//System.out.println("Positives" + positives);
		//System.out.println("min " + min + " max " + max);
		//System.out.println(correct);
		//System.out.println("Accuracy: " + accuracy + " lemma: " + depNode.getLemma() + " test: " + test);
		System.out.println(("correct " + correct));
		System.out.println(("incorrect " + incorrect));
		//negatives = 3278
		//total = 8504				
		//dumb baseline = 61.45343368
		//true baseline = 66.75682032
		double newAccuracy = 100d*correct/(correct+incorrect);
		System.out.println("Accuracy:" +newAccuracy);
		if (correct > totalCorrect) {
		totalCorrect = correct;
		improved = true;
		//accuracy = newAccuracy;
		}
		System.out.println("Finished");
		return improved;
		
		}		


























	public sentimentDS getSentDS(){
		return this.sentDS;
	}
	public sentimentAnalyzer getAnalyzer(){
		return this.sAnalyzer;
	}

}
