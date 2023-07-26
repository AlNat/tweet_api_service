package dev.alnat.tweet.apiservice.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Model of tweet information
 *
 * Created by @author AlNat on 26.06.2023.
 * Licensed by Apache License, Version 2.0
 */
@Data
@ToString
@EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "New tweet information")
public class TweetData {

    @Schema(description = "Tweet author", requiredMode = Schema.RequiredMode.REQUIRED, example = "Dow")
    private String author;

    @Schema(description = "Tweet tags", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "tweet")
    private List<String> tags;

    @Schema(description = "Tweet itself", requiredMode = Schema.RequiredMode.REQUIRED, example = "Some tweet text")
    private String tweet;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @Schema(description = "Tweet send date", requiredMode = Schema.RequiredMode.REQUIRED, example = "2023-01-01 12:00:00", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime sendAt;

}
