package org.matveev.botdetector.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties("gpt.properties.api")
public class GptProperties {

    private String key;

    private String url;

    private String model;
}
