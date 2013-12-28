/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Mallet;

import cc.mallet.classify.Classifier;
import java.io.*;

/**
 *
 * @author Karim
 */
public class Serialize implements Serializable{
    
    public void saveClassifier(Classifier classifier, File serializedFile) throws IOException {

        // The standard method for saving classifiers in                                                   
        //  Mallet is through Java serialization. Here we                                                  
        //  write the classifier object to the specified file.                                             

        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream (serializedFile));
        oos.writeObject (classifier);
        oos.close();
    }
    
    public Classifier loadClassifier(File serializedFile) throws FileNotFoundException, IOException, ClassNotFoundException {

        // The standard way to save classifiers and Mallet data                                            
        //  for repeated use is through Java serialization.                                                
        // Here we load a serialized classifier from a file.                                               

        Classifier classifier;

        ObjectInputStream ois = new ObjectInputStream (new FileInputStream (serializedFile));
        classifier = (Classifier) ois.readObject();
        ois.close();

        return classifier;
    }
}
