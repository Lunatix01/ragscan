package lunatix.ragscan.loader;

import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.JsonReader;
import org.springframework.core.io.Resource;

@Slf4j
public class JsonFileLoader implements FileLoader {

    @Override
    public List<Document> load(Resource resource) {
        log.info("Loading json file {}", resource.getFilename());
        final var jsonFile = new JsonReader(resource);
        return jsonFile.read();
    }
}
