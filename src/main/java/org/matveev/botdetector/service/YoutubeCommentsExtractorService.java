package org.matveev.botdetector.service;

import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.CommentThread;
import com.google.api.services.youtube.model.CommentThreadListResponse;
import lombok.RequiredArgsConstructor;
import org.matveev.botdetector.config.GptProperties;
import org.matveev.botdetector.config.YoutubeConfig;
import org.matveev.botdetector.dto.GptRequestDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class YoutubeCommentsExtractorService {

    private final YoutubeConfig youtubeConfig;
    private final GptProperties gptProperties;

    private final YouTube youtube;
    private final GptApiService gptApiService;

    public String getBotsFromComments(String videoId, int probability) {
        StringBuilder commentsBuilder = new StringBuilder();
        try {
            // Запрос к YouTube API для получения комментариев
            YouTube.CommentThreads.List request = youtube.commentThreads()
                    .list("snippet")
                    .setKey(youtubeConfig.getApiKey())
                    .setVideoId(videoId)
                    .setTextFormat("plainText");

            // Выполнение запроса
            CommentThreadListResponse response = request.execute();

            // Обработка полученных комментариев
            for (CommentThread commentThread : response.getItems()) {
                String comment = commentThread.getSnippet().getTopLevelComment().getSnippet().getTextDisplay();
                String author = commentThread.getSnippet().getTopLevelComment().getSnippet().getAuthorDisplayName();
                commentsBuilder.append("Автор: ").append(author).append(" Комментарий: ").append(comment).append("\n");
            }
        } catch (Exception e) {
            return "Ошибка получения комментариев: " + e.getMessage();
        }

        return getBots(commentsBuilder.toString(), probability);
    }

    private String getBots(String comments, int probability) {
        String request = "Я пришлю тебе комментарии в формате " +
                "\"Автор: @<имя_автора> Комментарий: <комментарий> ... \". " +
                "Проанализируй следующие комментарии и укажи авторов, вероятность того, " +
                "что они являются ботами, составляет " + probability + "% и более. " +
                "Предоставь только имена авторов подходящих под вероятность " +
                "(не подходящих не предоставляй) и причины, по которым ты пришел к такому выводу. " +
                "Формат твоего ответа: <автор>:<причина> (если больше, то через запятую следующих)." + comments;
        GptRequestDto gptRequest = GptRequestDto.builder()
                .messages(List.of(
                        GptRequestDto.Messages.builder()
                                .role("user")
                                .content(request)
                                .build()
                        )
                )
                .model(gptProperties.getModel())
                .build();
        return gptApiService.generate(gptRequest)
                .getChoices()
                .get(0)
                .getMessage()
                .getContent();
    }

}
