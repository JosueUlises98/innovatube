package org.developers.common.config;

import io.jsonwebtoken.security.Keys;
import org.developers.service.impl.TokenService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.Key;
import java.security.SecureRandom;

@Configuration
public class JWTConfig {

    @Bean
    public Key jwtKey() {
        SecureRandom random = new SecureRandom();
        byte[] keyBytes = new byte[64];
        random.nextBytes(keyBytes);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    @Bean
    public TokenService tokenService(Key key, JWTProperties jwtProperties) {
        return new TokenService(key, jwtProperties);
    }
}
