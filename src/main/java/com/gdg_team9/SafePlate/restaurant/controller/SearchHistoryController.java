package com.gdg_team9.SafePlate.restaurant.controller;

import com.gdg_team9.SafePlate.api.CommonResponse;
import com.gdg_team9.SafePlate.config.AuthErrorResponses;
import com.gdg_team9.SafePlate.member.domain.Member;
import com.gdg_team9.SafePlate.restaurant.dto.SearchHistoryRequest;
import com.gdg_team9.SafePlate.restaurant.dto.SearchHistoryResponse;
import com.gdg_team9.SafePlate.restaurant.service.SearchHistoryService;
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
@RequestMapping("/histories")
@RequiredArgsConstructor
@Tag(name = "SearchHistory", description = "검색 이력 관련 API")
public class SearchHistoryController {
    private final SearchHistoryService searchHistoryService;

    @GetMapping
    @Operation(summary = "검색 이력 조회", description = "사용자의 검색 이력 목록을 페이지별로 조회합니다")
    @ApiResponse(responseCode = "200", description = "조회 성공")
    @AuthErrorResponses
    public CommonResponse<SearchHistoryResponse.PageResult> getMyHistories(
            @AuthenticationPrincipal Member member,
            @Valid @ModelAttribute SearchHistoryRequest.PageRequest pageRequest
    ) {
        SearchHistoryResponse.PageResult memberHistories =
                searchHistoryService.getMemberHistories(member, pageRequest.getPageNumber());
        return CommonResponse.onSuccess(memberHistories);
    }

    @DeleteMapping("/{historyId}")
    @Operation(summary = "검색 이력 삭제", description = "특정 검색 이력을 삭제합니다")
    @ApiResponse(responseCode = "200", description = "삭제 성공")
    @ApiResponse(responseCode = "404", description = "이력을 찾을 수 없음",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(example = """
                            {
                              "isSuccess": false,
                              "code": "HISTORY4004",
                              "message": "검색 기록을 찾을 수 없습니다.",
                              "success": false
                            }
                            """)))
    @AuthErrorResponses
    public CommonResponse<Void> deleteMyHistory(
            @AuthenticationPrincipal Member member,
            @PathVariable long historyId
    ) {
        searchHistoryService.deleteMemberHistory(member, historyId);
        return CommonResponse.onSuccess(null);
    }
}
