package com.cthesign.twitterapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.auth.AccessToken;
import twitter4j.conf.ConfigurationBuilder;

@Configuration
public class TwitterConfig {

    @Bean
    public TwitterFactory getFactoryInstance() {
        ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
        configurationBuilder.setDebugEnabled(true)
                .setOAuthConsumerKey("")
                .setOAuthConsumerSecret("")
                .setOAuthAccessToken("")
                .setOAuthAccessTokenSecret("")
                .setHttpConnectionTimeout(2000);
        return new TwitterFactory(configurationBuilder.build());
    }

    @Bean
    public Twitter getTwitterInstance() {
        return getFactoryInstance().getInstance();
    }

    @Bean
    public TwitterStream getTwitterStreamInstance() {
        TwitterStream twitterStream = TwitterStreamFactory.getSingleton();
        twitterStream.setOAuthConsumer("8Ts7X9fwcOQgjab1kO1if1hM2", "PKzGDjzIbMEnmF3zAUgDatzpCxcSNTisuHVFKcGXC0jSncLpCH");
        twitterStream.setOAuthAccessToken(new AccessToken("1167133671851270144-IW2XZzHZ7X13URDpvtCl6XuatcFCq6", "8O5HobAUOherOMWBCX3OitSkkuKq0sWbJfyOPWLA489oM"));
        return twitterStream;
    }
}
