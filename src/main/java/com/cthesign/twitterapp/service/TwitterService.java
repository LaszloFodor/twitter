package com.cthesign.twitterapp.service;

import com.cthesign.twitterapp.config.TwitterConfig;
import com.cthesign.twitterapp.entity.Tweet;
import com.cthesign.twitterapp.exception.EmptyUserListException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import twitter4j.*;

import java.time.Duration;
import java.util.List;
import java.util.stream.Stream;

import static reactor.core.publisher.Flux.fromStream;

@Service
public class TwitterService {

    private Logger logger = LoggerFactory.getLogger(TwitterService.class);

    @Autowired
    private TwitterConfig twitterConfig;

    public Flux<Tweet> streamUsers(String[] userNames) throws EmptyUserListException {

        if (userNames == null || userNames.length == 0)
            throw new EmptyUserListException("List of users is empty. Please add existing users to the list.");

        TwitterStream twitterStram = twitterConfig.getTwitterStreamInstance();
        Tweet tweetStatus = new Tweet();
        twitterStram.addConnectionLifeCycleListener(new ConnectionLifeCycleListener() {
            @Override
            public void onConnect() {
                logger.info("Connected");
            }

            @Override
            public void onDisconnect() {
                logger.info("Disconnected");
                twitterStram.shutdown();
            }

            @Override
            public void onCleanUp() {
                logger.info("Cleanup");
                twitterStram.clearListeners();
                twitterStram.cleanUp();
            }
        });
        twitterStram.addListener(new StatusListener() {
            @Override
            public void onStatus(Status status) {
                logger.info(status.getUser().getName() + " : " + status.getText());
                tweetStatus.setUserName(status.getUser().getName());
                tweetStatus.setText(status.getText());
            }

            @Override
            public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {

            }

            @Override
            public void onTrackLimitationNotice(int i) {

            }

            @Override
            public void onScrubGeo(long l, long l1) {

            }

            @Override
            public void onStallWarning(StallWarning stallWarning) {

            }

            @Override
            public void onException(Exception e) {
                twitterStram.shutdown();
            }
        });

        long[] userIds = new long[userNames.length];
        FilterQuery filterQuery = new FilterQuery();
        for (int i = 0; i < userNames.length; i++) {
            try {
                userIds[i] = twitterConfig.getTwitterInstance().showUser(userNames[i]).getId();
            } catch (TwitterException e) {
                logger.info("This user has no id yet: " + userNames[i]);
            }
        }
        filterQuery.follow(userIds);
        twitterStram.filter(filterQuery);

        Flux<Long> interval = Flux.interval(Duration.ofSeconds(1));
        Flux<Tweet> events = fromStream(Stream.generate(() -> tweetStatus));
        return Flux.zip(events, interval, (key, value) -> key);
    }

}

