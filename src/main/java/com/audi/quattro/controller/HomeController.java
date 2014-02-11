package com.audi.quattro.controller;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.audi.quattro.social.FacebookConfigurationTemplate;
import com.audi.quattro.social.InstagramConfigurationTemplate;
import com.audi.quattro.social.TwitterConfigurationTemplate;
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
	@Autowired
	private TwitterConfigurationTemplate twitterTemplate;
		
	// Facebook properties
	@Autowired
	private FacebookConfigurationTemplate facebookTemplate;
	
	// Instagram properties
	@Autowired
	private InstagramConfigurationTemplate instagramTemplate;
	
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		logger.info("Welcome home! The client locale is {}.", locale);
		
		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
		
		String formattedDate = dateFormat.format(date);
		
		model.addAttribute("serverTime", formattedDate );
				
		// get tweets from Twitter
    	try {
    		
    		model.addAttribute("tweets",  getTwitterFeeds());
		} catch (IllegalStateException e) {
			e.printStackTrace();
		}
		
    	// get posts from facebook.
    	model.addAttribute("fbposts",  getFacebookFeeds());
    	
    	// get user from instagram
    	try {
			model.addAttribute("instauser", getInstagramFeeds());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
		return "home";
	}
	
	public List<Status> getTwitterFeeds() {
		Query query = new Query("from:" + "davidstarsoccer" + " #" + "share2");
		List<Status> tweets = new ArrayList<Status>();
		try {
			tweets = twitterTemplate.twitterBean().search(query).getTweets();
		} catch (TwitterException e) {
			e.printStackTrace();
		}
		return tweets;
	}
	
	private com.sola.instagram.model.User getInstagramFeeds() throws Exception {
		com.sola.instagram.model.User me = instagramTemplate.instagramBean()
				.searchUsersByName("devangvdesai").get(0);
		System.out.println("user = " + me.getFullName());
		return me;

	}

	private List<JsonObject> getFacebookFeeds() {
		// david.roth.923519@facebook.com
		FacebookClient facebookClient = new DefaultFacebookClient("CAACEdEose0cBAFdMLFIHf38zZAZCnsYmJq0ZC4E6TdpZBj7DIgLrDh3cNvgqTWWsTbSdYaLzlPlVsV9ZC1ZAdPn7qyfro0nSoHvjfBdLtMNpwZCNaIs0bJGLut6vDuIOglQWLCRSFBsWPqqw1ofnrxcGVZCTWJEwavZAlagyY29YZBQ7JBEp9dr6IcUkZBLVoxwJZA9PFhXTDKv2mAZDZD");
				
		// Here's how to handle an FQL query
		//String query = "SELECT uid, name FROM user WHERE uid=527118462";
		String query = "SELECT message FROM stream WHERE (source_id IN (SELECT uid2 FROM friend WHERE uid1 = me()) OR source_id=me()  ) "
				+ "AND strpos(lower(message),lower('#SanFrancisco')) >=0";
		List<JsonObject> queryResults = facebookClient.executeFqlQuery(query, JsonObject.class);
		System.out.println("User = " + queryResults);
		return queryResults;
		
	}
	
}

