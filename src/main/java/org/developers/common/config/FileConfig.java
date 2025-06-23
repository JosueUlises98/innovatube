package org.developers.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "archivo")
@Data
public class FileConfig {

    private Map<TipoArchivo, ConfiguracionTipoArchivo> configuraciones;

    @Data
    public static class ConfiguracionTipoArchivo {
        private List<String> extensionesPermitidas;
        private long tamanoMaximo;
        private String directorioAlmacenamiento;
    }

    public enum TipoArchivo {
        IMAGEN_PERFIL,
        VIDEO,
        DOCUMENTO
    }

}
