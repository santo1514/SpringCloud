package com.ssline.company.config;

import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@Configuration
@OpenAPIDefinition(info = @Info(title = "Company CRUD API", version = "1.0", description = "Documentation Company CRUD API v1.0"))
public class OpenApiConfig {}