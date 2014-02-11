/**
 * 
 */
package com.audi.quattro.social;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;

/**
 * @author devang.desai
 *
 */
@Configuration
@PropertySource("classpath:twitter.properties")
public class TwitterConfigurationTemplate {
	// Twitter properties
	@Value("${twitter.consumer_key}")
	private String twitterConsumerKey;

	@Value("${twitter.consumer_secret}")
	private String twitterConsumerSecret;

	@Value("${twitter.access_token}")
	private String twitterAccessToken;

	@Value("${twitter.access_token_secret}")
	private String twitterAccessTokenSecret;

	@Bean
	public Twitter twitterBean() {
		Twitter twitter = new TwitterFactory().getInstance();
		twitter.setOAuthConsumer(twitterConsumerKey, twitterConsumerSecret);
		twitter.setOAuthAccessToken(new AccessToken(twitterAccessToken,
				twitterAccessTokenSecret));

		return twitter;
	}
}
