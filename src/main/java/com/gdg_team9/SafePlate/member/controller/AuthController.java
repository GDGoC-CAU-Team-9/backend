package com.gdg_team9.SafePlate.member.controller;

import com.gdg_team9.SafePlate.api.ApiResponse;
import com.gdg_team9.SafePlate.api.code.status.SuccessStatus;
import com.gdg_team9.SafePlate.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final MemberService memberService;

    @PostMapping("/join")
    public ApiResponse<Void> join(@Valid @RequestBody JoinRequest request){
        memberService.join(request.getEmail(), request.getPassword(), request.getName());
        return ApiResponse.of(SuccessStatus._NO_CONTENT, null);
    }
}
