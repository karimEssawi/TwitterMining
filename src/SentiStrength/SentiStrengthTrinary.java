/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SentiStrength;

/**
 *
 * @author Karim
 */
import uk.ac.wlv.sentistrength.*;

public class SentiStrengthTrinary {
    
    private SentiStrength sentiStrength = new SentiStrength(); 
    
    public String subjectivityDetection(String text){
        //Create an array of command line parameters to send (not text or file to process)
        String ssthInitialisation[] = {"sentidata", "SentStrength_Data/", "mood", "0", "trinary"};
        sentiStrength.initialise(ssthInitialisation); //Initialise
        return sentiStrength.computeSentimentScores(text);
    }
    
//    public String binaryDetection(String text){
////        Create an array of command line parameters to send (not text or file to process)
//        String ssthInitialisation[] = {"sentidata", "SentStrength_Data/", "binary"};
//        sentiStrength.initialise(ssthInitialisation); //Initialise
//        return sentiStrength.computeSentimentScores(text);
//    }
    
    public String scaleDetection(String text){
//        Create an array of command line parameters to send (not text or file to process)
        String sssthInitialisation[] = {"sentidata", "SentStrength_Data/", "mood", "0", "scale"};
        sentiStrength.initialise(sssthInitialisation); //Initialise
        return sentiStrength.computeSentimentScores(text);
    }
}
