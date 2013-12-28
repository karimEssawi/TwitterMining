/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Mallet;

import java.io.*;
/**
 *
 * @author Karim
 */
public class ConstructDicrectory {
    String fileName;
    
    public ConstructDicrectory(String fileName){
        this.fileName = fileName;
    }
    
    public void construct () throws IOException{
        try{
            BufferedReader in = new BufferedReader(new FileReader(fileName));
//            PrintWriter SocialCareWriter = new PrintWriter("ACCDirectoryNew\\Testing\\Social care and wellbeing\\Social care and wellbeing.txt");
            PrintWriter HousingEnvironmentWriter = new PrintWriter("ACCDirectoryNew\\Testing\\HAE\\Housing and Environment.txt");
            PrintWriter EnterpriseWriter = new PrintWriter("ACCDirectoryNew\\Testing\\EPI\\Enterprise Planning and Infrastructure.txt");
            PrintWriter EducationSportWriter = new PrintWriter("ACCDirectoryNew\\Testing\\ECS\\Education Culture and Sport.txt");
            PrintWriter CorporateWriter = new PrintWriter("ACCDirectoryNew\\Testing\\CGX\\Corporate Governance.txt");
            PrintWriter TransportWriter = new PrintWriter("ACCDirectoryNew\\Testing\\TPT\\Transport.txt");

            while (in.ready()) {
                String s = in.readLine();
//                if (s.endsWith("SCW"))
//                    SocialCareWriter.println(s + "\n");
                 if (s.trim().endsWith("HAE"))
                    HousingEnvironmentWriter.println(s + "\n");
                else if (s.trim().endsWith("EPI"))
                    EnterpriseWriter.print(s + "\n");
                else if (s.trim().endsWith("ECS"))
                    EducationSportWriter.print(s + "\n");
                else if (s.trim().endsWith("CGX"))
                    CorporateWriter.print(s + "\n");
                else if (s.trim().endsWith("TPT"))
                    TransportWriter.print(s + "\n");
            }
            in.close();
//            SocialCareWriter.flush();
//            SocialCareWriter.close();
            HousingEnvironmentWriter.flush();
            HousingEnvironmentWriter.close();
            EnterpriseWriter.flush();
            EnterpriseWriter.close();
            EducationSportWriter.flush();
            EducationSportWriter.close();
            CorporateWriter.flush();
            CorporateWriter.close();
            TransportWriter.flush();
            TransportWriter.close();
        }catch(IOException e){}
    }
    
    public static void main(String[] args) throws IOException{
        new ConstructDicrectory("LabeledTweets.txt").construct();
    }
}
