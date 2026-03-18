package com.gdg_team9.SafePlate.avoid.controller;

import com.gdg_team9.SafePlate.api.ApiResponse;
import com.gdg_team9.SafePlate.avoid.dto.AvoidPresetResponse;
import com.gdg_team9.SafePlate.avoid.service.AvoidPresetService;
import com.gdg_team9.SafePlate.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/avoid-presets")
@RequiredArgsConstructor
public class AvoidPresetController {

    private final AvoidPresetService avoidPresetService;

    /**
     * 모든 Preset 조회 (사용자 언어로 번역)
     */
    @GetMapping
    public ApiResponse<AvoidPresetResponse.PresetListResponse> getAllPresets(
            @AuthenticationPrincipal Member member
    ) {
        return ApiResponse.onSuccess(avoidPresetService.getAllPresets(member.getLanguage()));
    }

    /**
     * 특정 Preset 상세 조회
     */
    @GetMapping("/{presetId}")
    public ApiResponse<AvoidPresetResponse.PresetInfoResponse> getPresetDetail(
            @AuthenticationPrincipal Member member,
            @PathVariable Long presetId
    ) {
        return ApiResponse.onSuccess(avoidPresetService.getPresetDetail(presetId, member.getLanguage()));
    }
}
