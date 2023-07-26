package dev.alnat.tweet.apiservice.tools;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Mock of service after kafka
 * Created by @author AlNat on 31.05.2023.
 * Licensed by Apache License, Version 2.0
 */
@Slf4j
@Component
public class BackendMockListener {

    @KafkaListener(
            groupId = "backend-mock-service",
            topics = "tweet.save"
    )
    public void process(final Message<String> data) {
        log.debug("received by {} = [{}]", this.getClass().getSimpleName(), data.getPayload());
        received.add(data.getPayload());
        count.incrementAndGet();
        latch.countDown();
    }

    private CountDownLatch latch = new CountDownLatch(1);
    private final AtomicInteger count = new AtomicInteger();
    private final Queue<String> received = new LinkedList<>();


    private CountDownLatch getLatch() {
        return latch;
    }

    private int getCount() {
        return count.get();
    }

    public Optional<String> poll() {
        return Optional.ofNullable(received.poll());
    }

    public List<String> getAllReceived() {
        return new ArrayList<>(received);
    }

    public void reset() {
        count.set(0);
        received.clear();
    }

    public void init(int count) {
        latch = new CountDownLatch(count);
    }


    @SneakyThrows
    public void awaitAllMessages() {
        boolean searchAwait = getLatch().await(15, TimeUnit.SECONDS);
        Assertions.assertTrue(searchAwait);
        Assertions.assertEquals(0L, getLatch().getCount());
    }

}
