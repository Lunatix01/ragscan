package lunatix.ragscan.loader;

import java.util.List;

import org.springframework.ai.document.Document;
import org.springframework.core.io.Resource;

public interface FileLoader {

    public List<Document> load(Resource resource);
}
