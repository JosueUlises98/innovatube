package org.developers.common.config;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.youtube.YouTube;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class YoutubeConfig {

    @Value("${youtube.application.name}")
    private String applicationName;
    @Getter
    @Value("${youtube.api.key}")
    private String apiKey;

    @Bean
    public YouTube youTube() {
        try {
            return (new YouTube.Builder(GoogleNetHttpTransport.newTrustedTransport(), GsonFactory.getDefaultInstance(), (httpRequest) -> httpRequest.getUrl().set("key", this.apiKey))).setApplicationName(this.applicationName).build();
        } catch (Exception e) {
            throw new RuntimeException("Error al inicializar YouTube API", e);
        }
    }
}
