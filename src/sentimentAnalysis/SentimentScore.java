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

import edu.emory.clir.clearnlp.util.MathUtils;


/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class SentimentScore implements Comparable<SentimentScore>
{
	private double score;
	private double intensity;
	
	public SentimentScore(double score, double intensity)
	{
		setScore(score);
		setIntensity(intensity);
	}
	
	public double getScore()
	{
		return score;
	}
	
	public void setScore(double score)
	{
		this.score = score;
	}
	
	public void addScore(double score)
	{
		this.score += score;
	}
	
	public double getIntensity()
	{
		return intensity;
	}
	
	public void setIntensity(double intensity)
	{
		this.intensity = intensity;
	}
	
	public void set(SentimentScore score)
	{
		setScore(score.score);
		setIntensity(score.intensity);
	}

	@Override
	public int compareTo(SentimentScore o)
	{
		return MathUtils.signum(this.score - o.score);
	}
}