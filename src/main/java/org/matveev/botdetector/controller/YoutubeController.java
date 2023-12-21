package org.matveev.botdetector.controller;

import lombok.RequiredArgsConstructor;
import org.matveev.botdetector.service.YoutubeCommentsExtractorService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/youtube")
@RequiredArgsConstructor
public class YoutubeController {

    private final YoutubeCommentsExtractorService youtubeCommentsExtractorService;

    @GetMapping("/comments/{videoId}/{probability}")
    public String getBots(@PathVariable String videoId,
                          @PathVariable int probability) {
        return youtubeCommentsExtractorService.getBotsFromComments(videoId, probability);
    }

}
