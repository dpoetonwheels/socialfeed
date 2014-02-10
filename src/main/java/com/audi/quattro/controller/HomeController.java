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

import facebook4j.Facebook;
import facebook4j.FacebookException;
import facebook4j.FacebookFactory;
import facebook4j.conf.ConfigurationBuilder;
import facebook4j.internal.org.json.JSONArray;
import facebook4j.internal.org.json.JSONException;
import facebook4j.internal.org.json.JSONObject;
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
		
	
	
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		logger.info("Welcome home! The client locale is {}.", locale);
		
    	try {
			model.addAttribute("tweets",  getTwitterFeeds(model, locale));
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (TwitterException e) {
			e.printStackTrace();			
		}
		
    	getFacebookFeeds();
    	
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
	
	private void getFacebookFeeds() {
		//Facebook facebook = new FacebookFactory().getInstance();		
		//facebook.setOAuthAppId("617321221655345", "0dac6987961e1eb770734913a3988f85");
		//facebook.setOAuthPermissions(commaSeparetedPermissions);				
		//facebook.setOAuthAccessToken(new facebook4j.auth.AccessToken("617321221655345|pfc-BNyh_SfvXvTigjSguBC4xJU"));
		//.setOAuthAccessToken(new AccessToken("57f5ad619084289f0a32d724da8e5c9a", null));
		
		// Get an access token from: 
		// https://developers.facebook.com/tools/explorer
		// Copy and paste it below.
		//String accessTokenString = "617321221655345|pfc-BNyh_SfvXvTigjSguBC4xJU";
		//facebook4j.auth.AccessToken at = new facebook4j.auth.AccessToken(accessTokenString);
		// Set access token.
		//facebook.setOAuthAccessToken(at);
		
		
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true)
		  .setOAuthAppId("617321221655345")
		  .setOAuthAppSecret("0dac6987961e1eb770734913a3988f85")
		  //.setOAuthAccessToken("617321221655345|pfc-BNyh_SfvXvTigjSguBC4xJU")
		  .setOAuthAccessToken("CAAIxc0whCzEBAMMWngCLZAEZAraxeU7tPbKQMRKDdYK2In1uBXIBFs12EElmh8dPzy3mxC8T6z6CEFSRVgspppEkB6bQocyEMORYhtoALw7nZCcCg0pwpa2u1bWTqEqz8CXuRC6FqFyRza5UR079xJ9XYVuWHLjul9UYZCqYbY94p2QRiBDsbIiVwLiD2QQVfw9LDKNyYgZDZD")
		  .setOAuthPermissions("email,publish_stream");
		FacebookFactory ff = new FacebookFactory(cb.build());
		Facebook facebook = ff.getInstance();
		
		try {
			
			//facebook.searchPosts("");
			String results = facebook.getPosts("techcrunch").toString();
            String response;
			try {
				response = stringToJson(results);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            
			System.out.println("facebook posts - " + 
					facebook.getPosts("techcrunch"));
		} catch (FacebookException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	public static String stringToJson(String data) throws JSONException
    {
        // Create JSON object
        JSONObject jsonObject = new JSONObject(data);
        		//.fromObject(data);
        JSONArray message = (JSONArray) jsonObject.get("message");
        System.out.println("Message : "+message);
        return "Done";
    }


	private void getTwitterFeedsFromTemplate() {
		//TwitterConfigurationTemplate template = new TwitterConfigurationTemplate();		
				//TwitterTemplate twitterTemplate = template.twitterTemplate();		
				//System.out.println("find tweets ---- " + twitterTemplate.userOperations().getUserProfile().getFriendsCount());		
				//model.addAttribute("twittertemplate", twitterTemplate.searchOperations().search("from:devangvdesai"));
				
	}
	
}
