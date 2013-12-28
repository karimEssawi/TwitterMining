/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Mallet;

import cc.mallet.classify.*;
import cc.mallet.types.*;
import cc.mallet.util.FeatureCounter;
import cc.mallet.util.Randoms;

import java.io.*;
import java.util.*;

/**
 *
 * @author Karim
 */
public class Main {
    
    public static void main (String[] args) throws IOException, ClassNotFoundException {

        ImportData importer = new ImportData();
        Eval eval = new Eval();
        Serialize serialize = new Serialize();
//        InstanceList instances = importer.readDirectory(new File("DirectoryHoldOut - Copy"));
//        FeatureConstraintUtil fcu = new FeatureConstraintUtil();
//        Iterator i = fcu.selectFeaturesByInfoGain(instances, 1000).iterator();
//        while (i.hasNext()){
//            System.out.println(i.next().toString());
//        }
//        InstanceList tweetInstances = importer.readDirectory(new File("DirectoryCrossVal - Copy\\Tweets"));
//        InstanceList wpInstances = importer.readDirectory(new File("DirectoryCrossVal - Copy\\Web Pages"));
        InstanceList releventInstances = importer.readDirectory(new File("DirectoryIrrelevant - Copy\\Testing"));
        
//        FeatureCounter fc = new FeatureCounter(wpInstances);
//        fc.count();
//        fc.printCounts();  //Print term frequency
//        instances.save(new File("mallet-2.0.7\\training.mallet")); //Save extracted features into a file
//        KNNTrainer knnTrainer = new KNNTrainer(6, new NormalizedDotProductMetric());
//        KNN knn = null;//KNN(6, new NormalizedDotProductMetric(), instances);
//      
//        NaiveBayesTrainer nBTrainer = new NaiveBayesTrainer().setDocLengthNormalization(0.5);
//        NaiveBayes nB = null;//nBTrainer.train(instances)  
//
//        SVMTrainer svmTrainer = new SVMTrainer();
//        SVM svm = svmTrainer.train(releventInstances);
        
        MaxEntTrainer maxTrainer = new MaxEntTrainer(1.0);
        MaxEnt mE = null;//maxTrainer.train(instances, 5);
            
//            FeatureSelector fs = new FeatureSelector(new FeatureCounts.Factory(), 9.0);
//            fs.selectFeaturesFor(instances);
//            FeatureSelectingClassifierTrainer c = new FeatureSelectingClassifierTrainer(maxTrainer, fs);
//            mE = (MaxEnt)c.train(instances);
  
//        MaxEntGETrainer maxGETrainer = new MaxEntGETrainer();
//        maxGETrainer.setConstraintsFile("constraints.txt");
//        maxGETrainer.setGaussianPriorVariance(0.1);
//        instances.hideSomeLabels(1.0, new Randoms());
//        MaxEnt mE = maxGETrainer.train(instances,5);
//            serialize.saveClassifier(mE, new File("MaxEntGE.mallet")); //save the classifier to disk

//        eval.crossValidation(tweetInstances, wpInstances, maxTrainer, mE, 10);   // Test the classifier using Cross validation
//        Trial holdOutTrial = eval.testTrainSplit(tweetInstances, wpInstances, maxTrainer, mE, 0.7, 0.3);  // Test the classifier using Holdout
//        // Print some evaluation measures for Holdout
//        System.out.println("Hold out Accuracy: " + holdOutTrial.getAccuracy());
//        Object classes [] = tweetInstances.getTargetAlphabet().toArray();
//        for (int i = 0; i < classes.length; i++){
//            System.out.println(classes[i].toString() + " Precision: " + holdOutTrial.getPrecision(classes[i])
//                                                     + " Recall: " + holdOutTrial.getRecall(classes[i])
//                                                     + " F1: " + holdOutTrial.getF1(classes[i]));
//        }
        
        Trial holdOutRelevant = eval.holdOutRelevant(releventInstances, maxTrainer, mE, 0.7, 0.3);  // Test the classifier using Holdout
//        // Print some evaluation measures for Holdout
        System.out.println("Hold out Accuracy: " + holdOutRelevant.getAccuracy());
        Object classes [] = releventInstances.getTargetAlphabet().toArray();
        for (int i = 0; i < classes.length; i++){
            System.out.println(classes[i].toString() + " Precision: " + holdOutRelevant.getPrecision(classes[i])
                                                     + " Recall: " + holdOutRelevant.getRecall(classes[i])
                                                     + " F1: " + holdOutRelevant.getF1(classes[i]));
        }
        
//        mE = (MaxEnt)serialize.loadClassifier(new File("ServiceClassifier.mallet"));    //Load the saved classifier from the HoldOut trial
//        eval.evaluate(mE, new File("HoldOutTweets.txt"),importer.buildPipe());  //Test the classifier on a separate test set
        
//        
        
         // Test the classifier on a single unlabled instance
//        mE = (MaxEnt)serialize.loadClassifier(new File("MaxEntGE.mallet"));
//         Classification c = mE.classify("Love love love fireworks! #fireworks #aberdeen #pretty http://t.co/y3bPzmxloZ");
//         System.out.println("Class Name - " + c.getLabeling().getBestLabel());

//         // Test the classifier on a set of unlabeled instances
//        try{ 
//             BufferedReader in = new BufferedReader(new FileReader("asd.txt"));
//             int counter = 1;
//             while (in.ready()) {
//                String ss = in.readLine();
//                Classification c = mE.classify(ss);
//                System.out.println(counter + " Class Name - " + c.getLabeling().getBestLabel().toString());
//                counter++;
//             }
//             in.close(); 
//        }
//        catch (IOException e){e.printStackTrace();}
    }
}
