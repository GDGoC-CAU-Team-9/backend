package com.gdg_team9.SafePlate.avoid.controller;

import com.gdg_team9.SafePlate.api.CommonResponse;
import com.gdg_team9.SafePlate.avoid.dto.AvoidPresetResponse;
import com.gdg_team9.SafePlate.avoid.service.AvoidPresetService;
import com.gdg_team9.SafePlate.config.AuthErrorResponses;
import com.gdg_team9.SafePlate.member.domain.Member;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/avoid-presets")
@RequiredArgsConstructor
@Tag(name = "AvoidPreset", description = "피해야 할 음식 프리셋 관련 API")
public class AvoidPresetController {

    private final AvoidPresetService avoidPresetService;

    @GetMapping
    @Operation(summary = "모든 피해야 할 음식 프리셋 조회", description = "사용자 언어로 번역된 모든 프리셋 목록을 조회합니다")
    @ApiResponse(responseCode = "200", description = "조회 성공")
    @AuthErrorResponses
    public CommonResponse<AvoidPresetResponse.PresetListResponse> getAllPresets(
            @AuthenticationPrincipal Member member
    ) {
        return CommonResponse.onSuccess(avoidPresetService.getAllPresets(member.getLanguage()));
    }

    @GetMapping("/{presetId}")
    @Operation(summary = "피해야 할 음식 프리셋 상세 조회", description = "특정 프리셋의 상세 정보를 사용자 언어로 조회합니다")
    @ApiResponse(responseCode = "200", description = "조회 성공")
    @ApiResponse(responseCode = "404", description = "프리셋을 찾을 수 없음",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(example = """
                            {
                              "isSuccess": false,
                              "code": "PRESET4004",
                              "message": "프리셋을 찾을 수 없습니다.",
                              "success": false
                            }
                            """)))
    @AuthErrorResponses
    public CommonResponse<AvoidPresetResponse.PresetInfoResponse> getPresetDetail(
            @AuthenticationPrincipal Member member,
            @PathVariable Long presetId
    ) {
        return CommonResponse.onSuccess(avoidPresetService.getPresetDetail(presetId, member.getLanguage()));
    }
}
