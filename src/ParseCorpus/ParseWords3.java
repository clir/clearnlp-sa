package ParseCorpus;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class ParseWords3 {

  public static void main(String args[]) throws IOException {
	ParseWords3 parser = new ParseWords3("subjectivity.txt");
    parser.processLineByLine();
    log("Done.");
  }
  
  public ParseWords3(String aFileName){
    fFilePath = Paths.get(aFileName);
  }
  
  
  public final void processLineByLine() throws IOException {
    try (Scanner scanner =  new Scanner(fFilePath, ENCODING.name())){
        int count = 0;

      while (scanner.hasNextLine()){
        processLine(scanner.nextLine());
        System.out.println(count++);

      }      
    }
  }
  
  
  protected void processLine(String aLine){
    Scanner scanner = new Scanner(aLine);
    scanner.useDelimiter(" ");
    if (scanner.hasNext()){
      String type = scanner.next();
      String len = scanner.next();
      String word = scanner.next();
      String pos = scanner.next();
      String stemmed = scanner.next();
      String polarity = scanner.next();
//      System.out.println(type);
//      System.out.println(len);
//      System.out.println(word);
//      System.out.println(pos);
//      System.out.println(stemmed);
//      System.out.println(polarity);
//      System.out.println(polarity);
//      System.out.println(type.substring(5));
//      System.out.println(word.substring(6));
      System.out.println(type.substring(5) + "\n" + word.substring(6) + "\n" + polarity.substring(14));
    }
    else {
      log("Empty or invalid line. Unable to process.");
    }
  }
  
  private final Path fFilePath;
  private final static Charset ENCODING = StandardCharsets.UTF_8;  
  
  private static void log(Object aObject){
    System.out.println(aObject);
  }
  
  private String quote(String aText){
    String QUOTE = "'";
    return QUOTE + aText + QUOTE;
  }
} 