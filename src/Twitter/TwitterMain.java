/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Twitter;

/**
 *
 * @author Karim
 */
import SentiStrength.*;
import DB.*;
import Mallet.*;
import twitter4j.*;
import twitter4j.json.DataObjectFactory;
import Weka.*;

import java.io.*;
import java.util.*;
import java.net.URISyntaxException;
import java.sql.Date;

public class TwitterMain{
    
    private TwitterMiner twitterMiner;
//    private SentiStrengthTrinary sentiStrength;
//    private SentiStrengthScale sentiStrengthScale;
//    private SentiStrengthBinary sentiStrengthBinary;
    private SentiStrengthApp sentiStrength;
    private MalletMain mallet;
    private MySQLWrapper mySQL;
    private MongoDBWrapper mongoDB;
    private MyFilteredClassifier relevantClassifier;
    
    private String tweetId;
    private String tweetText;
    private java.util.Date tweetDate;
    private java.sql.Date tweetDateMySQL;
    private String tweetSentimentScore;
    private String tweetCategory;
    private Double tweetLatitude;
    private Double tweetLongitude;
    
    public TwitterMain(String dbDriver, String dbUrl, String relevantClassifierPath, String serviceClassifierPath){
        sentiStrength = new SentiStrengthApp();
        mallet = new MalletMain();
        mallet.setRelevantClassifierPath(relevantClassifierPath);
        mallet.setServiceClassifierPath(serviceClassifierPath);
        mySQL = new MySQLWrapper(dbDriver, dbUrl);
       mongoDB = new MongoDBWrapper();
    }
   
    public void getTweets(){  
     
        StatusListener listener = new StatusListener() { 
        @Override
        public void onStatus(Status status) {
            String hashtag="";
            HashtagEntity[] hashTagEntity;
            URLEntity[] urlEntity;
            List hashtagList;
            List urlList;
            String geo = "N/A";

            if (!status.isRetweet() && status.getUser().getLang().equalsIgnoreCase("en")){ 
                try {
                    if(!sentiStrength.subjectivityDetection(status.getText()).endsWith("0") && /*mallet.classifyRelevant(status.getText()).equals("relevant") &&*/ !status.getText().contains("Job") && !status.getText().toLowerCase().contains("#job") && !status.getText().toLowerCase().contains("#jobs")){
                        if(status.getGeoLocation() != null)
                            geo = status.getGeoLocation().toString();

                        System.out.println( status.getCreatedAt() + " - " + status.getText() + " - " + mallet.classifyRelevant(status.getText()));

                        Util.writeStringToFile("C:\\Users\\Karim\\Documents\\NetBeansProjects\\AllTweets.txt",
                            status.getCreatedAt() + " | Retweets: " + status.getRetweetCount() + " | " 
                            + "Followers: " + status.getUser().getFollowersCount() + " | " 
                            + status.getText() + " | " + geo + "\n");

                        tweetId = String.valueOf(status.getId());
                        tweetText = status.getText();
                        tweetDate = status.getCreatedAt();
                        tweetDateMySQL = new java.sql.Date(status.getCreatedAt().getTime());
                        if(sentiStrength.scaleDetection(tweetText).endsWith("0"))
                            tweetSentimentScore = sentiStrength.binaryDetection(tweetText);
                        else
                            tweetSentimentScore = sentiStrength.scaleDetection(tweetText);        
                        tweetCategory = mallet.classifyService(tweetText);
                        if(status.getGeoLocation()!= null){
                            tweetLatitude = status.getGeoLocation().getLatitude();
                            tweetLongitude = status.getGeoLocation().getLongitude();
                        }
                        else{
                            tweetLatitude = null;
                            tweetLongitude = null;
                        }
                        
                        urlEntity = status.getURLEntities();
                        hashTagEntity = status.getHashtagEntities();
                        
                        if(hashTagEntity.length == 0)
                            hashtagList = null;
                        else{
                            hashtagList = new ArrayList<>();
                            for (int i=0; i < hashTagEntity.length; i++)
                                hashtagList.add(hashTagEntity[i].getText());
                        }
                        
                        if(urlEntity.length == 0)
                            urlList = null;
                        else{
                            urlList = new ArrayList<>();
                            for (int i=0; i < urlEntity.length; i++)
                                urlList.add(urlEntity[i].getExpandedURL());
                        }
                        if(mallet.classifyRelevant(tweetText).equals("relevant")){
                        mySQL.insertUser(String.valueOf(status.getUser().getId()), status.getUser().getScreenName(), status.getUser().getLocation(), status.getUser().getFollowersCount(), status.getUser().isVerified());
                        mySQL.insertTweet(tweetId, tweetText, tweetDateMySQL, tweetSentimentScore, tweetCategory, (Double)tweetLatitude, (Double)tweetLongitude, String.valueOf(status.getUser().getId()));
                        
                        for (int i=0; i < hashTagEntity.length; i++)
                            mySQL.insertHashtag(hashTagEntity[i].getText(), tweetId);
                        
                        for (int i=0; i < urlEntity.length; i++)
                            mySQL.insertURL(urlEntity[i].getExpandedURL(), tweetId);
                        
                        mongoDB.insertTweet(tweetId, tweetText, tweetDate, tweetSentimentScore, tweetCategory, tweetLatitude, tweetLongitude, 
                                            String.valueOf(status.getUser().getId()), status.getUser().getScreenName(), status.getUser().getLocation(), 
                                            status.getUser().getFollowersCount(), status.getUser().isVerified(), hashtagList, urlList);
                        }
                    }  
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
            System.out.println("Got a status deletion notice id:" + statusDeletionNotice.getStatusId());
        }

        @Override
        public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
            System.out.println("Got track limitation notice:" + numberOfLimitedStatuses);
        }

        @Override
        public void onScrubGeo(long userId, long upToStatusId) {
            System.out.println("Got scrub_geo event userId:" + userId + " upToStatusId:" + upToStatusId);
        }

        @Override
        public void onStallWarning(StallWarning warning) {
            System.out.println("Got stall warning:" + warning);
        }

        @Override
        public void onException(Exception ex) {
            ex.printStackTrace();
        }

        };
        
        twitterMiner = new TwitterMiner(listener);
    }
    
    public static void main(String[] args) throws URISyntaxException{
        TwitterMain t = new TwitterMain("com.mysql.jdbc.Driver", "jdbc:mysql://localhost:3306/twittermining", "relevantClassifier.mallet", "serviceClassifier.mallet");
        t.getTweets();     
    }
}
