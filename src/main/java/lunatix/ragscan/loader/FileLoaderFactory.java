package lunatix.ragscan.loader;

public class FileLoaderFactory {

    private FileLoaderFactory() {}

    public static FileLoader create(FileType fileType) {
        return switch (fileType) {
            case PDF -> new PdfFileLoader();
            case TXT -> new TextFileLoader();
            case JSON, HTML, XML, OTHER -> new OtherFileLoader();
        };
    }
}
