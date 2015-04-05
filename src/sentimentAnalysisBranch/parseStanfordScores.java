package sentimentAnalysisBranch;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class parseStanfordScores {

	
	public static void parseIntensifiers(FileInputStream in, Map<String,Double> map) throws IOException {
		Scanner read = new Scanner(in);
		String word;
		double intensity;		
		while(read.hasNextLine()) {
			word = read.next();
			intensity = Double.parseDouble(read.next());
			map.put(word, intensity);
		}
		read.close();
	}
	
	public static void parseStanfordScores(FileInputStream in,List<Double> stanfordScore) throws IOException{
		Scanner read = new Scanner(in);
		int count = 0;
		while(read.hasNext() && count < 8504){
			String s = read.nextLine();
			int number = Integer.parseInt(s.substring(1, 2));
			double normalized = (number/2d)-1;
			stanfordScore.add(normalized);
			count++;
		}
		read.close();
	}
	
	public static void parseWordSentiments(Map<String,Double> map, FileInputStream in, String pattern) throws Exception {
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		String line;
		Pattern regexpattern = Pattern.compile(pattern);
		Matcher matches;
		double temp;
		while ((line = reader.readLine()) != null) {
			matches = regexpattern.matcher(line);
			while(matches.find()) {
				String StringPair = matches.group();
				StringPair = StringPair.replaceAll("\\(", "").replaceAll("\\)","");
				String[] split = StringPair.split(" ");
				temp = (.5*Integer.parseInt(split[0]))-1;
				map.put(split[1],temp);
			}
		}
		}
	
}
