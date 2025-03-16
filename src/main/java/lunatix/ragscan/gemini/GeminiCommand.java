package lunatix.ragscan.gemini;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
@RequiredArgsConstructor
public class GeminiCommand {

    private final VectorStore vectorStore;
    private final ChatModel chatModel;

    @ShellMethod(
            key = "ask",
            value = "Ask a question, Note: you need to load files",
            group = "Chat")
    private String ask(String question) {
        final var resultInDB = vectorStore.similaritySearch(
                SearchRequest.builder()
                        .query(question)
                        .topK(5)
                        .build()
        );
        assert resultInDB != null && !resultInDB.isEmpty()
                : "error getting context";

        final var generatedPrompt = getGenerateContentRequest(question, resultInDB)
                .toArray(new Message[0]);
        return chatModel.call(generatedPrompt);
    }

    private static List<Message> getGenerateContentRequest(String message, List<Document> resultInDB) {
        final var systemContent = """
                Answer only from the data you got as input, otherwise say you don't know, and clean up weird formats
                like if it's json clean it up, if it's Markdown clean it up etc...
                Your name is Ragscan.
                You will always get some questions with some context. Use the context only.
                """;
        final var systemMessage = new SystemMessage(systemContent);
        final var messageContent = """
                Question: %s
                Context: %s
                """;

        final var userMessage = new UserMessage(String.format(messageContent, message, resultInDB.toString()));

        return List.of(
                systemMessage,
                userMessage
        );
    }
}
