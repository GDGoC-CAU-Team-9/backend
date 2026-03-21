package com.gdg_team9.SafePlate.config;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import java.lang.annotation.*;

/**
 * Swagger의 API 응답 문서화 중,
 * 인증 관련 API에서 공통적으로 발생할 수 있는 에러 응답들을 묶어서
 * 재사용하기 위한 커스텀 어노테이션
 * 이 어노테이션을 사용하면 400, 401, 403 에러 응답이 자동으로 추가되며,
 * Bearer Authentication 보안 요구사항도 함께 적용됨
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ApiResponses({
        @ApiResponse(responseCode = "400", ref = "#/components/responses/BadRequest"),
        @ApiResponse(responseCode = "401", ref = "#/components/responses/Unauthorized"),
        @ApiResponse(responseCode = "403", ref = "#/components/responses/Forbidden")
})
@SecurityRequirement(name = "Bearer Authentication")
public @interface AuthErrorResponses {
}
