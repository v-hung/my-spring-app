package com.example.demo.configurations;

import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@Configuration
@OpenAPIDefinition(info = @Info(contact = @Contact(name = "Việt Hùng", email = "viet.hung.2898@gmail.com"), description = "OpenApi document for Spring Security", title = "OpenApi specification - My Spring App", version = "1.0"), security = {
	@SecurityRequirement(name = "bearerAuth")
}, servers = {
	@Server(url = "http://localhost:8085", description = "Local Development Server"),
	@Server(url = "https://vigilant-fiesta-vgpwvq94pvwc666p-8085.app.github.dev", description = "GitHub Codespaces Server")
})
@SecurityScheme(name = "bearerAuth", description = "Jwt Authentication", scheme = "bearer", type = SecuritySchemeType.HTTP, bearerFormat = "JWT", in = SecuritySchemeIn.HEADER)
public class OpenApiConfig {

}
