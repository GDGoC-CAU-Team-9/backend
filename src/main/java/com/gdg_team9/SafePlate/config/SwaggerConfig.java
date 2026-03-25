package com.gdg_team9.SafePlate.config;

import com.gdg_team9.SafePlate.api.CommonResponse;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        Info info = new Info()
                .title("SafePlate API")
                .description("SafePlate API Documentation")
                .version("0.0.1");

        SecurityRequirement securityRequirement =
                new SecurityRequirement().addList("Bearer Authentication");

        // BadRequest 응답 예시 (JSON 객체)
        CommonResponse<Map<String, String>> validationErrorResponse =
                CommonResponse.onFailure(
                        "COMMON400",
                        "잘못된 요청입니다.",
                        Map.of("erroredConstraint", "오류 메시지")
                );

        Components components = new Components()
                .addSecuritySchemes("Bearer Authentication", new SecurityScheme()
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")
                        .description("Enter your JWT token"))
                // 재사용 가능한 에러 응답들
                .addResponses("BadRequest", new ApiResponse()
                        .description("요청 형식 오류")
                        .content(new Content().addMediaType("application/json",
                                new MediaType().schema(new Schema().$ref("#/components/schemas/BadRequestResponse")))))
                .addResponses("Unauthorized", new ApiResponse()
                        .description("인증 필요 (JWT 토큰 없음)"))
                .addResponses("Forbidden", new ApiResponse()
                        .description("접근 권한 없음"))
                // 응답 스키마 정의
                .addSchemas("BadRequestResponse", new Schema()
                        .example(validationErrorResponse));

        return new OpenAPI()
                .info(info)
                .addSecurityItem(securityRequirement)
                .components(components);
    }
}
