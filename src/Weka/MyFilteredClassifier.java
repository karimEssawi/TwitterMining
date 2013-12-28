/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Weka;

/**
 *
 * @author Karim
 */
import weka.core.*;
import weka.core.FastVector;
import weka.classifiers.meta.FilteredClassifier;
import java.io.*;
import weka.classifiers.meta.AttributeSelectedClassifier;

/**
* This class implements a simple text classifier in Java using WEKA.
* It loads a file with the text to classify, and the model that has been
* learnt with MyFilteredLearner.java.
* @author Jose Maria Gomez Hidalgo - http://www.esp.uem.es/jmgomez
* @see MyFilteredLearner
*/
 public class MyFilteredClassifier {

        /**
         * String that stores the text to classify
         */
        String text;
        /**
         * Object that stores the instance.
         */
        Instances instances;
        /**
         * Object that stores the classifier.
         */
        FilteredClassifier classifier;
                
        /**
         * This method loads the text to be classified.
         * @param fileName The name of the file that stores the text.
         */
        public void loadFromFile(String fileName) {
                try {
                        BufferedReader reader = new BufferedReader(new FileReader(fileName));
                        String line;
                        text = "";
                        while ((line = reader.readLine()) != null) {
                text = text + " " + line;
            }
                        System.out.println("===== Loaded text data: " + fileName + " =====");
                        reader.close();
//                        System.out.println(text);
                }
                catch (IOException e) {
                        System.out.println("Problem found when reading: " + fileName);
                }
        }
        
        public void load(String text){
            this.text = text;
        }
                        
        /**
         * This method loads the model to be used as classifier.
         * @param fileName The name of the file that stores the text.
         */
        public void loadModel(String fileName) {
                try {
                    ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileName));
                    Object tmp = in.readObject();
                    classifier = (FilteredClassifier) tmp;
                    in.close();
//                    System.out.println("===== Loaded model: " + fileName + " =====");
                }
                catch (Exception e) {
                    // Given the cast, a ClassNotFoundException must be caught along with the IOException
                    System.out.println("Problem found when reading: " + fileName);
                }
        }
        
        /**
         * This method creates the instance to be classified, from the text that has been read.
         */
        public void makeInstance() {
                // Create the attributes, class and text
                FastVector fvNominalVal = new FastVector(2);
                fvNominalVal.addElement("relevant");
                fvNominalVal.addElement("irrelevant");

                Attribute attribute1 = new Attribute("class", fvNominalVal);
                Attribute attribute2 = new Attribute("text",(FastVector) null);
                
                // Create list of instances with one element
                FastVector fvWekaAttributes = new FastVector(2);
                fvWekaAttributes.addElement(attribute1);
                fvWekaAttributes.addElement(attribute2);
                instances = new Instances("Test relation", fvWekaAttributes, 1);
                
                // Set class index
                instances.setClassIndex(0);
                
                // Create and add the instance
//                DenseInstance instance = new DenseInstance(2);
                Instance instance = new Instance(2);
                instance.setValue(attribute2, text);
                // Another way to do it:
                // instance.setValue((Attribute)fvWekaAttributes.elementAt(1), text);
//                instance.setDataset(instances);
                instances.add(instance);
//                 System.out.println("===== Instance created with reference dataset =====");
//                System.out.println(instances);
        }
        
        /**
         * This method performs the classification of the instance.
         * Output is done at the command-line.
         */
        public String classify() {
            double pred = 0.0;
                try {
                        pred = classifier.classifyInstance(instances.instance(0));
//                        System.out.println("===== Classified instance =====");
//                        System.out.println("Class predicted: " + instances.classAttribute().value((int) pred));
                }
                catch (Exception e) {
                        e.printStackTrace();
                }
                
                return instances.classAttribute().value((int) pred);
        }
        
        /**
         * Main method. It is an example of the usage of this class.
         * @param args Command-line arguments: fileData and fileModel.
         */
        public static void main (String[] args) {
             MyFilteredClassifier classifier = new MyFilteredClassifier();
             classifier.loadFromFile("new3.txt");
             classifier.loadModel("wekaBinaryClassifier.model");
             classifier.makeInstance();
             classifier.classify();
        }
        
        public String classifyTweet(String text){
            MyFilteredClassifier classifier = new MyFilteredClassifier();
            classifier.load(text);
            classifier.loadModel("wekaBinaryClassifier.model");
            classifier.makeInstance();
            
            return  classifier.classify(); 
        }
}        