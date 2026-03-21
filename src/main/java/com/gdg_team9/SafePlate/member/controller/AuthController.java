package com.gdg_team9.SafePlate.member.controller;

import com.gdg_team9.SafePlate.api.CommonResponse;
import com.gdg_team9.SafePlate.api.code.status.ErrorStatus;
import com.gdg_team9.SafePlate.api.code.status.SuccessStatus;
import com.gdg_team9.SafePlate.config.JwtUtil;
import com.gdg_team9.SafePlate.exception.GeneralException;
import com.gdg_team9.SafePlate.member.dto.MemberRequest;
import com.gdg_team9.SafePlate.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Auth", description = "인증 관련 API")
public class AuthController {

    private final MemberService memberService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @PostMapping("/join")
    @Operation(summary = "회원가입", description = "새로운 사용자를 등록합니다")
    @ApiResponse(responseCode = "201", description = "회원가입 성공",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(example = """
                            {
                              "isSuccess": true,
                              "code": "COMMON201",
                              "message": "성공적으로 생성되었습니다.",
                              "success": true
                            }
                            """)))
    @ApiResponse(responseCode = "409", description = "이메일 중복",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(example = """
                            {
                              "isSuccess": false,
                              "code": "MEMBER4004",
                              "message": "이미 존재하는 이메일입니다.",
                              "success": false
                            }
                            """)))
    @ApiResponse(responseCode = "400", ref = "#/components/responses/BadRequest")
    public CommonResponse<Void> join(@Valid @RequestBody MemberRequest.JoinRequest request) {
        memberService.join(request.getEmail(), request.getPassword(), request.getLanguage());
        return CommonResponse.of(SuccessStatus._CREATED, null);
    }

    @PostMapping("/login")
    @Operation(summary = "로그인", description = "사용자 인증 후 JWT 토큰을 반환합니다")
    @ApiResponse(responseCode = "200", description = "로그인 성공",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(example = """
                            {
                              "isSuccess": true,
                              "code": "COMMON200",
                              "message": "성공입니다.",
                              "result": {
                                "token": "eyJhbGciOiJIUzI1NiJ9...."
                              },
                              "success": true
                            }
                            """)))
    @ApiResponse(responseCode = "404", description = "인증 실패",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(example = """
                            {
                              "isSuccess": false,
                              "code": "MEMBER4003",
                              "message": "해당하는 멤버가 없습니다.",
                              "success": false
                            }
                            """)))
    @ApiResponse(responseCode = "400", ref = "#/components/responses/BadRequest")
    public CommonResponse<Map<String, String>> login(@Valid @RequestBody MemberRequest.LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );

            String token = jwtUtil.generateToken(request.getEmail());
            return CommonResponse.of(SuccessStatus._OK, Map.of("token", token));
        } catch (AuthenticationException e) {
            throw new GeneralException(ErrorStatus.MEMBER_NOT_FOUND);
        }
    }
}
