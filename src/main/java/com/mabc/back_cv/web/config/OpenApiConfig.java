package com.mabc.back_cv.web.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        final String securitySchemeName = "bearerAuth";
        
        return new OpenAPI()
                // Información general de la API
                .info(new Info()
                        .title("API de Mi Aplicación Backend para mi CV Web")
                        .version("1.0.0")
                        .description("Documentación de los endpoints protegidos con JWT.")
                        .contact(new Contact()
                                .name("Soporte Técnico")
                                .email("mabc@live.cl")))
                
                // Aplica la autenticación de forma global a todos los endpoints en la UI de Swagger
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                
                // Configuración del esquema de seguridad JWT
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName,
                                new SecurityScheme()
                                        .name(securitySchemeName)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("Introduce el token JWT en este formato: Bearer [tu_token]")));
    }
}