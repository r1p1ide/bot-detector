package org.matveev.botdetector.service;

import lombok.RequiredArgsConstructor;
import org.matveev.botdetector.config.GptProperties;
import org.matveev.botdetector.dto.GptRequestDto;
import org.matveev.botdetector.dto.GptResponseDto;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class GptApiService {

    private final GptProperties gptProperties;
    private final RestTemplate restTemplate;

    public GptResponseDto generate(GptRequestDto requestDto) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + gptProperties.getKey());

        HttpEntity<GptRequestDto> requestEntity =
                new HttpEntity<>(requestDto, headers);

        return restTemplate.exchange(
                gptProperties.getUrl(),
                HttpMethod.POST,
                requestEntity,
                GptResponseDto.class).getBody();
    }

}
