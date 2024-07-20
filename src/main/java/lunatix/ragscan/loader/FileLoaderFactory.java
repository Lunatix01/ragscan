package lunatix.ragscan.loader;

public class FileLoaderFactory {

    public static FileLoader create(FileType fileType) {
        return switch (fileType) {
            case PDF -> new PdfFileLoader();
            case TXT -> new TextFileLoader();
            case JSON -> new OtherFileLoader();
            case HTML, XML, OTHER -> new OtherFileLoader();
        };
    }
}
