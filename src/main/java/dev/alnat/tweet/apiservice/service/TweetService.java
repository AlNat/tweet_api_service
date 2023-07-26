package dev.alnat.tweet.apiservice.service;

import dev.alnat.tweet.apiservice.model.TweetData;

/**
 * Created by @author AlNat on 26.06.2023.
 * Licensed by Apache License, Version 2.0
 */
public interface TweetService {

    void saveTweet(TweetData data);

}
