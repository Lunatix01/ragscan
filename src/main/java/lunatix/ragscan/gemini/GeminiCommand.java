package lunatix.ragscan.gemini;

import java.util.List;
import java.util.Objects;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.web.client.RestClient;

@ShellComponent
@RequiredArgsConstructor
public class GeminiCommand {

    private static final String GEMINI_URI = "/v1beta/models/gemini-1.5-flash-latest:generateContent?key={key}";

    private final VectorStore vectorStore;
    private final RestClient restClient;

    @Value("${spring.ai.vertex.ai.api-key}")
    private String apiKey;

    @ShellMethod(
            key = "ask",
            value = "Ask a question, Note: you need to load files",
            group = "Chat")
    private String ask(String question) {
        final var resultInDB = vectorStore.similaritySearch(SearchRequest.query(question).withTopK(5));
        final var generateContentRequest = getGenerateContentRequest(question, resultInDB);
        return Objects.requireNonNull(restClient.post()
                        .uri(GEMINI_URI, apiKey)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(generateContentRequest)
                        .retrieve()
                        .body(GeminiResponse.class))
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
