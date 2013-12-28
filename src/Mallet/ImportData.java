/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Mallet;

/**
 *
 * @author Karim
 */

import java.io.*;
import java.util.*;
import java.util.regex.*;

import cc.mallet.pipe.*;
import cc.mallet.pipe.iterator.*;
import cc.mallet.types.*;
import org.tartarus.snowball.SnowballStemmer;
import Mallet.SnowBall.englishStemmer;

public class ImportData {
    
    Pipe pipe;
    
    public ImportData() {
        pipe = buildPipe();
    }

    public Pipe buildPipe() {
        ArrayList pipeList = new ArrayList();

        // Read data from File objects
        pipeList.add(new Input2CharSequence("UTF-8"));
                
        // Remove the HTML contents
        pipeList.add(new CharSequenceRemoveHTML());

        // Regular expression for what constitutes a token.
        //  This pattern includes Unicode letters, Unicode numbers, 
        //   and the underscore character. Alternatives:
        //    "\\S+"   (anything not whitespace)
        //    "\\w+"    ( A-Z, a-z, 0-9, _ )
        //    "[\\p{L}\\p{N}_]+|[\\p{P}]+"   (a group of only letters and numbers OR
        //                                    a group of only punctuation marks)
        Pattern tokenPattern =
            Pattern.compile("[\\p{L}\\p{N}_]+");

        // Tokenize raw strings
        pipeList.add(new CharSequence2TokenSequence(tokenPattern));
//        int x[]={2};
//        pipeList.add(new TokenSequenceNGrams(x));
        
        // Tokenize using N-grams
//        pipeList.add(new CharSequence2CharNGrams(2, true));
        
        // Remove non Alphabet words from the Token Sequence
        pipeList.add(new TokenSequenceRemoveNonAlpha());

        // Normalize all tokens to all lowercase
        pipeList.add(new TokenSequenceLowercase());

        // Remove stopwords from a standard English stoplist.
        //  options: [case sensitive] [mark deletions]
        TokenSequenceRemoveStopwords tokenStopWords = new TokenSequenceRemoveStopwords(false, false);
        pipeList.add(tokenStopWords);
        
        //  Perform stemming on tokens (snowball stemmer)
//        TokenSequence stemmedToken = new TokenSequence();
//        SnowballStemmer stemmer = new englishStemmer();
//        Alphabet a = tokenStopWords.getAlphabet();
//        Iterator it = a.iterator();
//        Token token = null;
//        while (it.hasNext()) {
//            token = (Token)it.next();
//            stemmer.setCurrent(token.getText());
//            stemmer.stem();
//            stemmedToken.add(stemmer.getCurrent());
//        }
        
        // Rather than storing tokens as strings, convert 
        //  them to integers by looking them up in an alphabet.
        pipeList.add(new TokenSequence2FeatureSequence());
//        pipeList.add(new FeatureCountPipe());
        
        // Do the same thing for the "target" field: 
        //  convert a class label string to a Label object,
        //  which has an index in a Label alphabet.
        pipeList.add(new Target2Label());

        // Now convert the sequence of features to a sparse vector,
        //  mapping feature IDs to counts.
        pipeList.add(new FeatureSequence2FeatureVector());
        
        // Print out the features and the label
//        pipeList.add(new PrintInputAndTarget());

        return new SerialPipes(pipeList);
    }

    public InstanceList readDirectory(File directory) {
        return readDirectories(new File[] {directory});
    }

    public InstanceList readDirectories(File[] directories) {
        
        // Construct a file iterator, starting with the 
        //  specified directories, and recursing through subdirectories.
        // The second argument specifies a FileFilter to use to select
        //  files within a directory.
        // The third argument is a Pattern that is applied to the 
        //   filename to produce a class label. In this case, I've 
        //   asked it to use the last directory name in the path.
        FileIterator iterator =
            new FileIterator(directories,
//                             new TxtFilter(), //File type filter
                             FileIterator.LAST_DIRECTORY);

        // Construct a new instance list, passing it the pipe
        //  we want to use to process instances.
        InstanceList instances = new InstanceList(pipe);

        // Now process each instance provided by the iterator.
        instances.addThruPipe(iterator);
        
        // Feature selection based on Information Gain
//        InfoGain ig = new InfoGain(instances);
//        FeatureCounts fc = new FeatureCounts(instances);
//        FeatureSelection selectedFeatures = new FeatureSelection(fc, 2000);
//        instances.setFeatureSelection(selectedFeatures);

        return instances;
    }
       
    /** This class illustrates how to build a simple file filter */
    class TxtFilter implements FileFilter {

        /** Test whether the string representation of the file 
         *   ends with the correct extension. Note that {@ref FileIterator}
         *   will only call this filter if the file is not a directory,
         *   so we do not need to test that it is a file.
         */
        public boolean accept(File file) {
            return file.toString().endsWith(".htm");
        }
    }
}
