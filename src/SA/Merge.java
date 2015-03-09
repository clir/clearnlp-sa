package SA;

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

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringJoiner;
import java.util.stream.Collectors;

import edu.emory.clir.clearnlp.collection.pair.ObjectDoublePair;
import edu.emory.clir.clearnlp.component.mode.sentiment.TSentiment;
import edu.emory.clir.clearnlp.dependency.DEPLib;
import edu.emory.clir.clearnlp.dependency.DEPNode;
import edu.emory.clir.clearnlp.dependency.DEPTree;
import edu.emory.clir.clearnlp.reader.TSVReader;
import edu.emory.clir.clearnlp.util.IOUtils;
import edu.emory.clir.clearnlp.util.MathUtils;
import edu.emory.clir.clearnlp.util.Splitter;
import edu.emory.clir.clearnlp.util.StringUtils;
import edu.emory.clir.clearnlp.util.constant.PatternConst;
import edu.emory.clir.clearnlp.util.constant.StringConst;

/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class Merge
{
	private Map<String,TSentiment> m_sentiments;
	private Map<String,String[]>   m_tokens;
	
	public Merge()
	{
		m_sentiments = new HashMap<>();
		m_tokens = new HashMap<>();  
	}
	
	public void initMaps(InputStream in) throws Exception
	{
		BufferedReader fin = IOUtils.createBufferedReader(in);
		TSentiment sentiment;
		String line, phrase;
		String[] t;
		
		while ((line = fin.readLine()) != null)
		{
			t = Splitter.splitTabs(line);
			phrase    = StringUtils.toLowerCase(t[2].trim());
			sentiment = convertSentiment(t[3].trim());
			m_sentiments.put(phrase, sentiment);
			m_tokens.put(phrase, Splitter.split(phrase, PatternConst.WHITESPACES));
		}
		
		fin.close();
	}
	
	private TSentiment convertSentiment(String s)
	{
		switch (s)
		{
		case "0": return TSentiment.STRONG_NEGATIVE;
		case "1": return TSentiment.WEAK_NEGATIVE;
		case "2": return TSentiment.NEUTRAL;
		case "3": return TSentiment.WEAK_POSITIVE;
		case "4": return TSentiment.STRONG_POSITIVE;
		}
		
		throw new IllegalArgumentException(s);
	}
	
	public void printDEPTrees(InputStream in, OutputStream out) throws Exception
	{
		PrintStream fout = IOUtils.createBufferedPrintStream(out);
		TSVReader fin = new TSVReader(0, 1, 2, 3, 4, 5, 6, 7);
		DEPTree tree;
		fin.open(in);
		
		for (int i=0; (tree = fin.next()) != null; i++)
		{
			merge(tree);
			fout.println(tree.toStringSRL()+"\n");
			if (i == 10)
			{
				fout.close();
				System.exit(1);
			}
		}
		
		fout.close();
		fin.close();
	}
	
	private void merge(DEPTree tree)
	{
		TSentiment sentiment;
		String phrase;
		
		for (DEPNode node : tree)
		{
			phrase = StringUtils.toLowerCase(node.getSubNodeList().stream().map(DEPNode::getWordForm).collect(Collectors.joining(StringConst.SPACE)));
			sentiment = getSentiment(phrase);
			node.putFeat(DEPLib.FEAT_SA, sentiment.toValue());
		}
		
		phrase = StringUtils.toLowerCase(getWordForms(tree));
		sentiment = m_sentiments.get(phrase);
		if (sentiment == null) System.out.println(phrase);
		else tree.get(1).putFeat(DEPLib.FEAT_SAR, sentiment.toValue());
	}
	
	private TSentiment getSentiment(String phrase)
	{
		TSentiment t;
		
		if ((t = m_sentiments.get(phrase)) != null)
			return t;
		
		if ((t = getSubsequenceSentiment(phrase)) != null)
			return t;
		
		throw new IllegalArgumentException(phrase);
	}
	
	private TSentiment getSubsequenceSentiment(String phrase)
	{
		ObjectDoublePair<String> max = new ObjectDoublePair<String>(null, 0);
		String[] tokens = Splitter.split(phrase, PatternConst.WHITESPACES);
		double p, r, f;
		int c;
		
		for (Entry<String,String[]> e : m_tokens.entrySet())
		{
			c = StringUtils.getLCSLength(tokens, e.getValue());
			p = MathUtils.divide(c, tokens.length);
			r = MathUtils.divide(c, e.getValue().length);
			f = MathUtils.getF1(p, r);
			if (f > max.d) max.set(e.getKey(), f);
		}
		
		return (max.o != null) ? m_sentiments.get(max.o) : null;
	}
	
	private String getWordForms(DEPTree tree)
	{
		StringJoiner joiner = new StringJoiner(StringConst.SPACE);
		
		for (DEPNode node : tree)
			joiner.add(node.getWordForm());
		
		return joiner.toString();
	}
	
	static public void main(String[] args)
	{
		Merge m = new Merge();
		
		String phraseFile = args[0];
		String treeFile   = args[1];
		String outputFile = args[2];
		
		try
		{
			m.initMaps(new FileInputStream(phraseFile));
			m.printDEPTrees(new FileInputStream(treeFile), new FileOutputStream(outputFile));
		}
		catch (Exception e) {e.printStackTrace();}
	}
}
