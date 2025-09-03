package org.example.userserviceapplication.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
        info = @Info(
                title = "User Service Api",
                description = "API сервиса юзеров",
                version = "1.0.0",
                contact = @Contact(
                        name = "Файзуллин Закир",
                        email = "zakirfajjzullin@gmail.com"
                )
        )
)

public class OpenApiConfig {}
