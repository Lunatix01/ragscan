package lunatix.ragscan.loader;

import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.ExtractedTextFormatter;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.core.io.Resource;

@Slf4j
public class PdfFileLoader implements FileLoader {

    @Override
    public List<Document> load(Resource resource) {
        log.info("Loading PDF Document {}", resource.getFilename());
        PagePdfDocumentReader pdfFile = new PagePdfDocumentReader(resource,
                PdfDocumentReaderConfig.builder()
                        .withPageTopMargin(0)
                        .withPageExtractedTextFormatter(ExtractedTextFormatter.builder()
                                .withNumberOfTopTextLinesToDelete(0)
                                .build())
                        .withPagesPerDocument(1)
                        .build());

        return pdfFile.read();
    }
}
