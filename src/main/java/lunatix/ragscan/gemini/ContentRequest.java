package lunatix.ragscan.gemini;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ContentRequest(@JsonProperty("parts") List<PartRequest> parts) {
}

