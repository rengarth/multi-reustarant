package com.education.kitchen.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenAPIConfiguration {

    @Value(value = "${server.port}")
    int port;

    @Bean
    public OpenAPI defineOpenApi() {
        Server server = new Server();
        server.setUrl("http://localhost:" + port);
        server.setDescription("Development");

        Contact myContact = new Contact();
        myContact.setName("Fedyanin Victor");
        myContact.setEmail("fedyanin.v.v@yandex.ru");

        Components authorization = new Components()
                .addSecuritySchemes("basicAuth", new SecurityScheme()
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("basic"));


        Info information = new Info()
                .title("Restaurant Service API")
                .version("1.0")
                .description("description")
                .contact(myContact);
        return new OpenAPI()
                .info(information)
                .components(authorization)
                .addSecurityItem(new SecurityRequirement().addList("basicAuth"))
                .servers(List.of(server));
    }
}
