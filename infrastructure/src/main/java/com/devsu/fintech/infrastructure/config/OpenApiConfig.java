package com.devsu.fintech.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OpenAPI metadata for the client microservice.
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI clientOpenAPI() {
        return new OpenAPI().info(new Info()
                .title("Client Microservice API")
                .description("Client management for the Devsu fintech platform")
                .version("v1"));
    }
}
