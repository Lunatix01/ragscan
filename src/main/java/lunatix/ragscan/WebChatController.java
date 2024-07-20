package lunatix.ragscan;

import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.qdrant.QdrantVectorStore;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
class WebChatController {

    private final QdrantVectorStore vectorStore;

    @GetMapping("/chat/llama3")
    public List<Document> llama3(@RequestParam("message") String message) {
//        final var userMessage = new UserMessage(message);
//        final var systemMessage = new SystemMessage("""
//                Answer in a good manner.
//                """);
//
//        final var prompt = new Prompt(List.of(userMessage, systemMessage));
//        final var x = new TikaDocumentReader(initialDesign)
//                .get()
//                .getFirst();
//        final var splitter = new TokenTextSplitter();
//        final var splittedResult = splitter.split(x);
//        System.out.println(splittedResult.size());
////        Jsoup.parse().text();
//        vectorStore.add(splittedResult);
        log.info(message);
        return vectorStore.similaritySearch(message);
    }
}
