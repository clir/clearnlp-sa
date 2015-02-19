package sentimentAnalysis;

import java.util.List;

import edu.emory.clir.clearnlp.util.MathUtils;

public class ScoreNode {
	private String wordForm;
	private double score;
	private double intensity;
	private List<ScoreNode> dependents;
	private ScoreNode parent;
	
	public ScoreNode(String wordForm, double score, double intensity) {
		this.wordForm = wordForm;
		this.score = score;
		this.intensity = intensity;
	}
	
	public double getMaxScore() {
		double maxScore = 0;
		for(int i = 0; i < dependents.size(); i++) {
			Double childScore = dependents.get(i).getScore();
			if (Math.abs(childScore-.5) > Math.abs(maxScore-.5)) 
				maxScore = childScore;
		}
		return maxScore;
	}
	
	public double getMaxIntensity() {
		double maxIntensity =1;
		for(int i = 0; i < dependents.size(); i++) {
			Double childIntensity = dependents.get(i).getScore();
			if (Math.abs(childIntensity) > Math.abs(maxIntensity)) 
				maxIntensity = childIntensity ;
		}
		return maxIntensity;
	}

	public String getWordForm() {
		return wordForm;
	}

	public void setWordForm(String wordForm) {
		this.wordForm = wordForm;
	}

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}

	@Override
	public String toString() {
		return "ScoreNode [wordForm=" + wordForm + ", score=" + score
				+ ", intensity=" + intensity + "]";
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
