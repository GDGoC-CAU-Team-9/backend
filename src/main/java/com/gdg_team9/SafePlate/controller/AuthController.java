package com.gdg_team9.SafePlate.controller;

import com.gdg_team9.SafePlate.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final MemberService memberService;

    @PostMapping("/join")
    public String join(@RequestBody JoinRequest request){
        memberService.join(request.getEmail(), request.getPassword(), request.getName());
        return "회원가입 성공";
    }
}
