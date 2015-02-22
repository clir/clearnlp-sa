package sentimentAnalysis;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class SentimentTrain {

	private Map<String,Double> m_labels;

	public SentimentTrain() throws Exception
	{
		m_labels = new HashMap<>();
		
		m_labels.put("A", 0d);
		m_labels.put("B", 1d);
		m_labels.put("C", 2d);
		
		System.out.println(m_labels);
		
		save(m_labels, new BufferedOutputStream(new GZIPOutputStream(new FileOutputStream("model.gz"))));
		m_labels = null;
		m_labels = load(new BufferedInputStream(new GZIPInputStream(new FileInputStream("model.gz"))));
		
		System.out.println(m_labels);
	}
	
	public void save(Map<String,Double> map, OutputStream out) throws Exception
	{
		ObjectOutputStream oout = new ObjectOutputStream(out);
		oout.writeObject(map);
		oout.close();
	}
	
	@SuppressWarnings("unchecked")
	public Map<String,Double> load(InputStream in) throws Exception
	{
		ObjectInputStream oin = new ObjectInputStream(in);
		Map<String,Double> map = (Map<String,Double>)oin.readObject();
		oin.close();
		return map;
	}
	
	static public void main(String[] args) throws Exception
	{
		new SentimentTrain();
	}
}
