package com.audi.quattro.controller;

import java.text.DateFormat;
import java.util.ArrayList;
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
	
	private void getTwitterFeedsFromTemplate() {
		//TwitterConfigurationTemplate template = new TwitterConfigurationTemplate();		
				//TwitterTemplate twitterTemplate = template.twitterTemplate();		
				//System.out.println("find tweets ---- " + twitterTemplate.userOperations().getUserProfile().getFriendsCount());		
				//model.addAttribute("twittertemplate", twitterTemplate.searchOperations().search("from:devangvdesai"));
				
	}
	
}
