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

public class SentiStrengthApp {
    
    private SentiStrength sentiStrengthBinary = new SentiStrength();
    private SentiStrength sentiStrengthScale = new SentiStrength();
    private SentiStrength sentiStrengthTrinary = new SentiStrength();
    
    public String binaryDetection(String text){
        //Create an array of command line parameters to send (not text or file to process)
        String ssthInitialisation[] = {"sentidata", "SentStrength_Data/",  "mood", "0", "binary"};
        sentiStrengthBinary.initialise(ssthInitialisation); //Initialise
        return sentiStrengthBinary.computeSentimentScores(text);
    }
    
    public String scaleDetection(String text){
        //Create an array of command line parameters to send (not text or file to process)
        String ssthInitialisation[] = {"sentidata", "SentStrength_Data/",  "mood", "0", "scale"};
        sentiStrengthScale.initialise(ssthInitialisation); //Initialise
        return sentiStrengthScale.computeSentimentScores(text);
    }
    
    public String subjectivityDetection(String text){
        //Create an array of command line parameters to send (not text or file to process)
        String ssthInitialisation[] = {"sentidata", "SentStrength_Data/", "mood", "0", "trinary"};
        sentiStrengthTrinary.initialise(ssthInitialisation); //Initialise
        return sentiStrengthTrinary.computeSentimentScores(text);
    }
}
