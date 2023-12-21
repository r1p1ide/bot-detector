package org.matveev.botdetector.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class GptRequestDto {

    @JsonProperty("model")
    private final String model;

    @JsonProperty("messages")
    private final List<Messages> messages;

    @JsonProperty("temperature")
    private final Double temperature;

    @Getter
    @Builder
    public static class Messages {

        @JsonProperty("role")
        private final String role;

        @JsonProperty("content")
        private final String content;

    }

}
