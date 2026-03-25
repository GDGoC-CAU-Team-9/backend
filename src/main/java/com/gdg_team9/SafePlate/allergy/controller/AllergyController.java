package com.gdg_team9.SafePlate.allergy.controller;

import com.gdg_team9.SafePlate.allergy.dto.AllergyRequest;
import com.gdg_team9.SafePlate.allergy.dto.AllergyResponse;
import com.gdg_team9.SafePlate.allergy.service.AllergyService;
import com.gdg_team9.SafePlate.api.CommonResponse;
import com.gdg_team9.SafePlate.config.AuthErrorResponses;
import com.gdg_team9.SafePlate.member.domain.Member;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/allergies")
@RequiredArgsConstructor
@Tag(name = "Allergy", description = "알레르기 정보 관련 API")
public class AllergyController {
    private final AllergyService allergyService;

    @GetMapping
    @Operation(summary = "전체 알레르기 목록 조회", description = "시스템에 등록된 모든 알레르기 목록을 조회합니다")
    @ApiResponse(responseCode = "200", description = "조회 성공")
    public CommonResponse<AllergyResponse.AllergyListResponse> getAllergies() {
        AllergyResponse.AllergyListResponse response = allergyService.getAllergies();
        return CommonResponse.onSuccess(response);
    }

    @GetMapping("/my")
    @Operation(summary = "내 알레르기 정보 조회", description = "현재 사용자의 알레르기 정보를 조회합니다")
    @ApiResponse(responseCode = "200", description = "조회 성공")
    @AuthErrorResponses
    public CommonResponse<AllergyResponse.AllergyListResponse> getMyAllergies(
            @AuthenticationPrincipal Member member
    ) {
        String email = member.getEmail();
        AllergyResponse.AllergyListResponse response = allergyService.getMyAllergies(email);
        return CommonResponse.onSuccess(response);
    }

    @PutMapping("/my")
    @Operation(summary = "내 알레르기 정보 수정", description = "현재 사용자의 알레르기 정보를 수정합니다")
    @ApiResponse(responseCode = "200", description = "수정 성공")
    @AuthErrorResponses
    public CommonResponse<Void> updateMyAllergies(
            @AuthenticationPrincipal Member member,
            @Valid @RequestBody AllergyRequest.UpdateUserAllergyRequest request
    ) {
        String email = member.getEmail();
        allergyService.updateMyAllergies(email, request.getAllergyIds());
        return CommonResponse.onSuccess(null);
    }
}
