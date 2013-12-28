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
public class ConstructTestData {
    
    String fileName;
    int counter = 1;
    public ConstructTestData(String fileName){
        this.fileName = fileName;
    }
    
    public void construct() throws IOException{
        try{
            BufferedReader in = new BufferedReader(new FileReader(fileName));
            PrintWriter fileWriter = new PrintWriter("TestDataFull.txt");
            
            while (in.ready()) {
                String s = in.readLine();
                if (s.trim().endsWith("HAE")){
                    fileWriter.println(counter + "  HAE  " + s);
                    counter++;
                }
                else if (s.trim().endsWith("EPI")){
                    fileWriter.println(counter + "  EPI  " + s);
                    counter++;
                }
                else if (s.trim().endsWith("ECS")){
                    fileWriter.println(counter + "  ECS  " + s);
                    counter++;
                }
                else if (s.trim().endsWith("CGX")){
                    fileWriter.println(counter + "  CGX  " + s);
                    counter++;
                }
                else if (s.trim().endsWith("TPT")){
                    fileWriter.println(counter + "  TPT  " + s);
                    counter++;
                }
            }
            in.close();
            fileWriter.flush();
            fileWriter.close();
        }catch(IOException e){}
    }
    
    public static void main(String[] args) throws IOException{
        new ConstructTestData("ACCTweets.txt").construct();
    }
}
