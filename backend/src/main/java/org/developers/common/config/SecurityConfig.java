package org.developers.common.config;


import org.apache.catalina.connector.Connector;
import org.developers.common.utils.security.JWTAuthenticationFilter;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JWTAuthenticationFilter jwtAuthFilter;

    public SecurityConfig(JWTAuthenticationFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // Configuración de autorización de solicitudes HTTP
                .authorizeHttpRequests(auth -> auth
                        // Permitir acceso sin autenticación a las rutas específicas primero
                        .requestMatchers(
                                "/api/v1/auth/login",
                                "/api/v1/users/create",
                                "/api/v1/users/availability/email",
                                "/api/v1/users/availability/username",
                                "/api/v1/videos/trending",
                                "/api/v1/videos/youtube/trending"
                        ).permitAll()
                        // Requerir autenticación para todas las demás rutas bajo /api/
                        .requestMatchers("/api/**").authenticated()
                        // Permitir acceso sin autenticación a cualquier otra solicitud
                        .anyRequest().permitAll()
                )
                // Deshabilitar la protección CSRF (común para APIs REST sin sesiones)
                .csrf(AbstractHttpConfigurer::disable)
                // Configuración de CORS
                .cors(cors -> cors
                        .configurationSource(corsConfigurationSource()) // Usar la fuente de configuración CORS
                )
                // Configuración de gestión de sesiones: sin estado (STATELESS) para APIs REST con JWT
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                // Añadir el filtro JWT personalizado antes del filtro de autenticación de usuario y contraseña
                .addFilterBefore(this.jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                // Deshabilitar la autenticación HTTP Basic
                .httpBasic(AbstractHttpConfigurer::disable);

        // Construir y retornar la cadena de filtros de seguridad
        return http.build();
    }

    @Bean
    public ServletWebServerFactory servletContainer() {
        return new TomcatServletWebServerFactory();
    }

    private Connector redirectConnector() {
        Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
        connector.setScheme("http");
        connector.setPort(8080);
        connector.setSecure(false);
        connector.setRedirectPort(8443);
        return connector;
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList(
            "http://localhost:3000", 
            "https://localhost:3000",
            "http://localhost:5173", 
            "https://localhost:5173",
            "http://localhost:4173", 
            "https://localhost:4173",
            "http://localhost:8080"
        ));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setExposedHeaders(Arrays.asList("Authorization", "Content-Type", "X-Total-Count", "X-Requested-With", "Set-Cookie"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
