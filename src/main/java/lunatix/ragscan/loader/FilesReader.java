package lunatix.ragscan.loader;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.core.io.InputStreamResource;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@RequiredArgsConstructor
@ShellComponent
@Slf4j
public class FilesReader {

    private final VectorStore vectorStore;

    @ShellMethod(
            key = "load",
            value = """
                    Give it the main folder and it will load supported files inside of it
                    """,
            group = "Prerequisite")
    public String loadFiles(String fullPath) {
        return Try.withResources(() -> Files.walk(Path.of(fullPath)))
                .of(paths -> paths
                        .filter(Files::isRegularFile)
                        .map(path -> {
                            final var fileExtension = FileType.getFileExtension(path.getFileName());
                            final var fileType = FileType.fromFileExtension(fileExtension);
                            final var fileLoader = FileLoaderFactory.create(fileType);
                            return Try.of(() -> Files.newInputStream(path))
                                    .map(InputStreamResource::new)
                                    .map(fileLoader::load)
                                    .peek(documents -> {
                                        final var splitter = new TokenTextSplitter();
                                        final var splittedDocuments = splitter.apply(documents);
                                        vectorStore.accept(splittedDocuments);
                                        log.info("added {} documents", splittedDocuments.size());
                                    })
                                    .get();
                        })
                        .toList()
                )
                .map(ignored -> "loaded Successfully")
                .getOrElse(() -> "Failed to load files");
    }
}
