package lunatix.ragscan.loader;

import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.core.io.Resource;

@Slf4j
public class OtherFileLoader implements FileLoader {

    /**
     * This Supports variant of files, for example DOCX,PPTX, HTML, XML etc.
     * <a href="https://tika.apache.org/2.9.0/formats.html">click</a> here for full list
     */
    @Override
    public List<Document> load(Resource resource) {
        log.info("Loading file {}", resource.getFilename());
        final var file = new TikaDocumentReader(resource);
        return file.read();
    }
}
