# Instrucciones de Construcción e Integración con Spring Boot

## 🚀 Construcción del Frontend

### 1. Instalar Dependencias
```bash
cd frontend-react/
npm install
```

### 2. Construir para Producción
```bash
npm run build
```

Esto creará una carpeta `dist/` con todos los archivos optimizados para producción.

## 🔧 Integración con Spring Boot

### 1. Copiar Archivos al Backend

Después de construir el proyecto, copia todo el contenido de la carpeta `dist/` a:
```
src/main/resources/static/
```

### 2. Configuración de Spring Boot

Asegúrate de que tu `application.properties` tenga estas configuraciones:

```properties
# Configuración de recursos estáticos
spring.web.resources.static-locations=classpath:/static/
spring.web.resources.cache.period=0
spring.mvc.static-path-pattern=/**

# SSL Configuration (ya configurado en tu archivo)
server.ssl.enabled=true
server.port=8443
server.ssl.certificate=classpath:localhost+2.pem
server.ssl.certificate-private-key=classpath:localhost+2-key.pem
server.ssl.trust-certificate=classpath:cacert.pem
server.ssl.key-alias=localhost
server.ssl.protocol=TLS
server.ssl.enabled-protocols=TLSv1.2,TLSv1.3
server.ssl.require-ssl=true
```

### 3. Configuración de Rutas SPA

Crea o modifica tu `WebConfig.java` para manejar las rutas de React:

```java
package org.developers.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // Configurar rutas para SPA (Single Page Application)
        registry.addViewController("/").setViewName("forward:/index.html");
        registry.addViewController("/{x:[\\w\\-]+}").setViewName("forward:/index.html");
        registry.addViewController("/{x:^(?!api$).*$}/**").setViewName("forward:/index.html");
    }
}
```

### 4. Configuración de CORS (si es necesario)

Si tienes problemas de CORS, agrega esta configuración:

```java
package org.developers.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class CorsConfig {

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("https://localhost:8443");
        configuration.addAllowedOrigin("http://localhost:3000"); // Para desarrollo
        configuration.addAllowedMethod("*");
        configuration.addAllowedHeader("*");
        configuration.setAllowCredentials(true);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", configuration);
        return source;
    }
}
```

## 📁 Estructura Final

Después de la integración, tu estructura debería verse así:

```
src/main/resources/
├── static/
│   ├── index.html
│   ├── assets/
│   │   ├── index-[hash].js
│   │   ├── index-[hash].css
│   │   └── vendor-[hash].js
│   └── favicon.ico
├── application.properties
└── [otros archivos de configuración]
```

## 🚀 Despliegue

### 1. Ejecutar Spring Boot
```bash
./mvnw spring-boot:run
```

### 2. Acceder a la Aplicación
- Frontend: `https://localhost:8443`
- API: `https://localhost:8443/api/v1`

## 🔧 Verificación

### 1. Verificar que el Frontend se Sirve Correctamente
- Accede a `https://localhost:8443`
- Deberías ver la aplicación React cargando

### 2. Verificar que las Rutas Funcionan
- Navega a `/videos` - debería cargar la página de videos
- Navega a `/favorites` - debería cargar la página de favoritos
- Navega a `/login` - debería cargar el formulario de login

### 3. Verificar la Conexión SSL
- El navegador debería mostrar un candado verde
- Las llamadas a la API deberían ser HTTPS

## 🐛 Solución de Problemas

### Problema: Página en blanco
**Solución**: Verifica que los archivos de `dist/` estén en `src/main/resources/static/`

### Problema: Rutas no funcionan
**Solución**: Asegúrate de que `WebConfig.java` esté configurado correctamente

### Problema: Errores de CORS
**Solución**: Verifica la configuración de CORS y que las URLs coincidan

### Problema: SSL no funciona
**Solución**: Verifica que los certificados estén en el classpath y configurados correctamente

## 📝 Notas Importantes

1. **Certificados SSL**: Asegúrate de que los certificados estén en `src/main/resources/`
2. **Variables de Entorno**: El frontend está configurado para usar `https://localhost:8443`
3. **Caché**: El navegador puede cachear archivos estáticos, usa Ctrl+F5 para forzar recarga
4. **Puerto**: El frontend está configurado para el puerto 8443 con SSL

## 🎯 Resultado Final

Después de seguir estos pasos, tendrás:
- ✅ Frontend React integrado con Spring Boot
- ✅ Conexión SSL/TLS funcionando
- ✅ Estrategia de fallback implementada
- ✅ Todas las funcionalidades disponibles
- ✅ Aplicación completamente funcional en `https://localhost:8443` 