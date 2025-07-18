package org.developers.service.impl;

import lombok.extern.log4j.Log4j2;
import org.developers.common.config.ReCaptchaConfig;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
@Log4j2
public class ReCaptchaService {

    private final ReCaptchaConfig recaptchaConfig;
    private final RestTemplate restTemplate;

    private static final double MIN_SCORE = 0.5; // Score mínimo para ReCaptcha v3

    public ReCaptchaService(ReCaptchaConfig recaptchaConfig) {
        this.recaptchaConfig = recaptchaConfig;
        this.restTemplate = new RestTemplate();
    }

    public boolean verifyRecaptcha(String recaptchaToken) {
        try {
            if (recaptchaToken == null || recaptchaToken.trim().isEmpty()) {
                log.error("ReCaptcha token es null o vacío");
                return false;
            }

            log.info("Verificando ReCaptcha token: {}", recaptchaToken.substring(0, Math.min(20, recaptchaToken.length())) + "...");

            String url = recaptchaConfig.getVerifyUrl();
            org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
            headers.setContentType(org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED);
            String body = "secret=" + recaptchaConfig.getSecretKey() + "&response=" + recaptchaToken;
            org.springframework.http.HttpEntity<String> request = new org.springframework.http.HttpEntity<>(body, headers);
            RestTemplate restTemplate = new RestTemplate();
            Map<String, Object> response = restTemplate.postForObject(url, request, Map.class);

            if (response == null) {
                log.error("ReCaptcha response es null");
                return false;
            }

            Boolean success = (Boolean) response.get("success");
            Double score = (Double) response.get("score");
            Object errorCodes = response.get("error-codes");

            log.info("ReCaptcha response - success: {}, score: {}, error-codes: {}", success, score, errorCodes);

            // Google test key for v3
            String testKey = "6LeIxAcTAAAAAGG-vFI1TnRWxMZNFuojJ4WifJWe";

            if (success != null && success) {
                if (score == null && recaptchaConfig.getSecretKey().equals(testKey)) {
                    // Permitir si es clave de test y success true
                    log.info("ReCaptcha validado con éxito usando clave de test. Score es null (esperado en test). Permitiendo registro.");
                    return true;
                } else if (score != null) {
                    log.info("ReCaptcha v3 score: {} (min: {})", score, MIN_SCORE);
                    return score >= MIN_SCORE;
                } else {
                    log.warn("Score es null y no es clave de test. Rechazando.");
                    return false;
                }
            } else {
                log.warn("ReCaptcha token inválido - posible expiración o clave incorrecta");
                log.warn("Token recibido: {}", recaptchaToken);
                log.warn("Secret key usada: {}", recaptchaConfig.getSecretKey());
                log.warn("URL de verificación: {}", recaptchaConfig.getVerifyUrl());
                log.warn("ReCaptcha verification failed - success: {}, score: {}, error-codes: {}", success, score, errorCodes);
                return false;
            }

        } catch (Exception e) {
            log.error("Error verificando ReCaptcha: {}", e.getMessage(), e);
            return false;
        }
    }
} 