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
import java.util.Random;

import weka.attributeSelection.*;
import weka.core.*;
import weka.core.converters.*;
import weka.core.Utils.*;
import weka.core.tokenizers.*;
import weka.core.stemmers.*;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.*;
import weka.classifiers.*;
import weka.classifiers.functions.LibSVM;
import weka.classifiers.meta.FilteredClassifier;
import weka.filters.MultiFilter;

public class RelevantClassifier {
    public static void main(String[] args) throws Exception{
        TextDirectoryLoader loaderTest = new TextDirectoryLoader();
        loaderTest.setDirectory(new File("DirectoryIrrelevant\\Training"));
        Instances trainRaw = loaderTest.getDataSet();
        loaderTest.setDirectory(new File("DirectoryIrrelevant\\Testing"));
        Instances testRaw = loaderTest.getDataSet();
        
        StringToWordVector filter = new StringToWordVector();
        filter.setInputFormat(trainRaw);
        filter.setInputFormat(testRaw);
        
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
        Instances train = Filter.useFilter(trainRaw, filter);
        Instances test = Filter.useFilter(testRaw, filter);
        
        weka.filters.supervised.attribute.AttributeSelection attFilter = new weka.filters.supervised.attribute.AttributeSelection();
        Ranker ranker = new Ranker();
        ranker.setNumToSelect(-1);
//        ranker.setThreshold(0.0);
        attFilter.setSearch(ranker);
        attFilter.setEvaluator(new InfoGainAttributeEval());
        
        Filter[] multiFilter = {filter,attFilter};
        MultiFilter mf = new MultiFilter();
        mf.setFilters(multiFilter);
        

        LibSVM svm = new LibSVM();
//        svm.setSVMType(new SelectedTag(LibSVM.SVMTYPE_ONE_CLASS_SVM, LibSVM.TAGS_SVMTYPE));
        FilteredClassifier fc = new FilteredClassifier();
        fc.setFilter(mf);
        fc.setClassifier(svm);
        
        //Cross Validation
//        int folds = 10;
//        Instances randData = new Instances(train);
//        randData.randomize(new Random(4));
//        if (randData.classAttribute().isNominal())
//            randData.stratify(folds);
//        Evaluation eval = new Evaluation(randData);
//        for (int n = 0; n < folds; n++){
//            Instances trainFold = randData.trainCV(folds, n);
//            Instances testFold = randData.testCV(folds, n);
//
//            FilteredClassifier clsCopy = (FilteredClassifier)fc.makeCopy(fc);
//            clsCopy.buildClassifier(trainFold);
//            eval.evaluateModel(clsCopy, testFold);
//        }
//        System.out.println(eval.toSummaryString("=== " + folds + "-fold Cross-validation ===", false));
        
        //Hold Out
        Evaluation eval = new Evaluation(trainRaw);
        fc.buildClassifier(trainRaw);
        eval.evaluateModel(fc, testRaw);
        System.out.println(eval.toSummaryString("\nResults\n======\n", false));
        
        //Serialize the model
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("wekaBinaryClassifier.model"));
        oos.writeObject(fc);
        oos.flush();
        oos.close();
    }
}
