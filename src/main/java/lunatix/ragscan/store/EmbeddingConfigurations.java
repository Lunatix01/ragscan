package lunatix.ragscan.store;

import io.micrometer.observation.ObservationRegistry;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.openai.OpenAiEmbeddingOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.support.RetryTemplate;

@Configuration
class EmbeddingConfigurations {

    @Value("${spring.ai.openai.api-key}")
    private String apiKey;

    @Value("${spring.ai.openai.base-url}")
    private String baseUrl;


    @Value("${spring.ai.openai.embedding.options.model}")
    private String embeddingModel;

    @Value("${spring.ai.openai.embedding.embeddings-path}")
    private String embeddingPath;

    @Value("${spring.ai.openai.embedding.options.dimensions}")
    private Integer embeddingDimension;

    @Bean
    EmbeddingModel embeddingModel() {
        final var openAiApi = OpenAiApi.builder()
                .apiKey(apiKey)
                .baseUrl(baseUrl)
                .embeddingsPath(embeddingPath)
                .build();
        return new GeminiOpenAiEmbeddingModel(
                openAiApi,
                OpenAiEmbeddingOptions.builder()
                        .model(embeddingModel)
                        .dimensions(embeddingDimension)
                        .build(),
                RetryTemplate.builder()
                        .maxAttempts(10)
                        .fixedBackoff(1000)
                        .build(),
                ObservationRegistry.create()
        );
    }
}
