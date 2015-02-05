/**
 * Copyright 2015, Emory University
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package sentimentAnalysis;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import parseCorpus.SentimentScore;
import edu.emory.clir.clearnlp.dependency.DEPNode;
import edu.emory.clir.clearnlp.dependency.DEPTree;
import edu.emory.clir.clearnlp.reader.TSVReader;

/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class SentimentAnalyzer
{
	public void read(InputStream in) throws Exception
	{
		TSVReader reader = new TSVReader(0, 1, 2, 3, 4, 5, 6, 7);
		DEPTree tree;
		
		reader.open(in);
		
		while ((tree = reader.next()) != null)
		{
			analyze(tree);
		}
	}
	
	public void analyze(DEPTree tree)
	{
		List<DEPNode> roots = tree.getRoots();
		SentimentScore score = analyze(roots.get(0));
	}
	
	private SentimentScore analyze(DEPNode head)
	{
		SentimentScore headScore = getScore(head);
		List<SentimentScore> childrenScores = new ArrayList<>();
		
		for (DEPNode child : head.getDependentList())
			childrenScores.add(analyze(child));

		SentimentScore maxScore = Collections.max(childrenScores);
		headScore.addScore(maxScore.getScore());
		
		maxScore.setIntensity(0);
		
		for (SentimentScore childScore : childrenScores)
		{
			if (Math.abs(childScore.getIntensity()) > Math.abs(maxScore.getIntensity()))
				maxScore.set(childScore);
		}
		
		headScore.setScore(headScore.getScore() * maxScore.getScore());
		return headScore;
	}
	
	protected SentimentScore getScore(DEPNode node)
	{
		return null;
	}
}
