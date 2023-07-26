package dev.alnat.tweet.apiservice.api;

import dev.alnat.tweet.apiservice.model.TweetData;
import dev.alnat.tweet.apiservice.service.TweetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by @author AlNat on 26.06.2023.
 * Licensed by Apache License, Version 2.0
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/tweet")
@Tag(name = "REST API for tweet operations")
public class TweetAPI {

    private final TweetService service;

    @Operation(summary = "Save tweet data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Request complete"),
            @ApiResponse(responseCode = "500", description = "Internal error")
    })
    @PostMapping(value =  "")
    public ResponseEntity<String> saveTweet(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, description = "Tweet Data")
            @RequestBody TweetData tweetData) {
        service.saveTweet(tweetData);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
