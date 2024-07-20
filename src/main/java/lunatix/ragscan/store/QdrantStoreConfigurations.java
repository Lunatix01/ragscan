package lunatix.ragscan.store;

import java.util.concurrent.Future;

import io.qdrant.client.QdrantClient;
import io.qdrant.client.grpc.Collections;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@RequiredArgsConstructor
@ShellComponent
public class QdrantStoreConfigurations {

    private final QdrantClient qdrantClient;

    @Value("${spring.ai.vectorstore.qdrant.collection-name}")
    private String collectionName;

    @ShellMethod(
            key = "collection-size",
            value = """
                    Give it a desired collection size
                    """,
            group = "Prerequisite")
    public void saveCollectionSize(int size) {
        Try.of(() -> Collections.VectorParams.newBuilder().setSize(size)
                        .setDistance(Collections.Distance.Cosine)
                        .build())
                .map(vectorParams -> qdrantClient.recreateCollectionAsync(collectionName, vectorParams))
                .andThenTry(Future::get);
    }
}
