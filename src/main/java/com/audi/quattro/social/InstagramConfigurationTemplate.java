package com.audi.quattro.social;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.sola.instagram.InstagramSession;
import com.sola.instagram.auth.InstagramAuthentication;
import com.sola.instagram.exception.InstagramException;

/**
 * @author devang.desai
 *
 */
@Configuration
@PropertySource("classpath:instagram.properties")
public class InstagramConfigurationTemplate {

	// Instagram properties
	@Value("${instagram.access_token}")
	private String instagramAccessToken;

	@Value("${instagram.client_id}")
	private String instagramClientID;

	@Value("${instagram.client_secret}")
	private String instagramClientSecret;

	@Value("${instagram.redirect_url}")
	private String instagramRedirectURL;

	@Bean
	public InstagramSession instagramBean() throws InstagramException {
		InstagramAuthentication auth =  new InstagramAuthentication();
		String authUrl = auth.setRedirectUri(instagramRedirectURL)
		                     .setClientSecret(instagramClientSecret)
		                     .setClientId(instagramClientID)
		                     .setScope("comments+likes")
		                     .getAuthorizationUri();
		
		//com.sola.instagram.auth.AccessToken token = auth.build("code");
		com.sola.instagram.auth.AccessToken token = new com.sola.instagram.auth.AccessToken(instagramAccessToken);
		InstagramSession session = new InstagramSession(token);
		return session;
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
	
	
}
