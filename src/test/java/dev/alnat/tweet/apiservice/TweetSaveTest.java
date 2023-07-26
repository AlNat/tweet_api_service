package dev.alnat.tweet.apiservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.alnat.tweet.apiservice.model.TweetData;
import dev.alnat.tweet.apiservice.model.TweetDataBuilder;
import dev.alnat.tweet.apiservice.tools.BackendMockListener;
import lombok.SneakyThrows;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.apache.kafka.clients.admin.AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
@DirtiesContext // due kafka
class TweetSaveTest {

    @Container
    @ServiceConnection(name = "kafka")
    static KafkaContainer kafka = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.2.6"));

    private static final Set<String> KAFKA_TOPICS = Set.of("tweet.save");

    @BeforeAll
    static void initKafkaTopics() {
        try (var admin = AdminClient.create(Map.of(BOOTSTRAP_SERVERS_CONFIG, kafka.getBootstrapServers()))) {
            admin.createTopics(KAFKA_TOPICS.stream().map(topic -> new NewTopic(topic, 1, (short) 1)).collect(Collectors.toList()));
        }
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BackendMockListener backend;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    @AfterEach
    void cleanUp() {
        backend.reset();
    }


    @Test
    @SneakyThrows
    @DisplayName("Send some new tweet with API and verify that it was send with Kafka")
    void successSendTweetTest() {
        backend.init(1);

        var testTweet = TweetDataBuilder.someTweet().build();
        sendTweetSaveToAPI(testTweet);

        backend.awaitAllMessages(); // await kafka processing

        // verify data
        var receivesTweet = backend.getAllReceived();
        assertEquals(1, receivesTweet.size());
        assertEquals(testTweet, objectMapper.readValue(receivesTweet.get(0), TweetData.class));
    }

    @Test
    @SneakyThrows
    @DisplayName("Send few new tweets with API and verify that it were send with Kafka")
    void successSendMultipleTweetTest() {
        backend.init(3);

        sendTweetSaveToAPI(TweetDataBuilder.someTweet().build());
        sendTweetSaveToAPI(TweetDataBuilder.someTweet().withTag("first").withTweet("Another text").build());
        sendTweetSaveToAPI(TweetDataBuilder.someTweet().withTag("second").withTweet("And other text").build());

        backend.awaitAllMessages(); // await kafka processing

        // verify data
        var receivesTweet = backend.getAllReceived();
        var tweetDataDTO = receivesTweet.stream().map(s -> {
            try {
                return objectMapper.readValue(s, TweetData.class);
            } catch (JsonProcessingException e) {
                fail(e);
                return null;
            }
        }).toList();
        assertEquals(3, receivesTweet.size());
        assertTrue(tweetDataDTO.stream().allMatch(tweet -> tweet.getAuthor().equals("Dow")));
    }

    @SneakyThrows
    private String sendTweetSaveToAPI(TweetData data) {
        return mockMvc.perform(
                        post("/api/v1/tweet")
                                .content(objectMapper.writeValueAsString(data))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

}
