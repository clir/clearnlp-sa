package sentimentAnalysis;

import java.util.List;

import edu.emory.clir.clearnlp.util.MathUtils;

public class ScoreNode {
	private String lemma;
	private double score;
	private double intensity;
	private List<ScoreNode> dependents;
	private ScoreNode parent;
	
	public ScoreNode(String lemma, double score, double intensity) {
		this.lemma = lemma;
		this.score = score;
		this.intensity = intensity;
	}
	
	public double getMaxScore() {
		double maxScore = 0;
		for(int i = 0; i < dependents.size(); i++) {
			Double childScore = dependents.get(i).getScore();
			if (Math.abs(childScore) > Math.abs(maxScore)) 
				maxScore = childScore;
		}
		return maxScore;
	}
	
	public double getMaxIntensity() {
		double maxIntensity = this.intensity;
		for(int i = 0; i < dependents.size(); i++) {
			Double childIntensity = dependents.get(i).getIntensity();
			if (Math.abs(childIntensity) > Math.abs(maxIntensity)) 
				maxIntensity = childIntensity ;
		}
//		System.out.println(maxIntensity);
		return maxIntensity;
	}
	public double getSumIntensity() {
		double maxIntensity = this.intensity;
		for(int i = 0; i < dependents.size(); i++) {
			Double childIntensity = dependents.get(i).getIntensity();
			maxIntensity += childIntensity;
		}
		return maxIntensity;
	}

	public String getLemma() {
		return lemma;
	}

	public void setWordForm(String lemma) {
		this.lemma = lemma;
	}

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}

	@Override
	public String toString() {
		return "wordForm=" + lemma + ", score=" + score
				+ ", intensity=" + intensity;
	}

	public double getIntensity() {
		return intensity;
	}

	public void setIntensity(double intensity) {
		this.intensity = intensity;
	}

	public List<ScoreNode> getDependents() {
		return dependents;
	}

	public void setDependents(List<ScoreNode> dependents) {
		this.dependents = dependents;
	}
	
	public void addDependent(ScoreNode dependent) {
		this.dependents.add(dependent);
	}

	public ScoreNode getParent() {
		return parent;
	}

	public void setParent(ScoreNode parent) {
		this.parent = parent;
	}
	public int compareTo(ScoreNode o)
	{
		return MathUtils.signum(this.score - o.score);
	}
}
