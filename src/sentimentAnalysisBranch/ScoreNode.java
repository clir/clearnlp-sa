package sentimentAnalysisBranch;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.emory.clir.clearnlp.dependency.DEPLibEn;
import edu.emory.clir.clearnlp.dependency.DEPNode;
import edu.emory.clir.clearnlp.util.MathUtils;

public class ScoreNode {
	private String lemma;
	private double score;
	private double intensity;
	private List<ScoreNode> dependents;
	private ScoreNode parent;
	private String label;
	private Map<String, Integer> labelCounts;
	private int index;
	
	public ScoreNode(String lemma, double score, double intensity, String label, int index) {
		this.lemma = lemma;
		this.score = score;
		this.intensity = intensity;
		this.label = label;
		this.labelCounts = new HashMap<>();
		this.index = index;
	}

	public ScoreNode(ScoreNode node) {
		this.dependents = node.getDependents();
		this.lemma = node.getLemma();
		this.score = node.getScore();
		this.intensity = node.getIntensity();
		this.parent = node.getParent();
	}

	public double getMaxScore(Map<DEPNode, ScoreNode> depScoreMap, DEPNode depNode) {
		double maxScore = 0;
		double childScore = 0;
		for(int i = 0; i < dependents.size(); i++) {
			childScore = dependents.get(i).getScore();
			
			if (Math.abs(childScore) > Math.abs(maxScore)) {
				maxScore = childScore;
			}
		}
		
		// conjunctions
		List<DEPNode> dcc = depNode.getDependentListByLabel(DEPLibEn.DEP_CC);
		if (!dcc.isEmpty()) {
			List<DEPNode> dconj = depNode.getDependentListByLabel(DEPLibEn.DEP_CONJ);
			if (depScoreMap.get(dcc.get(0)).getIntensity() == 1 && !dcc.get(0).getWordForm().equals("and") && !dcc.get(0).getWordForm().equals("or"))
				//System.out.println(depScoreMap.get(dcc.get(0)));
			if (!dconj.isEmpty()) {
				childScore = depScoreMap.get(dconj.get(0)).getScore() * depScoreMap.get(dcc.get(0)).getIntensity();
			}
		}
		if (Math.abs(childScore) > Math.abs(maxScore)) {
			maxScore = childScore;
		}
		
		return maxScore;
	}
	
	
	public double getMaxIntensity() {
		double maxIntensity = 1;
		for(int i = 0; i < dependents.size(); i++) {
			Double childIntensity = dependents.get(i).getIntensity();
			if (Math.abs(childIntensity) > Math.abs(maxIntensity)) 
				maxIntensity = childIntensity ;
		}
		return maxIntensity;
	}
	public IntensityID getMaxIntensityID() { 
		IntensityID iid = null;
		double maxIntensity = 1;
		for(int i = 0; i < dependents.size(); i++) {
			Double childIntensity = dependents.get(i).getIntensity();
			if (Math.abs(childIntensity) > Math.abs(maxIntensity)) { 
				maxIntensity = childIntensity;
				iid = new IntensityID(maxIntensity, dependents.get(i).getIndex());
			}
		}
		return iid;
	}
	public double getSumIntensity() {
		double maxIntensity = this.intensity;
		for(int i = 0; i < dependents.size(); i++) {
			Double childIntensity = dependents.get(i).getIntensity();
			maxIntensity += childIntensity;
		}
		return maxIntensity;
	}

	public double getSumScore() {
		double sumScore = this.score;
		for(int i = 0; i < dependents.size(); i++) {
			Double childScore = dependents.get(i).getScore();
			sumScore += childScore;
		}
		return sumScore;
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

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public void setLemma(String lemma) {
		this.lemma = lemma;
	}

	public Map<String, Integer> getLabelCounts() {
		return labelCounts;
	}

	public void setLabelCounts(Map<String, Integer> labelCounts) {
		this.labelCounts = labelCounts;
	}
	
	public int getIndex() {
		return index;
	}
	
	public void setIndex(int index) {
		this.index = index;
	}
}
