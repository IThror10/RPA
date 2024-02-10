package com.RPA.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@SecurityScheme(
        name = "Bearer Authentication",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components())
                .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
                .addTagsItem(new io.swagger.v3.oas.models.tags.Tag().name("WebSocket Endpoints"))
                .path("/api/robot/connect", new PathItem()
                        .get(new Operation()
                                .summary("Create socket connection")
                                .responses(new ApiResponses()
                                        .addApiResponse("200",
                                                new ApiResponse().description("Socket connection established"))
                                )
                        )
                )
                .info(new Info().title("RPA -- Replace Routine with Robots")
                .version("1.0.0")
                .description("RPA Swagger Rest API"))
                ;

//        return new OpenAPI()
//                .info(new Info().title("RPA -- Replace Routine with Robots")
//                .version("1.0.0")
//                .description("RPA Swagger Rest API"));
    }
}