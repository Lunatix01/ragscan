package lunatix.ragscan.loader;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;

@Getter
@AllArgsConstructor
@Slf4j
public enum FileType {
    HTML(List.of("htm", "html")),
    TXT(List.of("txt")),
    PDF(List.of("pdf")),
    JSON(List.of("json")),
    XML(List.of("xml")),
    OTHER(List.of());

    private final List<String> fileExtensions;

    public static FileType fromFileExtension(String fileExtension) {
        log.info("Getting fileType for {}", fileExtension);
        return Arrays.stream(FileType.values())
                .filter(f -> f.getFileExtensions().contains(fileExtension.toLowerCase()))
                .findFirst()
                .orElse(OTHER);
    }

    public static String getFileExtension(Path filePath) {
        log.info("Getting file extension for {}", filePath);
        return FilenameUtils.getExtension(filePath.toString());
    }
}
