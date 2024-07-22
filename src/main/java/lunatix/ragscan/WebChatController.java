package lunatix.ragscan;

import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lunatix.ragscan.gemini.ContentRequest;
import lunatix.ragscan.gemini.GeminiResponse;
import lunatix.ragscan.gemini.GenerateContentRequest;
import lunatix.ragscan.gemini.PartRequest;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

@RestController
@RequiredArgsConstructor
@Slf4j
class WebChatController {

    private final VectorStore vectorStore;
    private final RestClient restClient;

    @Value("${spring.ai.vertex.ai.api-key}")
    private String apiKey;

    @GetMapping("/chat/llama3")
    public String llama3(@RequestParam("message") String message) {
        final var resultInDB = vectorStore.similaritySearch(SearchRequest.query(message).withTopK(3));

        final var generateContentRequest = getGenerateContentRequest(message, resultInDB);
        return restClient.post()
                .uri("/v1beta/models/gemini-1.5-flash-latest:generateContent?key={key}", apiKey)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(generateContentRequest)
                .retrieve()
                .body(GeminiResponse.class)
                .getCandidates().getFirst()
                .getContent()
                .getParts()
                .getFirst()
                .getText();
    }

    private static GenerateContentRequest getGenerateContentRequest(String message, List<Document> resultInDB) {
        final var systemContent = """
                Answer only from the context, otherwise say you don't know, and clean up weird formats
                like if it's json clean it up, if it's Markdown clean it up etc...
                context: %s
                """;

        return new GenerateContentRequest(
                List.of(new ContentRequest(List.of(new PartRequest(message)))),
                new ContentRequest(List.of(
                        new PartRequest(String.format(systemContent, resultInDB))))
        );
    }
}
