package dev.alnat.tweet.apiservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.alnat.tweet.apiservice.model.TweetData;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

/**
 * Created by @author AlNat on 26.06.2023.
 * Licensed by Apache License, Version 2.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaSenderTweetService implements TweetService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper mapper;

    @Value("${spring.kafka.topics.tweet-api.save.topic}")
    private String topicName;

    @Override
    @SneakyThrows
    public void saveTweet(TweetData data) {
        kafkaTemplate.send(topicName, data.getAuthor(), mapper.writeValueAsString(data));
    }

}
