package com.audi.quattro.controller;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.restfb.DefaultFacebookClient;
import com.restfb.Facebook;
import com.restfb.FacebookClient;
import com.restfb.json.JsonObject;
import com.restfb.types.User;
import com.sola.instagram.InstagramSession;
import com.sola.instagram.auth.InstagramAuthentication;
import com.sola.instagram.exception.InstagramException;

import twitter4j.Query;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	// Twitter properties
	@Value("${twitter.consumer_key}")
    private String twitterConsumerKey;
	
	@Value("${twitter.consumer_secret}")
    private String twitterConsumerSecret;
	
	@Value("${twitter.access_token}")
    private String twitterAccessToken;
	
	@Value("${twitter.access_token_secret}")
    private String twitterAccessTokenSecret;
	
	@Value("${twitter.username}")
	private String twitterUser;
	
	@Value("${twitter.hashtag}")
	private String twitterHashTag;
		
	// Facebook properties
	
	
	
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		logger.info("Welcome home! The client locale is {}.", locale);
		
		// get tweets from Twitter
    	try {
			model.addAttribute("tweets",  getTwitterFeeds(model, locale));
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (TwitterException e) {
			e.printStackTrace();			
		}
		
    	// get posts from facebook.
    	model.addAttribute("fbposts",  getFacebookFeeds());
    	
    	try {
			model.addAttribute("instauser", getInstagramFeeds());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
		return "home";
	}
	
	
	private List<Status> getTwitterFeeds(Model model, Locale locale) throws IllegalStateException, TwitterException {
		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
		
		String formattedDate = dateFormat.format(date);
		
		model.addAttribute("serverTime", formattedDate );
		
		Twitter twitter = new TwitterFactory().getInstance();
        		
		//	My Applications Consumer and Auth Access Token
        twitter.setOAuthConsumer(twitterConsumerKey, twitterConsumerSecret);
        twitter.setOAuthAccessToken(new AccessToken(twitterAccessToken, twitterAccessTokenSecret));
       
        Query query = new Query("from:" + twitterUser + " #" + twitterHashTag);
        model.addAttribute("screenName", twitter.getScreenName() );        	        	
        List<Status> tweets = twitter.search(query).getTweets();
        		
        return tweets;
	}
	
	private List<JsonObject> getFacebookFeeds() {
		
		FacebookClient facebookClient = new DefaultFacebookClient("CAAIxc0whCzEBAFfBlGGZAhRJTyflsTalxVGAuXMoAVc8i6TYV7810NhecFS3ZCUYZCZBlAO0i7ld3f0ZACA7VpcpHA3v0vEbM79RXB878IaXdU5OvuKLoqDVRyyotkyUt2DgJmyRRf7IihpiHnyF8wPkrCZBfAYR0qvqL0cD64JTZAXG3yszN1hvhYlkamZAcnXFSe0AWwHlIgZDZD");
				
		// Here's how to handle an FQL query
		//String query = "SELECT uid, name FROM user WHERE uid=527118462";
		String query = "SELECT message FROM stream WHERE (source_id IN (SELECT uid2 FROM friend WHERE uid1 = me()) OR source_id=me()  ) "
				+ "AND strpos(lower(message),lower('#SanFrancisco')) >=0";
		List<JsonObject> queryResults = facebookClient.executeFqlQuery(query, JsonObject.class);
		System.out.println("User = " + queryResults.get(0));
		return queryResults;
		
	}

	private com.sola.instagram.model.User getInstagramFeeds() throws Exception {
		InstagramAuthentication auth =  new InstagramAuthentication();
		String authUrl = auth.setRedirectUri("http://localhost:8080")
		                     .setClientSecret("b85968e28b694f0182b9b2c3a8b438b8")
		                     .setClientId("c810aa3e4e254b9986372e5cfffa2347")
		                     .setScope("comments+likes")
		                     .getAuthorizationUri();
		
		//com.sola.instagram.auth.AccessToken token = auth.build("code");
		com.sola.instagram.auth.AccessToken token = new com.sola.instagram.auth.AccessToken("7209504.5b9e1e6.ef915ff0074e4a8cacdb4b5b0fc7cacc");
		InstagramSession session = new InstagramSession(token);
		
		//com.sola.instagram.model.User rihanna = session.searchUsersByName("badgalriri").get(0);
		com.sola.instagram.model.User me = session.searchUsersByName("devangvdesai").get(0);
		System.out.println("user = " + me.getFullName());
		return me;
	}
	
	private void getTwitterFeedsFromTemplate() {
		//TwitterConfigurationTemplate template = new TwitterConfigurationTemplate();		
				//TwitterTemplate twitterTemplate = template.twitterTemplate();		
				//System.out.println("find tweets ---- " + twitterTemplate.userOperations().getUserProfile().getFriendsCount());		
				//model.addAttribute("twittertemplate", twitterTemplate.searchOperations().search("from:devangvdesai"));
				
	}
	
	public class FqlUser {
		  @Facebook
		  String uid;
		  
		  @Facebook
		  String name;

		  @Override
		  public String toString() {
		    return String.format("%s (%s)", name, uid);
		  }
		}
	
}

