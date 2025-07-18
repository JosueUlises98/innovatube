package org.developers.service.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.developers.api.response.User.UserSessionDetails;
import org.developers.common.config.JWTProperties;
import org.developers.common.exception.exceptions.AuthenticationException;
import org.developers.common.utils.security.TokenPair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
@Log4j2
public class TokenService {
    private final Key key;
    private final long accessTokenExpiration;
    private final long refreshTokenExpiration;

    @Autowired
    public TokenService(Key key, JWTProperties jwtProperties) {
        this.key = key;
        this.accessTokenExpiration = jwtProperties.getAccess().getExpiration();
        this.refreshTokenExpiration = jwtProperties.getRefresh().getExpiration();
    }

    public TokenPair generateTokenPair(UserSessionDetails userDetails) {
        String accessToken = this.generateToken(userDetails, this.accessTokenExpiration);
        String refreshToken = this.generateToken(userDetails, this.refreshTokenExpiration);
        return new TokenPair(accessToken, refreshToken);
    }

    private String generateToken(UserSessionDetails userDetails, long expiration) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);
        return Jwts.builder().setSubject(String.valueOf(userDetails.getUserId())).claim("username", userDetails.getUsername()).setIssuedAt(now).setExpiration(expiryDate).signWith(this.key).compact();
    }

    public Claims validateToken(String token) {
        try {
            return (Claims)Jwts.parserBuilder().setSigningKey(this.key).build().parseClaimsJws(token).getBody();
        } catch (JwtException e) {
            log.error("Error validando token: {}", e.getMessage());
            throw new AuthenticationException("Token inválido");
        }
    }

    public void addTokenCookies(HttpServletResponse response, TokenPair tokens) {
        this.addSecureCookie(response, "access_token", tokens.getAccessToken(), (int) TimeUnit.MINUTES.toSeconds(30L));
        this.addSecureCookie(response, "refresh_token", tokens.getRefreshToken(), (int)TimeUnit.DAYS.toSeconds(7L));
    }

    private void addSecureCookie(HttpServletResponse response, String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(maxAge);
        cookie.setAttribute("SameSite", "None");
        response.addCookie(cookie);
    }
}
