package com.gdg_team9.SafePlate.member.controller;

import com.gdg_team9.SafePlate.api.ApiResponse;
import com.gdg_team9.SafePlate.api.code.status.SuccessStatus;
import com.gdg_team9.SafePlate.config.JwtUtil;
import com.gdg_team9.SafePlate.member.dto.MemberRequest;
import com.gdg_team9.SafePlate.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final MemberService memberService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @PostMapping("/join")
    public ApiResponse<Void> join(@Valid @RequestBody MemberRequest.JoinRequest request) {
        memberService.join(request.getEmail(), request.getPassword(), request.getName());
        return ApiResponse.of(SuccessStatus._CREATED, null);
    }

    @PostMapping("/login")
    public ApiResponse<Map<String, String>> login(@Valid @RequestBody MemberRequest.LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        String token = jwtUtil.generateToken(request.getEmail());
        return ApiResponse.of(SuccessStatus._OK, Map.of("token", token));
    }
}
