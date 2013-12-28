/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Mallet;

import cc.mallet.classify.*;
import java.io.*;


/**
 *
 * @author Karim
 */
public class MalletMain {
    
    private Serialize serialize = new Serialize();
    private Classifier serviceClassifier;
    private Classifier relevantClassifier;
    private Classification classification;
    private String serviceClassifierPath;
    private String relevantClassifierPath;
        
    public void setRelevantClassifierPath(String relevantClassifierPath) {
        this.relevantClassifierPath = relevantClassifierPath;
        try{
            relevantClassifier = serialize.loadClassifier(new File(this.relevantClassifierPath));
        }catch(ClassNotFoundException| IOException e){e.printStackTrace();}    
    }
    
    public String classifyRelevant(String text){
        classification = relevantClassifier.classify(text);
        return classification.getLabeling().getBestLabel().toString();
    }
    
    public void setServiceClassifierPath(String serviceClassifierPath) {
        this.serviceClassifierPath = serviceClassifierPath;
        try{
            serviceClassifier = serialize.loadClassifier(new File(this.serviceClassifierPath));
        }catch(ClassNotFoundException| IOException e){e.printStackTrace();}    
    }
    
    public String classifyService(String text){
        classification = serviceClassifier.classify(text);
        return classification.getLabeling().getBestLabel().toString();
    }
}
