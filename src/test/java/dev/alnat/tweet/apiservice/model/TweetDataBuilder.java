package dev.alnat.tweet.apiservice.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Test builder with preset values for tests
 *
 * Created by @author AlNat on 26.06.2023.
 * Licensed by Apache License, Version 2.0
 */
@AllArgsConstructor
@NoArgsConstructor(staticName = "someTweet")
@With
public class TweetDataBuilder implements Builder<TweetData> {

    private String author = "Dow";
    private String tweet = "This is the tweet text";
    private LocalDateTime sendAt = LocalDateTime.now();
    private List<String> tags = new ArrayList<>();

    public TweetDataBuilder withTag(String tag) {
        if (this.tags == null) {
            this.tags = new ArrayList<>();
        }

        this.tags.add(tag);
        return this;
    }

    @Override
    public TweetData build() {
        final var tweetData = new TweetData();
        tweetData.setAuthor(author);
        tweetData.setTweet(tweet);
        tweetData.setSendAt(sendAt.withNano(0));

        if (tags != null && !tags.isEmpty()) {
            tweetData.setTags(tags);
        }

        return tweetData;
    }

}
