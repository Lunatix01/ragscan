package lunatix.ragscan.store;

import java.util.List;
import java.util.Objects;

import io.micrometer.observation.ObservationConvention;
import io.micrometer.observation.ObservationRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.metadata.DefaultUsage;
import org.springframework.ai.embedding.Embedding;
import org.springframework.ai.embedding.EmbeddingOptions;
import org.springframework.ai.embedding.EmbeddingRequest;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.ai.embedding.EmbeddingResponseMetadata;
import org.springframework.ai.embedding.observation.DefaultEmbeddingModelObservationConvention;
import org.springframework.ai.embedding.observation.EmbeddingModelObservationContext;
import org.springframework.ai.embedding.observation.EmbeddingModelObservationDocumentation;
import org.springframework.ai.model.ModelOptionsUtils;
import org.springframework.ai.openai.OpenAiEmbeddingModel;
import org.springframework.ai.openai.OpenAiEmbeddingOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.ai.openai.api.common.OpenAiApiConstants;
import org.springframework.lang.Nullable;
import org.springframework.retry.support.RetryTemplate;

public class GeminiOpenAiEmbeddingModel extends OpenAiEmbeddingModel {

    private static final Logger logger = LoggerFactory.getLogger(GeminiOpenAiEmbeddingModel.class);


    private static final ObservationConvention<EmbeddingModelObservationContext> DEFAULT_OBSERVATION_CONVENTION = new DefaultEmbeddingModelObservationConvention();
    private static final ObservationConvention<EmbeddingModelObservationContext> OBSERVATION_CONVENTION = DEFAULT_OBSERVATION_CONVENTION;
    private final OpenAiEmbeddingOptions defaultOptions;

    private final RetryTemplate retryTemplate;
    private final ObservationRegistry observationRegistry;
    private final OpenAiApi openAiApi;

    public GeminiOpenAiEmbeddingModel(OpenAiApi openAiApi,
                                      OpenAiEmbeddingOptions defaultOptions,
                                      RetryTemplate retryTemplate,
                                      ObservationRegistry observationRegistry) {
        super(openAiApi);
        this.defaultOptions = defaultOptions;
        this.retryTemplate = retryTemplate;
        this.observationRegistry = observationRegistry;
        this.openAiApi = openAiApi;
    }

    @Override
    public EmbeddingResponse call(EmbeddingRequest request) {
        OpenAiEmbeddingOptions requestOptions = mergeOptions(request.getOptions(), this.defaultOptions);
        OpenAiApi.EmbeddingRequest<List<String>> apiRequest = createRequest(request, requestOptions);

        var observationContext = EmbeddingModelObservationContext.builder()
                .embeddingRequest(request)
                .provider(OpenAiApiConstants.PROVIDER_NAME)
                .requestOptions(requestOptions)
                .build();

        return Objects.requireNonNull(EmbeddingModelObservationDocumentation.EMBEDDING_MODEL_OPERATION
                .observation(OBSERVATION_CONVENTION, DEFAULT_OBSERVATION_CONVENTION, () -> observationContext,
                        this.observationRegistry)
                .observe(() -> {
                    OpenAiApi.EmbeddingList<OpenAiApi.Embedding> apiEmbeddingResponse = this.retryTemplate
                            .execute(ctx -> this.openAiApi.embeddings(apiRequest).getBody());

                    if (apiEmbeddingResponse == null) {
                        logger.warn("No embeddings returned for request: {}", request);
                        return new EmbeddingResponse(List.of());
                    }

                    var metadata = new EmbeddingResponseMetadata(apiEmbeddingResponse.model(),
                            getDefaultUsage(apiEmbeddingResponse.usage()));

                    List<Embedding> embeddings = apiEmbeddingResponse.data()
                            .stream()
                            .map(e -> new Embedding(e.embedding(), e.index()))
                            .toList();

                    EmbeddingResponse embeddingResponse = new EmbeddingResponse(embeddings, metadata);

                    observationContext.setResponse(embeddingResponse);

                    return embeddingResponse;
                }));
    }

    private OpenAiEmbeddingOptions mergeOptions(@Nullable EmbeddingOptions runtimeOptions,
                                                OpenAiEmbeddingOptions defaultOptions) {
        var runtimeOptionsForProvider = ModelOptionsUtils.copyToTarget(runtimeOptions, EmbeddingOptions.class,
                OpenAiEmbeddingOptions.class);

        if (runtimeOptionsForProvider == null) {
            return defaultOptions;
        }

        return OpenAiEmbeddingOptions.builder()
                // Handle portable embedding options
                .model(ModelOptionsUtils.mergeOption(runtimeOptionsForProvider.getModel(), defaultOptions.getModel()))
                .dimensions(ModelOptionsUtils.mergeOption(runtimeOptionsForProvider.getDimensions(),
                        defaultOptions.getDimensions()))
                // Handle OpenAI specific embedding options
                .encodingFormat(ModelOptionsUtils.mergeOption(runtimeOptionsForProvider.getEncodingFormat(),
                        defaultOptions.getEncodingFormat()))
                .user(ModelOptionsUtils.mergeOption(runtimeOptionsForProvider.getUser(), defaultOptions.getUser()))
                .build();
    }

    private OpenAiApi.EmbeddingRequest<List<String>> createRequest(EmbeddingRequest request,
                                                                   OpenAiEmbeddingOptions requestOptions) {
        return new OpenAiApi.EmbeddingRequest<>(request.getInstructions(), requestOptions.getModel(),
                requestOptions.getEncodingFormat(), requestOptions.getDimensions(), requestOptions.getUser());
    }

    /*
        Because Gemini doesn't provide default usage we will use a mock data, otherwise we will get an NPE exception
        that's why this class is created.
     */
    private DefaultUsage getDefaultUsage(OpenAiApi.Usage usage) {
        return new DefaultUsage(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, usage);
    }
}
