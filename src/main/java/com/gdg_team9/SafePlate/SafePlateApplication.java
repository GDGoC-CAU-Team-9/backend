package com.gdg_team9.SafePlate;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
@EnableFeignClients
@OpenAPIDefinition(
        servers = {
                @Server(url = "http://localhost:8080", description = "Local development server"),
                @Server(url = "${be-server.url}", description = "Test/Production server"),
        }
)
public class SafePlateApplication {

    public static void main(String[] args) {
        SpringApplication.run(SafePlateApplication.class, args);
    }

}
