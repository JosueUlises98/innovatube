package org.developers.common.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Setter
@Getter
@Configuration
@ConfigurationProperties(
        prefix = "jwt"
)
public class JWTProperties {

    // Getters y setters
    private Access access;
    private Refresh refresh;

    @Setter
    @Getter
    public static class Access {
        private long expiration;

    }
    @Setter
    @Getter
    public static class Refresh {
        private long expiration;

    }
}
