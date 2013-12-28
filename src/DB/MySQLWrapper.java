/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DB;

/**
 *
 * @author Karim
 */
import java.sql.*;
import java.util.ArrayList;

public class MySQLWrapper{
    
    private Connection con;
    
    public MySQLWrapper(String dbDriver, String dbUrl){
        try{
            //Register the JDBC driver for the database.
            Class.forName(dbDriver).newInstance();
            
            //Get a connection to the database(URL is the url of the database server)
            con = DriverManager.getConnection(dbUrl,"ACC", "AberdeenCC");
        }catch(ClassNotFoundException| IllegalAccessException| InstantiationException| SQLException e){e.printStackTrace();}
    }
    
    public boolean insertTweet(String tweetId, String tweetText, Date tweetDate,String tweetSentimentScore, String tweetCategory, Double tweetLatitude, Double tweetLongitude, String userId){
        PreparedStatement insert;
        String query;
        boolean inserted = false;
        int tweetSentimentScoreInt;
//        String tweetSentiment;
        
        tweetText = tweetText.replace("'", "");
        tweetText = tweetText.replace("\"", "");
        tweetText = tweetText.replace(";", ",");
        
        tweetSentimentScoreInt = Integer.parseInt(tweetSentimentScore.substring(tweetSentimentScore.length()-2).trim());      
        
//        if(tweetSentimentScore.equals("0"))
//            tweetSentimentScore = "-1";
//        if (tweetSentimentScoreInt >= 1)
//            tweetSentiment = "positive";
//        if (tweetSentimentScoreInt < 0)
//        else
//            tweetSentiment = "negative";
        
        try{
            if (tweetLatitude == null || tweetLongitude == null){
                query = "INSERT INTO tweet(id, text, date, sentiment_score, service, user_id) VALUES "
                           + "('" + tweetId + "','" + tweetText + "','" + tweetDate + "','" + tweetSentimentScoreInt + 
                             "','" + tweetCategory + "','" + userId + "');";
            }
            else{
                query = "INSERT INTO tweet(id, text, date, sentiment_score, service, latitude, longitude, user_id) VALUES "
                           + "('" + tweetId + "','" + tweetText + "','" + tweetDate + "','" + tweetSentimentScoreInt + 
                             "','" + tweetCategory + "','" + tweetLatitude + "','" + tweetLongitude + "','" + userId + "');";
            }
            insert = con.prepareStatement(query);
            inserted = insert.execute();
//            insert.clocloseOnCompletion();
            
//            if(result > 0)
//               inserted = true; 
        }catch(SQLException e){e.printStackTrace();}
        
        return inserted;
    }
    
    public boolean insertHashtag(String hashtag, String tweetID){
        Statement select;
        PreparedStatement insert;
        ResultSet rset;
        ArrayList hashtagList = new ArrayList();
        boolean inserted = false;
        
        try{
            select = con.createStatement();
            rset = select.executeQuery("select hashtag from hashtag;");
                while(rset.next()){
                    hashtagList.add(rset.getString(1));
            }

            if(!hashtagList.contains(hashtag)){
                String query = "INSERT INTO hashtag(hashtag) VALUES "
                               + "('" + hashtag + "');";
                insert = con.prepareStatement(query);
                inserted = insert.execute();

                rset = select.executeQuery("select max(id) from hashtag;");
                if(rset.next())
                    insertTweetHashtag(tweetID, Integer.parseInt(rset.getString(1)));
            }
            else{
                rset = select.executeQuery("select id from hashtag where hashtag = '" + hashtag + "';");
                if(rset.next())
                    inserted = insertTweetHashtag(tweetID, Integer.parseInt(rset.getString(1)));
            }   
            
        }catch(SQLException e){e.printStackTrace();}
        
        return inserted;
    }
    
    public boolean insertTweetHashtag(String tweetID, int hashtagID){
        PreparedStatement insert;
        boolean inserted = false;
        
        try{
            String query = "INSERT INTO tweet_hashtag VALUES "
                               + "('" + tweetID + "','" + hashtagID + "');";
            insert = con.prepareStatement(query);
            inserted = insert.execute();
        }catch(SQLException e){e.printStackTrace();} 
        
        return inserted;
    }
    
    public boolean insertURL(String url, String tweetID){
        Statement select;
        PreparedStatement insert;
        ResultSet rset;
        ArrayList urlList = new ArrayList();
        boolean inserted = false;
                
        try{
            select = con.createStatement();
            rset = select.executeQuery("select url from url;");
                while(rset.next()){
                    urlList.add(rset.getString(1));
            }

            if(!urlList.contains(url)){
                String query = "INSERT INTO url(url) VALUES "
                               + "('" + url + "');";
                insert = con.prepareStatement(query);
                inserted = insert.execute();

                rset = select.executeQuery("select max(id) from url;");
                if(rset.next())
                    insertTweetURL(tweetID, Integer.parseInt(rset.getString(1)));
            }
            else{
                rset = select.executeQuery("select id from url where url = '" + url + "';");
                if(rset.next())
                    inserted = insertTweetURL(tweetID, Integer.parseInt(rset.getString(1)));
            }
            
        }catch(SQLException e){e.printStackTrace();}
        
        return inserted;
    }
    
    public boolean insertTweetURL(String tweetID, int urlID){
        PreparedStatement insert;
        boolean inserted = false;
        
        try{
            String query = "INSERT INTO tweet_url VALUES "
                               + "('" + tweetID + "','" + urlID + "');";
            insert = con.prepareStatement(query);
            inserted = insert.execute();
        }catch(SQLException e){e.printStackTrace();} 
        
        return inserted;
    }
    
    public boolean insertUser(String userId, String userScreenName, String userLocation, int userFollowers, boolean isVerified){
        Statement select;
        PreparedStatement insert;
        ResultSet rset;
        ArrayList userList = new ArrayList();
        boolean inserted = false;
        String verified;
        
        userScreenName = userScreenName.replace("'", "");
        userScreenName = userScreenName.replace("\"", "");
        
        userLocation = userLocation.replace("'", "");
        userLocation = userLocation.replace("\"", "");
        
        if(isVerified == true)
            verified = "Y";
        else
            verified = "N";
                
        try{
            select = con.createStatement();
            rset = select.executeQuery("select id from user;");
                while(rset.next()){
                    userList.add(rset.getString(1));
            }

            if(!userList.contains(userId)){
                String query = "INSERT INTO user VALUES "
                               + "('" + userId + "','" + userScreenName + "','" + userLocation + "','" + userFollowers + "','" + verified + "');";
                insert = con.prepareStatement(query);
                inserted = insert.execute();    
            }
        }catch(SQLException e){e.printStackTrace();} 
        
        return inserted;
    }
}