package org.developers.common.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import lombok.Data;

@Configuration
@ConfigurationProperties(prefix = "recaptcha")
@Data
public class ReCaptchaConfig {
    private String secretKey;
    private String siteKey;
    private String verifyUrl = "https://www.google.com/recaptcha/api/siteverify";
} 