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
public class ConstructTweet {
    
    String fileName;
    int HAECounter = 1;
    int TPTCounter = 1;
    int CGXCounter = 1;
    int EPICounter = 1;
    int ECSCounter = 1;
    
    public ConstructTweet(String fileName){
        this.fileName = fileName;
    }
    
    public void construct () throws IOException{
        try{
            BufferedReader in = new BufferedReader(new FileReader(fileName));
//            PrintWriter SocialCareWriter = new PrintWriter("ACCDirectoryNew\\Testing\\Social care and wellbeing\\Social care and wellbeing.txt");
            PrintWriter HousingEnvironmentWriter;
            PrintWriter EnterpriseWriter;
            PrintWriter EducationSportWriter;
            PrintWriter CorporateWriter;
            PrintWriter TransportWriter;
            
            while (in.ready()) {
                String s = in.readLine();
                 if (s.trim().contains("HousingAndEnvironment")){
                      HousingEnvironmentWriter = new PrintWriter("DirectoryHoldOut\\Testing\\HAE\\HAE_" + HAECounter + ".txt");
                      HousingEnvironmentWriter.println(s.replace("HousingAndEnvironment", ""));
                      HousingEnvironmentWriter.flush();
                      HousingEnvironmentWriter.close();
                      HAECounter++;
                 }
                 if (s.trim().contains("Transport")){
                      TransportWriter = new PrintWriter("DirectoryHoldOut\\Testing\\TPT\\TPT_" + TPTCounter + ".txt");
                      TransportWriter.println(s.replace("Transport", ""));
                      TransportWriter.flush();
                      TransportWriter.close();
                      TPTCounter++;
                 }
                 if (s.trim().contains("CorporateGovernance")){
                      CorporateWriter = new PrintWriter("DirectoryHoldOut\\Testing\\CGX\\CGX_" + CGXCounter + ".txt");
                      CorporateWriter.println(s.replace("CorporateGovernance", ""));
                      CorporateWriter.flush();
                      CorporateWriter.close();
                      CGXCounter++;
                 }
                 if (s.trim().contains("EnterprisePlanningAndInfrastructure")){
                      EnterpriseWriter = new PrintWriter("DirectoryHoldOut\\Testing\\EPI\\EPI_" + EPICounter + ".txt");
                      EnterpriseWriter.println(s.replace("EnterprisePlanningAndInfrastructure", ""));
                      EnterpriseWriter.flush();
                      EnterpriseWriter.close();
                      EPICounter++;
                 }
                  if (s.trim().contains("ECS")){
                      EducationSportWriter = new PrintWriter("DirectoryHoldOut\\Testing\\ECS\\ECS_" + ECSCounter + ".txt");
                      EducationSportWriter.println(s.replace("EducationCultureAndSport", ""));
                      EducationSportWriter.flush();
                      EducationSportWriter.close();
                      ECSCounter++;
                 }
            }
        }catch(IOException e) {e.printStackTrace();}
    }
    
    public static void main(String[] args) throws IOException{
        new ConstructTweet("HoldOutTweets.txt").construct();
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author Karim
 */

 class ConstructTweets {
    
    String fileName;
    int HAECounter = 1;
    int TPTCounter = 1;
    int CGXCounter = 1;
    int EPICounter = 1;
    int ECSCounter = 1;
    
    public ConstructTweets(String fileName){
        this.fileName = fileName;
    }
    
    public void construct () throws IOException{
        try{
            BufferedReader in = new BufferedReader(new FileReader(fileName));
//            PrintWriter SocialCareWriter = new PrintWriter("ACCDirectoryNew\\Testing\\Social care and wellbeing\\Social care and wellbeing.txt");
            PrintWriter HousingEnvironmentWriter = new PrintWriter("Directory\\HAE\\HAETweets.txt");
            PrintWriter EnterpriseWriter = new PrintWriter("Directory\\EPI\\EPITweets.txt");
            PrintWriter EducationSportWriter = new PrintWriter("Directory\\ECS\\ECSTweets.txt");
            PrintWriter CorporateWriter = new PrintWriter("Directory\\CGX\\CGXTweets.txt");
            PrintWriter TransportWriter = new PrintWriter("Directory\\TPT\\TPTTweets.txt");
            
            while (in.ready()) {
                String s = in.readLine();
                 if (s.trim().endsWith("HAE")){
//                      HousingEnvironmentWriter = new PrintWriter("Directory\\HAE\\HAE_" + HAECounter + ".txt");
                      HousingEnvironmentWriter.println(s);
                      
                      HAECounter++;
                 }
                 if (s.trim().endsWith("TPT")){
//                      TransportWriter = new PrintWriter("Directory\\TPT\\TPT_" + TPTCounter + ".txt");
                      TransportWriter.println(s);
                      
                      TPTCounter++;
                 }
                 if (s.trim().endsWith("CGX")){
//                      CorporateWriter = new PrintWriter("Directory\\CGX\\CGX_" + CGXCounter + ".txt");
                      CorporateWriter.println(s);
                      
                      CGXCounter++;
                 }
                 if (s.trim().endsWith("EPI")){
//                      EnterpriseWriter = new PrintWriter("Directory\\EPI\\EPI_" + EPICounter + ".txt");
                      EnterpriseWriter.println(s);
                      
                      EPICounter++;
                 }
                  if (s.trim().endsWith("ECS")){
//                      EducationSportWriter = new PrintWriter("Directory\\ECS\\ECS_" + ECSCounter + ".txt");
                      EducationSportWriter.println(s);
                      
                      ECSCounter++;
                 }
            }
            EducationSportWriter.flush();
            EducationSportWriter.close();
            EnterpriseWriter.flush();
            EnterpriseWriter.close();
            CorporateWriter.flush();
            CorporateWriter.close();
            TransportWriter.flush();
            TransportWriter.close();
            HousingEnvironmentWriter.flush();
            HousingEnvironmentWriter.close();
        }catch(IOException e) {}
        
    }
    public static void main(String[] args) throws IOException{
        new ConstructTweets("TestDataFull.txt").construct();
    }
 }
    
class ConstructT{
        String fileName;
//    int HAECounter = 1;
//    int TPTCounter = 46;
//    int CGXCounter = 1;
//    int EPICounter = 1;
    int ECSCounter = 35;
    
//    PrintWriter HousingEnvironmentWriter;
//    PrintWriter EnterpriseWriter;
    PrintWriter EducationSportWriter;
//    PrintWriter CorporateWriter;
//    PrintWriter TransportWriter;
    public ConstructT(String fileName){
        this.fileName = fileName;
    }
    
    public void construct () throws IOException{
        try{
            BufferedReader in = new BufferedReader(new FileReader(fileName));
            while (in.ready()){
                String s = in.readLine();
                      EducationSportWriter = new PrintWriter("DirectoryCrossVal - Copy\\Tweets\\ECS\\ECS_" + ECSCounter + ".txt");
                      EducationSportWriter.println(s);
                      ECSCounter++;
                      EducationSportWriter.flush();
                      EducationSportWriter.close();
            }
        }catch(IOException e){}
    }
    public static void main(String[] args) throws IOException{
        new ConstructT("ECS.txt").construct();
    }
 }

class ConstructHoldOutTweets{
    
    String fileName;
    
    public ConstructHoldOutTweets(String fileName){
        this.fileName = fileName;
    }
    
    public void construct () throws IOException{
        PrintWriter writer = new PrintWriter("HoldOutTweets.txt");
        BufferedReader reader;
        
        try{
            File file = new File(fileName);
            if(file.isDirectory()){
                for(File f : file.listFiles()){
                    reader = new BufferedReader(new FileReader(f));
                    while (reader.ready()){
                        if(f.getName().startsWith("EPI"))
                            writer.println("EPI  " + reader.readLine());
                        if(f.getName().startsWith("HAE"))
                            writer.println("HAE  " + reader.readLine());
                        if(f.getName().startsWith("TPT"))
                            writer.println("TPT  " + reader.readLine());
                        if(f.getName().startsWith("ECS"))
                            writer.println("ECS  " + reader.readLine());
                        if(f.getName().startsWith("CGX"))
                            writer.println("CGX  " + reader.readLine());
                    }
                }
                writer.flush();
                writer.close();
            }
        }catch(IOException e){}
    }
    public static void main(String[] args) throws IOException{
        new ConstructHoldOutTweets("HoldOutTweets").construct();
    }
 }
    