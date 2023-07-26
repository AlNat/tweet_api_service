package dev.alnat.tweet.apiservice;

import dev.alnat.tweet.apiservice.model.TweetData;
import dev.alnat.tweet.apiservice.model.TweetDataBuilder;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by @author AlNat on 26.07.2023
 * Licensed by Apache License, Version 2.0
 */
@JsonTest
class TweetDataMappingTest {

    @Autowired
    private JacksonTester<TweetData> tester;

    private static final LocalDateTime SEND_AT = LocalDateTime.of(2021, 2, 13, 12, 0, 0);

    @Test
    @SneakyThrows
    void serializationTest() {
        var dto = TweetDataBuilder.someTweet().withTag("tag2").withTag("tag1").withSendAt(SEND_AT).build();
        var json = tester.write(dto);
        assertThat(json).isEqualToJson("/tweet/some_tweet.json");
    }

    @Test
    @SneakyThrows
    void deserializationTest() {
        var content = tester.readObject("/tweet/some_tweet.json");
        var dto = TweetDataBuilder.someTweet().withTag("tag2").withTag("tag1").withSendAt(SEND_AT).build();

        assertThat(content).isEqualTo(dto);
    }

}
