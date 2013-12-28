/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Weka;
/**
 *
 * @author Karim
 */
import java.io.*;

import weka.attributeSelection.*;
import weka.core.*;
import weka.core.converters.*;
import weka.core.Utils.*;
import weka.core.tokenizers.*;
import weka.core.stemmers.*;
import weka.filters.unsupervised.attribute.*;
import weka.classifiers.*;
import weka.classifiers.bayes.NaiveBayesMultinomial;
import weka.classifiers.functions.Logistic;
import weka.classifiers.functions.LibSVM;
import weka.classifiers.lazy.IBk;
import weka.classifiers.meta.FilteredClassifier;
import weka.filters.Filter;
import weka.filters.MultiFilter;


/**
 * Example class that converts HTML files stored in a directory structure into 
 * and ARFF file using the TextDirectoryLoader converter. It then applies the
 * StringToWordVector to the data and feeds a J48 classifier with it.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class WekaHoldOut {

  /**
   * Expects the first parameter to point to the directory with the text files.
   * In that directory, each sub-directory represents a class and the text
   * files in these sub-directories will be labeled as such.
       *
   * @param args        the commandline arguments
   * @throws Exception  if something goes wrong
   */
//  public double calculateAccuracy(FastVector predictions) {
//        double correct = 0;
//        
//        for (int i = 0; i < predictions.size(); i++) {
//            NominalPrediction np = (NominalPrediction) predictions.elementAt(i);
//            if (np.predicted() == np.actual()) {
//                correct++;
//            }
//        }
//  }
    
  public static void main(String[] args) throws Exception {
    // convert the directory into a dataset
    TextDirectoryLoader loaderTest = new TextDirectoryLoader();
    loaderTest.setDirectory(new File("DirectoryHoldOut\\Training"));
//    loaderTest.setDirectory(new File("ACC Directory\\svm\\training"));
    Instances trainRaw = loaderTest.getDataSet();
    loaderTest.setDirectory(new File("DirectoryHoldOut\\Testing"));
//    loaderTest.setDirectory(new File("ACC Directory\\svm\\testing"));
    Instances testRaw = loaderTest.getDataSet();
//    loaderTest.s
//    Instance toClassifyInstance = 

    // apply the StringToWordVector
    // (see the source code of setOptions(String[]) method of the filter
    // if you want to know which command-line option corresponds to which
    // bean property)
    StringToWordVector filter = new StringToWordVector();
    filter.setTokenizer(new AlphabeticTokenizer());
    filter.setUseStoplist(true);
    filter.setLowerCaseTokens(true);
    filter.setStemmer(new LovinsStemmer());
    filter.setTFTransform(true);
    filter.setIDFTransform(true);
//    filter.setMinTermFreq(10);
//    filter.setNormalizeDocLength(new SelectedTag(StringToWordVector.FILTER_NORMALIZE_ALL, StringToWordVector.TAGS_FILTER)); 
//    filter.setOutputWordCounts(true);
//    filter.setWordsToKeep(500);
    
//    filter.setInputFormat(trainRaw);
//    Instances train = Filter.useFilter(trainRaw, filter);
//    Instances test = Filter.useFilter(testRaw, filter);
    
//    AttributeSelectedClassifier classifier = new AttributeSelectedClassifier();
//    InfoGainAttributeEval evall = new InfoGainAttributeEval();
//    RankSearch search = new RankSearch();
     
    weka.filters.supervised.attribute.AttributeSelection attFilter = new weka.filters.supervised.attribute.AttributeSelection();
    Ranker ranker = new Ranker();
    ranker.setNumToSelect(-1);
    ranker.setThreshold(0.0);
    attFilter.setSearch(ranker);
    attFilter.setEvaluator(new InfoGainAttributeEval());
//    attFilter.setInputFormat(train);
//    train = Filter.useFilter(train, attFilter);
//    attFilter.setInputFormat(test);
//    test = Filter.useFilter(test, attFilter);
//    attFilter.SelectAttributes(train);
//    train = attFilter.reduceDimensionality(train);
//    attFilter.SelectAttributes(test);
//    test = attFilter.reduceDimensionality(test);
   
    
    //classifier
    IBk knn= new IBk(3);
    NaiveBayesMultinomial nB = new NaiveBayesMultinomial();
    Logistic mE = new Logistic();
    LibSVM svm = new LibSVM();
    FilteredClassifier fc = new FilteredClassifier();

    // build and evaluate classifier
    svm.setSVMType(new SelectedTag(LibSVM.KERNELTYPE_RBF, LibSVM.TAGS_KERNELTYPE));
//    svm.setNormalize(true);
//    svm.setCost(0.3);
//    svm.setWeights("1 1 1 0.2 1");
//    svm.buildClassifier(train);
//    classifier.setClassifier(svm);
//    classifier.buildClassifier(train);
//    classifier.setEvaluator(evall);
//    classifier.setSearch(search);
//    
//    Evaluation eval = new Evaluation(train);
//    eval.evaluateModel(classifier, test);
    
    Filter[] multiFilter = {filter,attFilter};
    MultiFilter mf = new MultiFilter();
    mf.setFilters(multiFilter);
    
    fc.setFilter(mf);
    fc.setClassifier(svm);
    fc.buildClassifier(trainRaw);
    Evaluation eval = new Evaluation(trainRaw);
    eval.evaluateModel(fc, testRaw);
//    ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("wekaClassifier.model"));
//    oos.writeObject(fc);
//    oos.flush();
//    oos.close();
    
//    eval.crossValidateModel(classifier, train, 10, new Random(1));
    System.out.println(eval.toSummaryString("\nResults\n======\n", false));
    }
}
