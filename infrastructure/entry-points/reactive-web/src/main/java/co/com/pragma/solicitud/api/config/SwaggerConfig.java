package co.com.pragma.solicitud.api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Autenticación API")
                        .version("1.0")
                        .description("Documentación de la API de Solcitiud con WebFlux y arquitectura hexagonal"));
    }
}
