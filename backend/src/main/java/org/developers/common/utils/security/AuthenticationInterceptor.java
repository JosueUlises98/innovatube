package org.developers.common.utils.security;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.developers.service.impl.TokenService;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

@Component
@Log4j2
public class AuthenticationInterceptor implements HandlerInterceptor {

    private final TokenService tokenService;
    private final List<String> publicPaths = Arrays.asList("/api/v1/auth/login","/api/v1/users/create" ,"/api/v1/auth/refresh",
            "/api/v1/users/availability/email", "/api/v1/users/availability/username");

    public AuthenticationInterceptor(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String path = request.getRequestURI();
        if (publicPaths.stream().anyMatch(path::startsWith)) {
            return true;
        } else {
            Cookie[] cookies = request.getCookies();
            if (cookies == null) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("No se encontró token de acceso");
                return false;
            } else {
                String accessToken = Stream.of(cookies)
                        .filter(cookie -> "access_token".equals(cookie.getName()))
                        .map(Cookie::getValue)
                        .findFirst()
                        .orElse(null);

                if (accessToken == null) {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.getWriter().write("Token no encontrado");
                    return false;
                }

                try {
                    Claims claims = this.tokenService.validateToken(accessToken);
                    request.setAttribute("userId", claims.getSubject());
                    request.setAttribute("username", claims.get("username"));
                } catch (Exception e) {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.getWriter().write("Token inválido o expirado");
                    return false;
                }
                return true;
            }
        }
    }
}
