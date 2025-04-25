package com.agencia.viagens.sistema.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringDocOpenApiConfig {
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI().components(new Components()).info(new Info().title("REST API - Agencia Viagens")
                        .description("Aplicativo para gestão do sistema de uma agencia de viagens.")
                        .version("v1")
//                .license(new License().name("").url(""))
        );
    }

}
