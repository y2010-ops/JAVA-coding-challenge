package com.example.customers.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

@Bean
public OpenAPI customOpenAPI() {
    String serverUrl = System.getenv("CODESPACE_NAME") != null
            ? "https://" + System.getenv("CODESPACE_NAME") + "-" + System.getenv("GITHUB_CODESPACES_PORT_FORWARDING_DOMAIN") + "-8080.app.github.dev"
            : "http://localhost:8080";

    return new OpenAPI()
            .addServersItem(new Server().url(serverUrl).description("API Server"));
}

}
