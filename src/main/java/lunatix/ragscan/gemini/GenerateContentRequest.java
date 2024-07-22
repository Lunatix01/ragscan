package lunatix.ragscan.gemini;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GenerateContentRequest(
        @JsonProperty("contents") List<ContentRequest> contents,
        @JsonProperty("systemInstruction") ContentRequest systemInstruction
) {
}


