package lunatix.ragscan.loader;

import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.TextReader;
import org.springframework.core.io.Resource;

@Slf4j
public class TextFileLoader implements FileLoader {

    @Override
    public List<Document> load(Resource resource) {
        log.info("Loading text file {}", resource.getFilename());
        final var textReader = new TextReader(resource);
        return textReader.read();
    }
}
