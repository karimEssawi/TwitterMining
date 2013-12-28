package Weka;
/**
 *
 * @author Karim
 */
import java.io.*;
import java.util.Random;

import weka.core.*;
import weka.core.converters.*;
import weka.core.Utils.*;
import weka.core.tokenizers.*;
import weka.filters.*;
import weka.filters.unsupervised.attribute.*;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayesMultinomial;
import weka.classifiers.functions.Logistic;
import weka.classifiers.lazy.IBk;
import weka.core.converters.ConverterUtils.DataSource;
import weka.core.stemmers.*;
import weka.classifiers.functions.LibSVM;

/**
 * Example class that converts HTML files stored in a directory structure into 
 * and ARFF file using the TextDirectoryLoader converter. It then applies the
 * StringToWordVector to the data and feeds a J48 classifier with it.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 */
public class TextCategorizationTest {

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
    TextDirectoryLoader tweetloader = new TextDirectoryLoader();
    tweetloader.setDirectory(new File("DirectoryCrossVal\\Tweets"));
    Instances trainRaw = tweetloader.getDataSet();
    TextDirectoryLoader webPagesloader = new TextDirectoryLoader();
    webPagesloader.setDirectory(new File("DirectoryCrossVal\\Web Pages"));
    Instances webPagesRaw = webPagesloader.getDataSet();

    // apply the StringToWordVector
    // (see the source code of setOptions(String[]) method of the filter
    // if you want to know which command-line option corresponds to which
    // bean property)
    StringToWordVector filter = new StringToWordVector();
    
    filter.setInputFormat(webPagesRaw);
    filter.setInputFormat(trainRaw);

    filter.setTokenizer(new AlphabeticTokenizer());
    filter.setUseStoplist(true);
    filter.setLowerCaseTokens(true);
    filter.setStemmer(new LovinsStemmer());
//    filter.setMinTermFreq(5);
//    filter.setNormalizeDocLength(new SelectedTag(StringToWordVector.FILTER_NORMALIZE_ALL, StringToWordVector.TAGS_FILTER)); 
//    filter.setTFTransform(true);
//    filter.setIDFTransform(true);
//    filter.setOutputWordCounts(true);
//    filter.setWordsToKeep(10);
    
    Instances train = Filter.useFilter(trainRaw, filter);
    Instances webPages = Filter.useFilter(webPagesRaw, filter);
//    AttributeSelection attFilter = new AttributeSelection();
//    Ranker ranker = new Ranker();
//    ranker.setNumToSelect(50);
//    attFilter.setSearch(ranker);
//    attFilter.setEvaluator(new ChiSquaredAttributeEval());
//    attFilter.SelectAttributes(train);
//    train = attFilter.reduceDimensionality(train);
//    attFilter.SelectAttributes(webPages);
//    webPages = attFilter.reduceDimensionality(webPages);
    
    // randomize data
    int folds = 10;
    Instances randData = new Instances(train);
    randData.randomize(new Random(4));
    if (randData.classAttribute().isNominal())
      randData.stratify(folds);
    
    //classifier
    IBk knn= new IBk(4);
    NaiveBayesMultinomial nB = new NaiveBayesMultinomial();
    Logistic mE = new Logistic();
    LibSVM svm = new LibSVM();
    // perform cross-validation
    Evaluation eval = new Evaluation(randData);
    for (int n = 0; n < folds; n++) {
      Instances tweetTrain = randData.trainCV(folds, n);
//      Instances allTrain = Instances.mergeInstances(tweetTrain, webPages);
      Instances allTrain = new TextCategorizationTest().merge(tweetTrain, webPages);
      Instances test = randData.testCV(folds, n);
      // the above code is used by the StratifiedRemoveFolds filter, the
      // code below by the Explorer/Experimenter:
      // Instances train = randData.trainCV(folds, n, rand);

      // build and evaluate classifier
      IBk clsCopy = (IBk)knn.makeCopy(knn);
//      NaiveBayesMultinomial clsCopy = (NaiveBayesMultinomial)nB.makeCopy(nB);
      
//      LibSVM clsCopy = (LibSVM)svm.makeCopy(svm);
      
      clsCopy.buildClassifier(allTrain);
      eval.evaluateModel(clsCopy, test);
    }
        System.out.println("\n\nFiltered data:\n\n" + train);
    System.out.println(train.toSummaryString());
    
    // output evaluation
    System.out.println();
    System.out.println("=== Setup ===");
    System.out.println("Classifier: " + knn.getClass().getName() + " " + Utils.joinOptions(knn.getOptions()));
    System.out.println("Dataset: " + train.relationName());
    System.out.println("Folds: " + folds);
    System.out.println("Seed: " + 4);
    System.out.println();
    System.out.println(eval.toSummaryString("=== " + folds + "-fold Cross-validation ===", false));
    
//    System.out.println("\n\nFiltered data:\n\n" + dataFiltered);
//    System.out.println(dataFiltered.toSummaryString());
//    TextDirectoryLoader loaderTest = new TextDirectoryLoader();
//    loaderTest.setDirectory(new File("DirectoryCrossVal\\Tweets"));
//    Instances testRaw = loaderTest.getDataSet();
//    System.out.println("\n\nImported data:\n\n" + train.numInstances());
//    Instances test = Filter.useFilter(testRaw, filter);
//    NaiveBayesMultinomial cls = new NaiveBayesMultinomial();
//    cls.buildClassifier(train);
    
    // evaluate classifier and print some statistics
//    Evaluation eval = new Evaluation(train);
//    eval.evaluateModel(cls, test);
//    System.out.println(eval.toSummaryString("\nResults\n\n", false));
//    System.out.println("\n\nClassifier model:\n\n" + knn.toString());
    
//    FilteredClassifier fc = new FilteredClassifier();
//    fc.setFilter(filter);
//    fc.setClassifier(cls);
//    fc.buildClassifier(train);
//    for (int i = 0; i < test.numInstances(); i++) {
//        double pred = fc.classifyInstance(test.instance(i));
//        System.out.print("ID: " + test.instance(i).value(0));
//        System.out.print(", actual: " + test.classAttribute().value((int) test.instance(i).classValue()));
//        System.out.println(", predicted: " + test.classAttribute().value((int) pred));
//    }
  }
  
  public static Instances merge(Instances data1, Instances data2) throws Exception{
    // Check where are the string attributes
    int asize = data1.numAttributes();
    boolean strings_pos[] = new boolean[asize];
    for(int i=0; i<asize; i++)
    {
        Attribute att = data1.attribute(i);
        strings_pos[i] = ((att.type() == Attribute.STRING) ||
                          (att.type() == Attribute.NOMINAL));
    }

    // Create a new dataset
    Instances dest = new Instances(data1);
    dest.setRelationName(data1.relationName() + " + " + data2.relationName());

    DataSource source = new DataSource(data2);
    Instances instances = source.getStructure();
    Instance instance = null;
    while (source.hasMoreElements(instances)) {
        instance = source.nextElement(instances);
        dest.add(instance);

        // Copy string attributes
        for(int i=0; i<asize; i++) {
            if(strings_pos[i]) {
                dest.instance(dest.numInstances()-1)
                    .setValue(i,instance.stringValue(i));
            }
        }
    }
    return dest;
  }
}

