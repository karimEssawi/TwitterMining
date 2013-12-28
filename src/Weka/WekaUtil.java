/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Weka;

import java.io.*;
import java.util.*;
/**
 *
 * @author Karim
 */
public class WekaUtil {

    public void getIrreleventTweets(){
        try{
            BufferedReader allTweetsReader = new BufferedReader(new FileReader("AllTweets.txt"));
            BufferedReader relevenTweetsReader = new BufferedReader(new FileReader("ReleventTweets.txt"));

            PrintWriter irreleventWriter = new PrintWriter("irrelevantTweets.txt");
            Set<String> set = new HashSet<String>();
            Iterator<String> it;
            int counter = 1;
            String s;
            String object;

            while(relevenTweetsReader.ready()){
                set.add(relevenTweetsReader.readLine());
            }
            relevenTweetsReader.close();
            
            it = set.iterator();
            for(int i = 0; i < 38097; i++){
                s = allTweetsReader.readLine();
                if((counter % 2) != 0){
                    if(it.hasNext()){
                        object = (String)it.next();
                        if(!object.equals(s))
                            irreleventWriter.println(s);
                    }
                    else{
                        it = set.iterator();
                    }
                }
                counter++;
            }
            allTweetsReader.close();
            irreleventWriter.flush();
            irreleventWriter.close();
            
        }catch(IOException e){e.printStackTrace();}
    }
    
    public void writeIrrelventTweets(){
        try{
            BufferedReader irreleventTweetsReader = new BufferedReader(new FileReader("irrelevantTweets.txt"));
            PrintWriter irreleventWriter = new PrintWriter("irrelevantTweetsNew.txt");
            String s;
            
            while(irreleventTweetsReader.ready()){
                s = irreleventTweetsReader.readLine();
                if(s.startsWith("Sun") || s.startsWith("Mon") || s.startsWith("Tue") || s.startsWith("Wed") || s.startsWith("Thu") || s.startsWith("Fri") || s.startsWith("Sat"))
                    irreleventWriter.println(s);
            }
            irreleventTweetsReader.close();
            irreleventWriter.flush();
            irreleventWriter.close();
            
        }catch(IOException e){e.printStackTrace();}
    }
    
    public void splitTweets(){
        try{
             BufferedReader irreleventTweetsReader = new BufferedReader(new FileReader("irrelevantTweets.txt"));
             PrintWriter irreleventWriter = new PrintWriter("irrelevantTweetsNew.txt");
             String s;
             while(irreleventTweetsReader.ready()){
                 s = irreleventTweetsReader.readLine();
                 String[] a = s.split("\\|");
                 irreleventWriter.println(a[3]);
//                 System.out.println(a[3]);
             }
             irreleventTweetsReader.close();
             irreleventWriter.flush();
             irreleventWriter.close();
             
        }catch(IOException e){e.printStackTrace();}
    }
    
    public void constructIrrelevantTweets(){
        int tweetCounter = 1;
        String s;
        try{
            BufferedReader reader = new BufferedReader(new FileReader("irrelevantTweetsProcessed.txt"));
            PrintWriter writer;
            
            while(reader.ready()){
                s = reader.readLine();
                writer = new PrintWriter("DirectoryIrrelevant\\Irrelevant\\Irrelevant_tweet_" + tweetCounter + ".txt");
                writer.println(s);
                writer.flush();
                writer.close();
                tweetCounter++;
            }
            reader.close();
            
        }catch(IOException e){e.printStackTrace();}
    }
    
    public static void main(String[] args){
        new WekaUtil().constructIrrelevantTweets();
    }
}
