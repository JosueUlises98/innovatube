package org.developers.api.controllers.User;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.developers.api.request.User.CreateUser;
import org.developers.api.request.User.Login;
import org.developers.api.response.User.UserResponse;
import org.developers.api.response.User.UserSessionDetails;
import org.developers.common.exception.exceptions.AuthenticationException;
import org.developers.common.utils.security.TokenPair;
import org.developers.service.impl.ReCaptchaService;
import org.developers.service.impl.TokenService;
import org.developers.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;

@RestController
@RequestMapping({"/api/v1/auth"})
@Log4j2
public class AuthController {

    private final UserServiceImpl userService;
    private final TokenService tokenService;
    private final ReCaptchaService recaptchaService;

    @Autowired
    public AuthController(UserServiceImpl userService, TokenService tokenService, ReCaptchaService recaptchaService) {
        this.userService = userService;
        this.tokenService = tokenService;
        this.recaptchaService = recaptchaService;
    }

    @PostMapping({"/register"})
    public ResponseEntity<UserResponse> registerUser(@RequestBody @Valid CreateUser request) {
        try {
            log.info("Iniciando registro de usuario '{}'", request.getUsername());
            
            // TODO: Re-enable ReCaptcha validation once keys are properly configured
            // Validar ReCaptcha
            /*
            if (!recaptchaService.verifyRecaptcha(request.getRecaptchaToken())) {
                log.warn("ReCaptcha validation failed for user '{}'", request.getUsername());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            */
            log.info("ReCaptcha validación temporalmente deshabilitada para desarrollo");

            log.info("ReCaptcha validado correctamente para usuario '{}'", request.getUsername());
            UserResponse userResponse = userService.createUser(request);
            log.info("Usuario '{}' registrado exitosamente", request.getUsername());
            return ResponseEntity.ok(userResponse);
        } catch (Exception e) {
            log.error("Error registering user '{}': {}", request.getUsername(), e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PostMapping({"/verify-recaptcha"})
    public ResponseEntity<?> verifyRecaptcha(@RequestBody RecaptchaVerifyRequest request) {
        try {
            boolean isValid = recaptchaService.verifyRecaptcha(request.getToken());
            if (isValid) {
                return ResponseEntity.ok().body(Map.of("success", true));
            } else {
                return ResponseEntity.badRequest().body(Map.of("success", false, "message", "ReCaptcha validation failed"));
            }
        } catch (Exception e) {
            log.error("Error verifying ReCaptcha: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("success", false, "message", "Error verifying ReCaptcha"));
        }
    }

    @PostMapping({"/login"})
    public ResponseEntity<UserSessionDetails> loginUser(@RequestBody @Valid Login loginRequest, HttpServletResponse response) {
        try {
            UserSessionDetails userDetails = this.userService.login(loginRequest);
            TokenPair tokens = this.tokenService.generateTokenPair(userDetails);
            this.tokenService.addTokenCookies(response, tokens);
            log.info("Usuario '{}' ha iniciado sesión exitosamente", userDetails.getUsername());
            return ResponseEntity.ok(userDetails);
        } catch (AuthenticationException e) {
            log.warn("Login fallido para usuario '{}': {}", loginRequest.getUsername(), e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping({"/refresh"})
    public ResponseEntity<?> refreshToken(@CookieValue(name = "refresh_token",required = false) String refreshToken, HttpServletResponse response) {
        try {
            if (refreshToken == null) {
                throw new AuthenticationException("No refresh token provided");
            } else {
                Claims claims = this.tokenService.validateToken(refreshToken);
                UserSessionDetails userDetails = this.userService.getUserById(Long.parseLong(claims.getSubject()));
                TokenPair newTokens = this.tokenService.generateTokenPair(userDetails);
                this.tokenService.addTokenCookies(response, newTokens);
                return ResponseEntity.ok().build();
            }
        } catch (Exception e) {
            log.error("Error refreshing token: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping({"/logout"})
    public ResponseEntity<?> logout(HttpServletResponse response) {
        Arrays.asList("access_token", "refresh_token").forEach((cookieName) -> {
            Cookie cookie = new Cookie(cookieName, "");
            cookie.setHttpOnly(true);
            cookie.setSecure(true);
            cookie.setPath("/");
            cookie.setMaxAge(0);
            response.addCookie(cookie);
        });
        return ResponseEntity.ok().build();
    }

    // Inner class for ReCaptcha verification request
    public static class RecaptchaVerifyRequest {
        private String token;

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }
}
