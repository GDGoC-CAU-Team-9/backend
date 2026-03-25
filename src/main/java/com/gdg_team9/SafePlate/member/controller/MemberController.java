package com.gdg_team9.SafePlate.member.controller;

import com.gdg_team9.SafePlate.api.CommonResponse;
import com.gdg_team9.SafePlate.config.AuthErrorResponses;
import com.gdg_team9.SafePlate.member.domain.Member;
import com.gdg_team9.SafePlate.member.dto.MemberRequest;
import com.gdg_team9.SafePlate.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Member", description = "사용자 정보 관련 API")
public class MemberController {

    private final MemberService memberService;

    @PatchMapping("/language")
    @Operation(summary = "언어 설정 변경", description = "사용자의 언어 설정을 변경합니다")
    @ApiResponse(responseCode = "200", description = "변경 성공")
    @AuthErrorResponses
    public CommonResponse<Void> updateLanguage(
            @AuthenticationPrincipal Member member,
            @Valid @RequestBody MemberRequest.UpdateLanguageRequest request
    ) {
        memberService.updateLanguage(member.getId(), request.getLanguage());
        return CommonResponse.onSuccess(null);
    }
}
