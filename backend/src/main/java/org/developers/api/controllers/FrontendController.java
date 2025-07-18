package org.developers.api.controllers;

import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@RestController
@Log4j2
public class FrontendController {

    @GetMapping(value = {"/", "/login", "/register", "/profile", "/videos", "/favorites", "/video/**"}, 
                produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<String> serveFrontend() {
        try {
            ClassPathResource resource = new ClassPathResource("static/index.html");
            String content = new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
            return ResponseEntity.ok(content);
        } catch (IOException e) {
            log.error("Error serving frontend: {}", e.getMessage());
            return ResponseEntity.status(500).body("Error loading application");
        }
    }
} 