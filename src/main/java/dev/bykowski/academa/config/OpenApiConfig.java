/*
 * © 2024 bykowski. All rights reserved.
 *
 * This file is part of the Academa project.
 * You may not use this file except in compliance with the project license.
 *
 * Created on: 2024-10-16
 * File: OpenApiConfig.java
 */

package dev.bykowski.academa.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI defineOpenApi() {
        Server server = new Server();
        server.setUrl("http://localhost:8080");
        server.setDescription("Academa Docs");

        Contact myContact = new Contact();
        myContact.setName("Olaf Hubert Bykowski");
        myContact.email("bykowola@fit.cvut.cz");

        Info information = new Info()
                .title("Academa API")
                .version("0.0.0")
                .contact(myContact);

        return new OpenAPI().info(information).servers(List.of(server));
    }
}