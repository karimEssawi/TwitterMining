/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Lucene;

/**
 *
 * @author Karim
 */

import org.apache.lucene.analysis.standard.*;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
//import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.util.CharArraySet;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.MultiFields;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.util.BytesRef;

import java.io.*;
import java.util.*;

import jxl.*;
import jxl.write.*;
import jxl.write.Number;


/**
 * This terminal application creates an Apache Lucene index in a folder and adds files into this index
 * based on the input of the user.
 */
public class TextFileIndexer {
    
    private static EnglishAnalyzer analyzer = null; //new EnglishAnalyzer(Version.LUCENE_44);
    private IndexWriter writer;
    private ArrayList<File> queue = new ArrayList<File>();
    private static HashSet set = new HashSet();
   
    public static void main(String[] args) throws IOException, WriteException {
//    System.out.println("Enter the path where the index will be created: (e.g. /tmp/index or c:\\temp\\index)");
    try{
        BufferedReader in = new BufferedReader(new FileReader("stopwords.txt"));
        while (in.ready()) {
            set.add(in.readLine());
        }
    }catch(IOException e){}
    analyzer = new EnglishAnalyzer(Version.LUCENE_44,new CharArraySet(Version.LUCENE_44, set, true));
//    analyzer.setMaxTokenLength(20);
    String indexLocation = null;
//    BufferedReader br = new BufferedReader(
//    new InputStreamReader(System.in));
    String s = "ACC Directory\\Web Pages";
    TextFileIndexer indexer = null;
    
    try {
      indexLocation = s;
      indexer = new TextFileIndexer(s);
    } catch (Exception ex) {
      System.out.println("Cannot create index..." + ex.getMessage());
      System.exit(-1);
    }

    //===================================================
    //read input from user until he enters q for quit
    //===================================================
////    while (!s.equalsIgnoreCase("q")) {
      try {
        //try to add file into the index
            indexer.indexFileOrDirectory(s);
      } catch (Exception e) {
            System.out.println("Error indexing " + s + " : " + e.getMessage());
        }
//    }

    //===================================================
    //after adding, we always have to call the
    //closeIndex, otherwise the index is not created    
    //===================================================
    indexer.closeIndex();
   
//    IndexReader reader = DirectoryReader.open(FSDirectory.open(new File(indexLocation)));
//    TermsEnum termEnum = MultiFields.getTerms(reader, "contents").iterator(null);
//    BytesRef term1 = null;
//    WritableWorkbook workBook = Workbook.createWorkbook(new File("Overlapping terms.xls"));
//    WritableSheet sheet = workBook.createSheet("First Sheet", 0);
//    HashSet<String> webSet = new HashSet<String>();
//    HashSet<String> tweetSet = new HashSet<String>();
//    try{
//        BufferedReader i = new BufferedReader(new FileReader("t.txt"));
//        while (i.ready()) {
//            tweetSet.add(i.readLine());
//        }
//        BufferedReader i2 = new BufferedReader(new FileReader("w.txt"));
//        while (i2.ready()) {
//            webSet.add(i2.readLine());
//        }
//        Iterator tIterator = tweetSet.iterator();
//        int count = 0;
//        while(tIterator.hasNext()){
//            if(webSet.contains((String)tIterator.next())) {
//                sheet.addCell(new Label(0, count, (String)tIterator.next()));
//                count++;
//            } 
//        }
//    }catch(IOException e){e.printStackTrace();}
    
//    int counter = 0;
//        while((term1 = termEnum.next()) != null) {
//            if (termEnum.totalTermFreq() > 1) {
////                System.out.println("1 : " + termEnum.term().utf8ToString() + " : " + termEnum.docFreq() + " : " + termEnum.totalTermFreq());
//                try{
//                    sheet.addCell(new Label(0, counter, termEnum.term().utf8ToString()));
//                    sheet.addCell(new Number(1, counter, termEnum.totalTermFreq()));
//                    sheet.addCell(new Number(2, counter, termEnum.docFreq()));
//                    counter++;
//                }catch(WriteException e){e.printStackTrace();}
//            }
//        }
//    workBook.write();
//    workBook.close();
//    reader.close();

    //=========================================================
    // Now search
    //=========================================================
//    IndexReader reader = DirectoryReader.open(FSDirectory.open(new File(indexLocation)));
//    IndexSearcher searcher = new IndexSearcher(reader);
//    TopScoreDocCollector collector = TopScoreDocCollector.create(5, true);

//    s = "";
//    while (!s.equalsIgnoreCase("q")) {
//      try {
//        System.out.println("Enter the search query (q=quit):");
//        s = br.readLine();
//        if (s.equalsIgnoreCase("q")) {
//          break;
//        }
//        Query q = new QueryParser(Version.LUCENE_40, "contents", analyzer).parse(s);
//        searcher.search(q, collector);
//        ScoreDoc[] hits = collector.topDocs().scoreDocs;
//
//        // 4. display results
//        System.out.println("Found " + hits.length + " hits.");
//        for(int i=0;i<hits.length;++i) {
//          int docId = hits[i].doc;
//          Document d = searcher.doc(docId);
//          System.out.println((i + 1) + ". " + d.get("path") + " score=" + hits[i].score);
//        }
//
//      } catch (Exception e) {
//        System.out.println("Error searching " + s + " : " + e.getMessage());
//      }
//    }

  }

  /**
   * Constructor
   * @param indexDir the name of the folder in which the index should be created
   * @throws java.io.IOException when exception creating index.
   */
  TextFileIndexer(String indexDir) throws IOException {
    // the boolean true parameter means to create a new index everytime, 
    // potentially overwriting any existing files there.
    FSDirectory dir = FSDirectory.open(new File(indexDir));
    IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_44, analyzer);
    writer = new IndexWriter(dir, config);
  }

  /**
   * Indexes a file or directory
   * @param fileName the name of a text file or a folder we wish to add to the index
   * @throws java.io.IOException when exception
   */
  public void indexFileOrDirectory(String fileName) throws IOException {
    //===================================================
    //gets the list of files in a folder (if user has submitted
    //the name of a folder) or gets a single file name (is user
    //has submitted only the file name) 
    //===================================================
    addFiles(new File(fileName));
    
    int originalNumDocs = writer.numDocs();
    for (File f : queue) {
      FileReader fr = null;
      try {
        Document doc = new Document();

        //===================================================
        // add contents of file
        //===================================================
        fr = new FileReader(f);
        doc.add(new TextField("contents", fr));
//        doc.add(new StringField("path", f.getPath(), Field.Store.YES));
//        doc.add(new StringField("filename", f.getName(), Field.Store.YES));

        writer.addDocument(doc);
        System.out.println("Added: " + f);
      } catch (Exception e) {
        System.out.println("Could not add: " + f);
      } finally {
        fr.close();
      }
    }
    
    int newNumDocs = writer.numDocs();
    System.out.println("");
    System.out.println("************************");
    System.out.println((newNumDocs - originalNumDocs) + " documents added.");
    System.out.println("************************");

    queue.clear();
  }

  private void addFiles(File file) {

    if (!file.exists()) {
      System.out.println(file + " does not exist.");
    }
    if (file.isDirectory()) {
      for (File f : file.listFiles()) {
        addFiles(f);
      }
    } else {
      String filename = file.getName().toLowerCase();
      //===================================================
      // Only index text files
      //===================================================
      if (filename.endsWith(".htm") || filename.endsWith(".html") || 
              filename.endsWith(".xml") || filename.endsWith(".txt")) {
        queue.add(file);
      } else {
        System.out.println("Skipped " + filename);
      }
    }
  }

  /**
   * Close the index.
   * @throws java.io.IOException when exception closing
   */
  public void closeIndex() throws IOException {
    writer.close();
  }
}

