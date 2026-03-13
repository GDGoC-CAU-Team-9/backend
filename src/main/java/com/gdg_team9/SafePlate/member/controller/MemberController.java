package com.gdg_team9.SafePlate.member.controller;

import com.gdg_team9.SafePlate.api.ApiResponse;
import com.gdg_team9.SafePlate.member.domain.Member;
import com.gdg_team9.SafePlate.member.dto.MemberRequest;
import com.gdg_team9.SafePlate.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PatchMapping("/language")
    public ApiResponse<Void> updateLanguage(
            @AuthenticationPrincipal Member member,
            @Valid @RequestBody MemberRequest.UpdateLanguageRequest request
    ) {
        memberService.updateLanguage(member.getEmail(), request.getLanguage());
        return ApiResponse.onSuccess(null);
    }
}
