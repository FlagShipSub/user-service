package com.example.userservice.security.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("user-service")
                        .version("1.0.0")
                        .description("User Authentication Service")
                )
                .addSecurityItem(new SecurityRequirement().addList("BearerJWTSecurityScheme"))
                .components(
                        new Components()
                                .addSecuritySchemes(
                                        "BearerJWTSecurityScheme", new SecurityScheme()
                                                .name("BearerJWTSecurityScheme")
                                                .type(SecurityScheme.Type.HTTP)
                                                .scheme("BEARER")
                                                .bearerFormat("JWT")
                                )
                );
    }
}
