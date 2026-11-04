package com.fawords;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
		info = @Info(
				title = "Fawords API",
				version = "1.0.0",
				description = "REST API documentation for Fawords application, featuring authentication and general utility endpoints.",
				contact = @Contact(name = "Fawords Support", email = "support@fawords.com", url = "https://fawords.com"),
				license = @License(name = "Apache 2.0", url = "https://www.apache.org/licenses/LICENSE-2.0.html")
		),
		servers = {
				@Server(url = "/", description = "Default Server URL")
		}
)
public class FawordsApplication {

	public static void main(String[] args) {
		SpringApplication.run(FawordsApplication.class, args);
	}

}
