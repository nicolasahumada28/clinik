package com.timmynet.clinik.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI clinicOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("Clinik Microservicio API")
                .description("API REST de gestión clínica para usuarios, pacientes, profesionales, citas, exámenes, órdenes y facturación.")
                .version("v1")
                .contact(new Contact().name("Equipo Clinik").email("soporte@clinik.local")));
    }
}
