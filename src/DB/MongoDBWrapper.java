/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DB;

/**
 *
 * @author Karim
 */

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.util.JSON;
import twitter4j.json.DataObjectFactory;

import java.net.UnknownHostException;
import java.util.*;

public class MongoDBWrapper {
    private Mongo mongo;
    private DB mongoDB;
    private DBCollection coll;

    public MongoDBWrapper() {
        try{
            mongo = new Mongo("localhost");
            mongoDB = mongo.getDB("twitter_mining");
            coll = mongoDB.getCollection("tweets");
        }catch(UnknownHostException e){e.printStackTrace();}
    }
    
    public void insertTweet(String tweetId, String tweetText, Date tweetDate, String tweetSentimentScore, String tweetCategory, Double tweetLatitude, 
                            Double tweetLongitude, String userID, String userScreenName, String userLocation, int userFollowers, boolean userVerified,
                            List hashtagList, List urlList){
        
        int tweetSentimentScoreInt = Integer.parseInt(tweetSentimentScore.substring(tweetSentimentScore.length()-2).trim());
        String tweetSentiment;

        if(tweetSentimentScoreInt > 0)
            tweetSentiment = "positive";
        else if(tweetSentimentScoreInt < 0)
            tweetSentiment = "negative";
        else
            tweetSentiment = "neutral";

        DBObject dbObj = new BasicDBObject();
        dbObj.put("_id", tweetId);
        dbObj.put("text", tweetText);
        dbObj.put("date", tweetDate);
        dbObj.put("sentimentScore", tweetSentimentScoreInt);
        dbObj.put("service", tweetCategory);
        dbObj.put("latitude", tweetLatitude);
        dbObj.put("longitude", tweetLongitude);
        dbObj.put("userID", userID);
        dbObj.put("userScreenName", userScreenName);
        dbObj.put("userLocation", userLocation);
        dbObj.put("userFollowers", userFollowers);
        dbObj.put("userVarified", userVerified);
        dbObj.put("hashtagList", hashtagList);
        dbObj.put("urlList", urlList);
        coll.insert(dbObj); 

//        String tweet = DataObjectFactory.getRawJSON(status);
//        DBObject doc = (DBObject)JSON.parse(tweet);
//        coll.insert(doc);
    }
}