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
import java.util.Random;

import cc.mallet.pipe.iterator.*;
import cc.mallet.types.*;
import cc.mallet.classify.*;
import cc.mallet.pipe.Pipe;
import cc.mallet.util.Randoms;
import java.util.*;

public class Eval {
    

    // Perform Holdout testing
    public Trial testTrainSplit(InstanceList instances, InstanceList wpInstances, ClassifierTrainer trainer, Classifier classifier, double trainPercentage, double testPercentage) {

        int TRAINING = 0;
        int TESTING = 1;
        int VALIDATION = 2;

        // Split the input list into training (90%) and testing (10%) lists.                               
	// The division takes place by creating a copy of the list,                                        
	//  randomly shuffling the copy, and then allocating                                               
	//  instances to each sub-list based on the provided proportions.                                  

        InstanceList[] instanceLists = instances.split(new Random(), new double[] {trainPercentage, testPercentage, 0.0});
        
        ArrayList<InstanceList> arrayList  = new ArrayList<InstanceList>();
        arrayList.add(instanceLists[TRAINING]);
        arrayList.add(wpInstances);
        MultiInstanceList multiList = new MultiInstanceList(arrayList);

	// The third position is for the "validation" set,                                                 
        //  which is a set of instances not used directly                                                  
        //  for training, but available for determining                                                    
        //  when to stop training and for estimating optimal                                               
	//  settings of nuisance parameters.                                                               
	// Most Mallet ClassifierTrainers can not currently take advantage                                 
        //  of validation sets. 
        
            FeatureSelector fs = new FeatureSelector(new FeatureCounts.Factory(), 8.0);
            fs.selectFeaturesFor(multiList);
            FeatureSelectingClassifierTrainer c = new FeatureSelectingClassifierTrainer(trainer, fs);
            classifier = c.train(multiList);
        
//        classifier = trainer.train(multiList);
//        
        try{
            new Serialize().saveClassifier(classifier, new File("ServiceClassifier.mallet")); //save the classifier to disk
        }catch(IOException e){e.printStackTrace();}
        
        for(Instance i : instanceLists[TESTING]){
            System.out.println(i.getName());
        }
        
        return new Trial(classifier, instanceLists[TESTING]);
    }
    
        public Trial holdOutRelevant(InstanceList instances, ClassifierTrainer trainer, Classifier classifier, double trainPercentage, double testPercentage) {

        int TRAINING = 0;
        int TESTING = 1;
        int VALIDATION = 2;

        // Split the input list into training (90%) and testing (10%) lists.                               
	// The division takes place by creating a copy of the list,                                        
	//  randomly shuffling the copy, and then allocating                                               
	//  instances to each sub-list based on the provided proportions.                                  

        InstanceList[] instanceLists = instances.split(new Random(), new double[] {trainPercentage, testPercentage, 0.0});
        
        ArrayList<InstanceList> arrayList  = new ArrayList<InstanceList>();
        arrayList.add(instanceLists[TRAINING]);
        
        MultiInstanceList multiList = new MultiInstanceList(arrayList);

	// The third position is for the "validation" set,                                                 
        //  which is a set of instances not used directly                                                  
        //  for training, but available for determining                                                    
        //  when to stop training and for estimating optimal                                               
	//  settings of nuisance parameters.                                                               
	// Most Mallet ClassifierTrainers can not currently take advantage                                 
        //  of validation sets. 
        
            FeatureSelector fs = new FeatureSelector(new FeatureCounts.Factory(), 1.0);
            fs.selectFeaturesFor(instanceLists[TRAINING]);
            FeatureSelectingClassifierTrainer c = new FeatureSelectingClassifierTrainer(trainer, fs);
            classifier = c.train(multiList);
//        classifier = trainer.train(multiList);
            try{
            new Serialize().saveClassifier(classifier, new File("RelevantClassifier.mallet")); //save the classifier to disk
        }catch(IOException e){e.printStackTrace();}
            
        return new Trial(classifier, instanceLists[TESTING]);
    }
    
    
    
    // Perform n-fold cross validation
    public void crossValidation(InstanceList tweetInstances, InstanceList wpInstances, ClassifierTrainer trainer, Classifier classifier, int foldNum){
        
        InstanceList.CrossValidationIterator cvIter = tweetInstances.crossValidationIterator(foldNum);
//        CrossValidationIterator cvIter = new CrossValidationIterator(tweetInstances, foldNum, new Random());
        int foldCounter = 1;
        double averageAccuracy = 0.0, //CGXPrecision = 0.0, CGXRecall = 0.0, CGXF1 = 0.0,
                                      ECSPrecision = 0.0, ECSRecall = 0.0, ECSF1 = 0.0,
                                      EPIPrecision = 0.0, EPIRecall = 0.0, EPIF1 = 0.0,
                                      HAEPrecision = 0.0, HAERecall = 0.0, HAEF1 = 0.0,
                                      TPTPrecision = 0.0, TPTRecall = 0.0, TPTF1 = 0.0;
        InstanceList [] trainTestSplits;
        InstanceList trainSplit, testSplit;
        ArrayList<InstanceList> arrayList  = new ArrayList<InstanceList>();
        MultiInstanceList multiList = null;
        Trial trial;
        while (cvIter.hasNext()){
            trainTestSplits = cvIter.nextSplit();
            arrayList.add(trainTestSplits[0]);
            arrayList.add(wpInstances);
            multiList = new MultiInstanceList(arrayList);
            trainSplit = multiList;
            testSplit = trainTestSplits[1];
//            classifier = trainer.train(trainSplit);
            
            FeatureSelector fs = new FeatureSelector(new FeatureCounts.Factory(), 8.0);
            fs.selectFeaturesFor(trainSplit);
//            fs.selectFeaturesFor(testSplit);
            FeatureSelectingClassifierTrainer c = new FeatureSelectingClassifierTrainer(trainer, fs);
            classifier = c.train(trainSplit);
            
            trial = new Trial(classifier, testSplit);
            arrayList.clear();
            
            averageAccuracy += trial.getAccuracy();
//            CGXPrecision += trial.getPrecision("CGX");
//            CGXRecall += trial.getRecall("CGX");
//            CGXF1 += trial.getF1("CGX");
            ECSPrecision += trial.getPrecision("ECS");
            ECSRecall += trial.getRecall("ECS");
            ECSF1 += trial.getF1("ECS");
            EPIPrecision += trial.getPrecision("EPI");
            EPIRecall += trial.getRecall("EPI");
            EPIF1 += trial.getF1("EPI");
            HAEPrecision += trial.getPrecision("HAE");
            HAERecall += trial.getRecall("HAE");
            HAEF1 += trial.getF1("HAE");
            TPTPrecision += trial.getPrecision("TPT");
            TPTRecall += trial.getRecall("TPT");
            TPTF1 += trial.getF1("TPT");
                        
            Iterator t = testSplit.iterator();
            Instance instance = null;
            while (t.hasNext()){
               instance = (Instance)t.next();
               System.out.println("FOLD NUMBER: " + foldCounter + " INSTANCE: " + instance.getName());
            }
            System.out.println("FOLD NUMBER: " + foldCounter + " ACCURACY: " + trial.getAccuracy());
            foldCounter++;
        }
//        System.out.println("CorporateGovernance: " + "Precision: " + (CGXPrecision / foldNum)+ " Recall:" + (CGXRecall / foldNum) + " F1:" + (CGXF1 / foldNum));
        System.out.println("EducationCultureAndSport: " + "Precision: " + (ECSPrecision / foldNum)+ " Recall:" + (ECSRecall / foldNum) + " F1:" + (ECSF1 / foldNum));
        System.out.println("EnterprisePlanningAndInfrastructure: " + "Precision: " + (EPIPrecision / foldNum)+ " Recall:" + (EPIRecall / foldNum) + " F1:" + (EPIF1 / foldNum));
        System.out.println("HousingAndEnvironment: " + "Precision: " + (HAEPrecision / foldNum)+ " Recall:" + (HAERecall / foldNum) + " F1:" + (HAEF1 / foldNum));
        System.out.println("Transport: " + "Precision: " + (TPTPrecision / foldNum)+ " Recall:" + (TPTRecall / foldNum) + " F1:" + (TPTF1 / foldNum));
        System.out.println("Overall Accuracy: " + averageAccuracy / foldNum);
    }
    
    public void evaluate(Classifier classifier, File file, Pipe pipe) throws IOException {

        // Create an InstanceList that will contain the test data.                                         
        // In order to ensure compatibility, process instances                                             
        //  with the pipe used to process the original training                                            
        //  instances.                                                                                     

        InstanceList testInstances = new InstanceList(pipe);

        // Create a new iterator that will read raw instance data from                                     
        //  the lines of a file.                                                                           
        // Lines should be formatted as:                                                                   
        //                                                                                                 
        //   [name] [label] [data ... ]                                                                    

        CsvIterator reader =
            new CsvIterator(new FileReader(file),
                            "(\\w+)\\s+(\\w+)\\s+(.*)",
                            3, 2, 1);  // (data, label, name) field indices               

        // Add all instances loaded by the iterator to                                                     
        //  our instance list, passing the raw input data                                                  
        //  through the classifier's original input pipe.                                                  

        testInstances.addThruPipe(reader);

        Trial trial = new Trial(classifier, testInstances);

        // The Trial class implements many standard evaluation                                             
        //  metrics. See the JavaDoc API for more details.                                                 
         
        System.out.println("Accuracy: " + trial.getAccuracy());

	// precision, recall, and F1 are calcuated for a specific                                          
        //  class, which can be identified by an object (usually                                           
	//  a String) or the integer ID of the class                                                       

//        System.out.println("F1 for class 'good': " + trial.getF1("good"));
//
//        System.out.println("Precision for class '" +
//                           classifier.getLabelAlphabet().lookupLabel(1) + "': " +
//                           trial.getPrecision(1));
        
//        System.out.println("EducationCultureAndSport" + " Precision: " + trial.getPrecision("EducationCultureAndSport")
//                                                     + " Recall: " + trial.getRecall("EducationCultureAndSport")
//                                                     + " F1: " + trial.getF1("EducationCultureAndSport"));
        
        // Print some evaluation measures
        Object classes [] = testInstances.getTargetAlphabet().toArray();
        for (int i = 0; i < classes.length; i++){
//            System.out.println(classes[i].toString() + " Precision: " + trial.getPrecision(classes[i])
//                                                     + " Recall: " + trial.getRecall(classes[i])
//                                                     + " F1: " + trial.getF1(classes[i]));
            System.out.println(classifier.getLabelAlphabet().lookupLabel(i) + 
                    " Precision: " + trial.getPrecision(i) +
                    " Recall: " + trial.getRecall(i) +
                    " F1: " + trial.getF1(i));
        }
    }
    
    public void manualAccuracy(Classifier classifier, String fileName){
        int counter = 0;
        int lineCounter = 0;
        double avg;
        try{
            BufferedReader in = new BufferedReader(new FileReader(fileName));
            while (in.ready()) {
                String s = in.readLine();
                lineCounter++;
                Classification c = classifier.classify(s);             
                if (s.contains(c.getLabeling().getBestLabel().toString()))
                    counter++;
            }
            avg = (double)counter / (double)lineCounter;
            System.out.println("Accuracy: " + avg);
        }catch(IOException e){}
    }
}