package dev.alnat.tweet.apiservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by @author AlNat on 26.06.2023.
 * Licensed by Apache License, Version 2.0
 */
@Configuration
public class SwaggerConfiguration {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Tweet API")
                        .version("1.0.0")
                        .description("Sample REST API for saving tweets in Elastic throw Kafka"));
    }

}
