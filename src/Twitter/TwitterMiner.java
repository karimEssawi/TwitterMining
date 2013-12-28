/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
*/
package Twitter;

/**
*
* @author Karim
*/
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

public final class TwitterMiner{
    
    private ConfigurationBuilder cb = new ConfigurationBuilder();
    private TwitterStream twitterStream;
    private FilterQuery query;
    private String[] track;
    
    public TwitterMiner(StatusListener listener){
        cb.setDebugEnabled(true)
          .setOAuthConsumerKey("geqUHtPPPpOJ52LhMJkQQ")
          .setOAuthConsumerSecret("6nLkq6CUR8Ztid50zPwQ7cZMxQyWCTItLrMyLy9eJm8")
          .setOAuthAccessToken("2244034142-kk5VesFLgp4NsSPv2wORhwCK9jWdyqvy1hNFocP")
          .setOAuthAccessTokenSecret("OizuAMGvzyKv5nO9v6emGBPDLHebUWsSUN8WKbn7mTtG7");
        
        twitterStream = new TwitterStreamFactory(cb.build()).getInstance();
        twitterStream.addListener(listener);
        query = new FilterQuery();
        
        try{
            getStream();
        }catch(TwitterException e){e.printStackTrace();}
    }

    public void getStream()throws TwitterException{      
        track = Util.readKeyWords("keywords.txt");
        query.track(track);
        twitterStream.filter(query);
   }
}