package lunatix.ragscan.gemini;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class GeminiClient {

    @Bean
    RestClient restClient() {
        return
                RestClient.builder()
                        .baseUrl("https://generativelanguage.googleapis.com")
                        .defaultHeader("Content-Type: application/json")
                        .build();
    }
}
