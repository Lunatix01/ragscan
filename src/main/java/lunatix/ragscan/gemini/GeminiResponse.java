package lunatix.ragscan.gemini;

import java.util.List;
import lombok.Data;

@Data
public class GeminiResponse {
    private List<Candidate> candidates;
    private UsageMetadata usageMetadata;

    @Data
    public static class Candidate {
        private Content content;
        private String finishReason;
        private Long index;
        private List<SafetyRating> safetyRatings;
    }

    @Data
    public static class Content {
        private List<Part> parts;
        private String role;
    }

    @Data
    public static class Part {
        private String text;
    }

    @Data
    public static class SafetyRating {
        private String category;
        private String probability;
    }

    @Data
    public static class UsageMetadata {
        private Long promptTokenCount;
        private Long candidatesTokenCount;
        private Long totalTokenCount;
    }
}

