package com.gdg_team9.SafePlate.avoid.controller;

import com.gdg_team9.SafePlate.api.CommonResponse;
import com.gdg_team9.SafePlate.avoid.dto.AvoidItemRequest;
import com.gdg_team9.SafePlate.avoid.dto.AvoidItemResponse;
import com.gdg_team9.SafePlate.avoid.service.AvoidItemService;
import com.gdg_team9.SafePlate.config.AuthErrorResponses;
import com.gdg_team9.SafePlate.member.domain.Member;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/avoid-items")
@RequiredArgsConstructor
@Tag(name = "AvoidItem", description = "피해야 할 음식 관련 API")
public class AvoidItemController {

    private final AvoidItemService avoidItemService;

    @GetMapping("/my")
    @Operation(summary = "내 피해야 할 음식 조회", description = "현재 사용자가 설정한 피해야 할 음식 목록을 조회합니다")
    @ApiResponse(responseCode = "200", description = "조회 성공")
    @AuthErrorResponses
    public CommonResponse<AvoidItemResponse.MyAvoidResponse> getMyAvoid(
            @AuthenticationPrincipal Member member
    ) {
        return CommonResponse.onSuccess(avoidItemService.getMyAvoid(member));
    }

    @PutMapping("/my")
    @Operation(summary = "내 피해야 할 음식 수정", description = "현재 사용자의 피해야 할 음식 목록을 수정합니다")
    @ApiResponse(responseCode = "200", description = "수정 성공")
    @AuthErrorResponses
    public CommonResponse<AvoidItemResponse.MyAvoidResponse> saveMyAvoid(
            @AuthenticationPrincipal Member member,
            @Valid @RequestBody AvoidItemRequest.SaveRequest request
    ) {
        return CommonResponse.onSuccess(avoidItemService.updateMyAvoid(member, request.getItems()));
    }

    @PostMapping("/my/search")
    @Operation(summary = "피해야 할 음식 검색", description = "텍스트로부터 피해야 할 음식을 자동으로 추출합니다")
    @ApiResponse(responseCode = "200", description = "검색 성공")
    @ApiResponse(responseCode = "500", description = "AI 서버 에러",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(example = """
                            {
                              "isSuccess": false,
                              "code": "AI5000",
                              "message": "AI 서버에서 문제가 발생했습니다.",
                              "success": false
                            }
                            """)))
    @AuthErrorResponses
    public CommonResponse<AvoidItemResponse.ExtractedAvoidResponse> searchMyAvoid(
            @AuthenticationPrincipal Member member,
            @Valid @RequestBody AvoidItemRequest.TextRequest request
    ) {
        return CommonResponse.onSuccess(avoidItemService.extractMyAvoid(member, request.getText()));
    }
}
