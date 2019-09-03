package com.cthesign.twitterapp;

import com.cthesign.twitterapp.service.TwitterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.time.Duration;

@SpringBootApplication
public class TwitterApplication implements CommandLineRunner {

	@Autowired
	private TwitterService twitterService;

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(TwitterApplication.class);
		app.setWebApplicationType(WebApplicationType.NONE);
		ConfigurableApplicationContext ctx = app.run(args);
	}

	@Override
	public void run(String... args) throws Exception {
		twitterService.streamUsers(args).delayElements(Duration.ofSeconds(1)).toStream().forEach(tweet -> System.out.println(tweet.getUserName() + " : " + tweet.getText()));
	}
}
